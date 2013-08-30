package net.jinius.qrestic.web;

import net.jinius.qrestic.sql.Criteria;
import net.jinius.qrestic.sql.Selection;
import net.jinius.qrestic.sql.WebRequestCriteriaResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.util.Map;

/**
 *
 */
@Controller
@RequestMapping("/data/")
public class DataController implements ApplicationContextAware {

    @Inject
    private DataSource dataSource;

    private Map<String,String> queries;
    private ApplicationContext ac;


    @RequestMapping("/view/{table}")
    public ModelAndView viewTable(
            @RequestParam(required = false, defaultValue = "tsv") String type,
            @PathVariable String table,
            WebRequest request,
            HttpServletResponse response
    ) throws IOException {
        CsvView.Type vt = CsvView.Type.valueOf(type.toUpperCase());
        WebRequestCriteriaResolver resolver = new WebRequestCriteriaResolver(new JdbcTemplate(dataSource));
        resolver.setObject(table);
        Criteria criteria = resolver.resolve(request);
        Selection selection = new Selection(table).withCriteria(criteria);
        String sql = selection.get();
        return renderSqlAs(vt,new ExportConfig(table,sql),response);
    }

    @RequestMapping("/table/{table}")
    public ModelAndView listTable(
            @PathVariable String table,
            WebRequest request,
            HttpServletResponse response
    ) throws IOException {
        WebRequestCriteriaResolver resolver = new WebRequestCriteriaResolver(new JdbcTemplate(dataSource));
        resolver.setObject(table);
        Criteria criteria = resolver.resolve(request);
        Selection selection = new Selection(table).withCriteria(criteria);
        String sql = selection.get();
        return renderSqlAs(CsvView.Type.TSV,new ExportConfig(table,sql),response);
    }

    @RequestMapping("/query/{name}")
    public ModelAndView listByNamedQuery(
            @PathVariable String name,
            WebRequest request,
            HttpServletResponse response
    ) throws IOException {
        WebRequestCriteriaResolver resolver = new WebRequestCriteriaResolver(new JdbcTemplate(dataSource));
        Map<String,String> queries = findQueriesMap();
        String query = queries.get(name);
        String object = String.format("(%s) t",query);
        resolver.setObject(object);
        Criteria criteria = resolver.resolve(request);
        Selection selection = new Selection(object).withCriteria(criteria);
        String sql = selection.get();
        return renderSqlAs(CsvView.Type.TSV,new ExportConfig(name,sql),response);
    }

    private Map<String, String> findQueriesMap() {
        Map<String,String> queries = this.queries;
        if(queries==null) try {
            queries = ac.getBean("queries",Map.class);
        } catch(NoSuchBeanDefinitionException nsb) {
            log.debug("No Bean Found named \"queries\"");
        }
        return queries;
    }

    public ModelAndView renderSqlAs(CsvView.Type t, ExportConfig ec, HttpServletResponse response) throws IOException {
        log.debug("CSV gen is using SQL:\n{}", ec.getSql());
        CsvView v = new CsvView(new CSVExporter(dataSource,ec));
        v.setType(t);
        return new ModelAndView(v);
    }

    public ModelAndView renderSqlMetaDataAsCsv(String name, String sql, HttpServletResponse response) throws IOException {

        ExportConfig exportConfig = new ExportConfig(name,sql);
        log.debug("CSV gen is using SQL:\n{}", exportConfig.getSql());
        CsvView v = new CsvView(new CSVExporter(dataSource,exportConfig));
        v.setType(CsvView.Type.TSV);
        return new ModelAndView(v);
    }


    Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.ac = applicationContext;
    }


    @Autowired(required = false)
    @Qualifier("queries")
    public void setQueriesMap(Map queries) {
        this.queries = queries;
    }
}
