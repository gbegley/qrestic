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
public class JSONView implements View {

    public static String type = "text/json";
    private JSONExporter jsonExporter;

    public JSONView(JSONExporter jsonExporter) {
        this.jsonExporter = jsonExporter;
    }

    @Override
    public String getContentType() {
        return type;
    }

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.info("{} exporting results using configs:\n{}", request.getRequestURI(), jsonExporter.getConfigList());
        jsonExporter.exportTo(response.getOutputStream());
    }


    public void setType(String type) {
        this.type = type;
    }

    private Logger log = LoggerFactory.getLogger(getClass());
}
