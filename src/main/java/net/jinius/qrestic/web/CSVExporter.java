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
public class CSVExporter {

    private DataSource dataSource;
    private LinkedList<ExportConfig> configList = new LinkedList<ExportConfig>();
    private int flushInterval = 1000;
    private String fieldDelimiter = ",";
    private String rowDelimiter = "\n";
    private String dataSetDelimiter = "\n\n\n";
    private int maxRows = Integer.MAX_VALUE;
    private String nullValue = "";
    private String booleanTrueValue = "1";
    private CharSequence fieldSeparator = ",";

    public CSVExporter(DataSource dataSource,ExportConfig ec) {
        this.dataSource = dataSource;
        configList.add(ec);
    }

    public CSVExporter() {
    }

    public CSVExporter addConfig(ExportConfig config) {
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
        for(int c=0;c<configList.size();c++) {
            if(c>0) {
                pw.append(dataSetDelimiter);
            }
            final ExportConfig exportConfig = configList.get(c);
            String sql = configList.get(c).sql;
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
                        for(String s : exportConfig.columnHeaderMap.values()) {
                            if(c==0) {
                                // if "ID" is at the start of the file, excel will try to open the file
                                // as a SYLK file which is wrong
                                if("ID".equals(s)) s = "_"+s;
                            } else {
                                pw.append(fieldDelimiter);
                            }

                            pw.append(s);
                            c++;
                        }
                        pw.append(rowDelimiter);
                        renderedHeaders = true;
                    }

                    private void renderRow(ResultSet rs) throws IOException, SQLException {
                        int c = 0;
                        for(String s : exportConfig.columnHeaderMap.keySet()) {
                            if(c>0) pw.append(fieldDelimiter);
                            Integer I = exportConfig.columnIndexMap.get(s);
                            if(I!=null ) {
                                String formattedField = formatField(rs, I);
                                if(formattedField.contains(fieldDelimiter)) {
                                    formattedField = String.format("\"%s\"",formattedField);
                                }
                                pw.append(formattedField);
                            } else {
                                pw.append(nullValue);
                            }
                            c++;
                        }
                        pw.append(rowDelimiter);
                    }
                });
            } catch (MaxRowsExceededException xe) {
                log.warn("Reached max rows: {}",maxRows);
            }
        }
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

    public void setFieldDelimiter(String fieldDelimiter) {
        this.fieldDelimiter = fieldDelimiter;
    }

    public void setRowDelimiter(String rowDelimiter) {
        this.rowDelimiter = rowDelimiter;
    }

    public void setDataSetDelimiter(String dataSetDelimiter) {
        this.dataSetDelimiter = dataSetDelimiter;
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


    NumberFormat numberFormat = NumberFormat.getNumberInstance();
    DateFormat dateFormat = new SimpleDateFormat("MM/dd/YYYY");

}
