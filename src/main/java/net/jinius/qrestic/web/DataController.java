package net.jinius.qrestic.web;

import net.jinius.qrestic.sql.Criteria;
import net.jinius.qrestic.sql.Selection;
import net.jinius.qrestic.sql.WebRequestCriteriaResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;

/**
 *
 */
@Controller
@RequestMapping("/data/")
public class DataController {

    @Inject
    private DataSource dataSource;



    @RequestMapping("/table/{table}")
    public ModelAndView listJobs(
            @PathVariable String table,
            WebRequest request,
            HttpServletResponse response
    ) throws IOException {
        WebRequestCriteriaResolver resolver = new WebRequestCriteriaResolver(new JdbcTemplate(dataSource));
        resolver.setObject(table);
        Criteria criteria = resolver.resolve(request);
        Selection selection = new Selection(table).withCriteria(criteria);
        String sql = selection.get();
        return renderSqlAsCsv(table,sql,response);
    }

    public ModelAndView renderSqlAsCsv(String name, String sql, HttpServletResponse response) throws IOException {
        ExportConfig exportConfig = new ExportConfig(name,sql);
        log.debug("CSV gen is using SQL:\n{}", exportConfig.getSql());
        CsvView v = new CsvView(new CSVExporter(dataSource,exportConfig));
        v.setType(CsvView.Type.TSV);
        return new ModelAndView(v);
    }


    Logger log = LoggerFactory.getLogger(getClass());
}
