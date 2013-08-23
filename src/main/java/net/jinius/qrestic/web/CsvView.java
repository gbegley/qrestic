package net.jinius.qrestic.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.View;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 *
 */
public class CsvView implements View {

    public static enum Type {
        CSV("text/csv",","),
        TSV("text/tsv","\t");
        Type(String contentType, String delimiter) {
            this.contentType = contentType;
            this.delimiter = delimiter;
        }
        public final String contentType;
        public final String delimiter;
    }

    private Type type = Type.CSV;
    private CSVExporter csvExporter;

    public CsvView(CSVExporter csvExporter) {
        this.csvExporter = csvExporter;
    }

    @Override
    public String getContentType() {
        return type.contentType;
    }

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.info("{} exporting results using configs:\n{}",request.getRequestURI(), csvExporter.getConfigList());
        csvExporter.exportTo(response.getOutputStream());
    }


    public void setType(Type type) {
        this.type = type;
        csvExporter.setFieldDelimiter(type.delimiter);
    }

    public void setRowDelimiter(String cs){
        csvExporter.setRowDelimiter(cs);
    }

    private Logger log = LoggerFactory.getLogger(getClass());
}
