package net.jinius.qrestic.web;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.io.*;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

/**
 *
 */
@Component
@Scope("prototype")
public class JSONExporter {

    private DataSource dataSource;
    private LinkedList<ExportConfig> configList = new LinkedList<ExportConfig>();
    private int flushInterval = 1000;
    private int maxRows = Integer.MAX_VALUE;
    private String nullValue = "";
    private String booleanTrueValue = "1";

    public JSONExporter(DataSource dataSource, ExportConfig ec) {
        this.dataSource = dataSource;
        configList.add(ec);
    }

    public JSONExporter() {
    }

    public JSONExporter addConfig(ExportConfig config) {
        configList.add(config);
        return this;
    }


    public void exportTo( File file ) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        try {
            exportTo( fileOutputStream );
        } finally {
            fileOutputStream.close();
        }
    }

    public void exportTo( OutputStream os ) throws IOException {
        //final PrintWriter pw = new PrintWriter(outputStream);\
        final OutputStreamWriter pw = new OutputStreamWriter(os);
        JdbcTemplate jt = new JdbcTemplate(dataSource);
        pw.append("{");
        for(int c=0;c<configList.size();c++) {
            final ExportConfig exportConfig = configList.get(c);
            nv(pw,"name",exportConfig.getName());
            String sql = configList.get(c).sql;
            pw.append(",\n");
            nv(pw,"sql",sql);
            pw.append(",\n");

            try {
                jt.query(sql,new RowCallbackHandler() {
                    boolean renderedHeaders = false;
                    int rowIndex = 0;
                    @Override
                    public void processRow(ResultSet rs) throws SQLException {
                        if(rowIndex>maxRows) throw new MaxRowsExceededException();
                        ResultSetMetaData md =  rs.getMetaData();
                        int cols = md.getColumnCount();
                        try {
                            if(!renderedHeaders) {
                                renderHeaders(rs);
                                pw.append("\"\n ,rows\":[");
                            }
                            renderRow(rs);
                            rowIndex++;
                            if(rowIndex%flushInterval==0) {
                                log.debug("Flushing CSV Export after {} rows", rowIndex);
                                pw.flush();
                            }
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    private void renderHeaders(ResultSet rs) throws SQLException, IOException {
                        ResultSetMetaData md = rs.getMetaData();
                        int cols = md.getColumnCount();
                        int c = 0;
                        if(exportConfig.columnHeaderMap.size()==0) {
                            for(int i=1;i<=cols;i++) {
                                String hName = formatColumnHeadingName(exportConfig,rs, i);
                                String cName = md.getColumnName(i);
                                exportConfig.columnHeaderMap.put(cName, hName);
                                exportConfig.columnIndexMap.put(cName, i);
                            }
                        } else {
                            for(int i=1;i<=cols;i++) {
                                String cName = md.getColumnName(i);
                                exportConfig.columnIndexMap.put(cName, i);
                            }
                        }

                        pw.append("\n\t");
                        pw.append(QUOTE).append("metadata").append(QUOTE).append(COLON);
                        pw.append("[");
                        for(String s : exportConfig.columnHeaderMap.values()) {
                            pw.append("\n\t\t");
                            pw.append('{');
                                nv(pw,"name",s);
                                pw.append(',');
                                nv(pw,"label",exportConfig.columnHeaderMap.get(s));
                                pw.append(',');
                                nv(pw,"index",String.valueOf( exportConfig.columnIndexMap.get(s)));
                            pw.append('}');
                        }
                        pw.append("]");
                        renderedHeaders = true;
                    }

                    private void renderRow(ResultSet rs) throws IOException, SQLException {
                        pw.append("\n\t");
                        pw.append(rowIndex>0 ? ",": " ");
                        pw.append("{");
                        int i=0;
                        for(String col : exportConfig.getColumnHeaderMap().keySet()){
                            Integer index = exportConfig.getColumnIndexMap().get(col);
                            String formattedField = index!=null? formatField(rs, index) : null;
                            if(i>1)pw.append(',');
                            nv(pw,col,formattedField);
                        }
                        pw.append("}");
                        rowIndex++;
                    }
                });
            } catch (MaxRowsExceededException xe) {
                log.warn("Reached max rows: {}",maxRows);
            }
            pw.append("\n]");
        }
        pw.append("}");
        pw.flush();

    }

    protected String formatColumnHeadingName(ExportConfig conf, ResultSet rs, int i) throws SQLException {
        String colName = rs.getMetaData().getColumnLabel(i);
        String mappedName = conf.columnHeaderMap.get(colName);
        return StringUtils.defaultIfEmpty(mappedName,colName);
    }

    /**
     * If necessary, override this method to perform some customization. For example, format dates or numbers,
     * escape delimiters, etc.
     *
     * @param rs The Result Set
     *
     * @param i The result set index of the field to extract and format.
     *
     * @return The string formatted value of the field to write to the CSV attribute
     *
     * @throws java.sql.SQLException If their is a problem accessing the result set index.
     */
    protected String formatField(ResultSet rs, int i) throws SQLException {
        String r = nullValue;
        Object o = rs.getObject(i);
        if(o!=null) {
            if(o instanceof String) {
                r = (String) o;
            } else  if(o instanceof Number) { // format number;
                r = ((Number)o).toString();
            } else if (o instanceof Date) { // format date;
                r = dateFormat.format( (Date)o);
            } else if(o instanceof Boolean && Boolean.TRUE.equals(o) ){
                r = booleanTrueValue;
            }
        } else {
            r = nullValue;
        }
        return r;
    }

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public LinkedList<ExportConfig> getConfigList() {
        return configList;
    }

    public void setConfigList(LinkedList<ExportConfig> configList) {
        this.configList = configList;
    }

    public void setFlushInterval(int flushInterval) {
        this.flushInterval = flushInterval;
    }

    public void setMaxRows(int maxRows) {
        this.maxRows = maxRows;
    }

    public void setNullValue(String nullValue) {
        this.nullValue = nullValue;
    }

    Logger log = LoggerFactory.getLogger(getClass());


    private class MaxRowsExceededException extends RuntimeException {

    }


    private static void nv(Appendable a, String n, String v) throws IOException {
        a.append(QUOTE);
        a.append( jsonEscape(n)  );
        a.append(QUOTE);
        a.append(COLON);
        if(v!=null) {
            a.append(QUOTE);
            a.append( jsonEscape(v) );
            a.append(QUOTE);
        } else {
            a.append("null");
        }
    }

    private static String jsonEscape(String s) {
        if(s!=null && s.contains("\"")) s = s.replace("\"","\\\"");
        return s;
    }

    private static final Character QUOTE = '"';
    private static final Character COLON = ':';

    NumberFormat numberFormat = NumberFormat.getNumberInstance();
    DateFormat dateFormat = new SimpleDateFormat("MM/dd/YYYY");

}
