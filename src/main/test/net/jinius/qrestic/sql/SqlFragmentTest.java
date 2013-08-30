package net.jinius.qrestic.sql;

import com.google.common.base.Joiner;
import junit.framework.Assert;
import net.jinius.qrestic.config.DataConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.web.context.request.ServletWebRequest;

/**
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={DataConfig.class}, loader=AnnotationConfigContextLoader.class)
public class SqlFragmentTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void testSimple(){
        SimpleFragment sqb = new SimpleFragment("select 1");
        String sql = sqb.get();
        int i = jdbcTemplate.queryForInt(sql);
        Assert.assertEquals(1, i);
    }

    @Test
    public void testWebRequestResolvingSqlProvider(){
        MockHttpServletRequest mockServletRequest = new MockHttpServletRequest();
        mockServletRequest.addParameter("SEGPROD_EFF_FAMILY","!DOV");
        mockServletRequest.addParameter("SEGPROD_EFF", Joiner.on("|").join("ABD6","ABD8","ABD9"));
        mockServletRequest.addParameter("SEGCUST_EFF", Joiner.on("|").join("IND2-7","IND2-9","IND1-9"));
        mockServletRequest.addParameter("TARGET", Joiner.on("~").join("0.25","0.30"));
        mockServletRequest.addParameter("JOB_FK","48");
        ServletWebRequest webRequest = new ServletWebRequest(mockServletRequest);
        WebRequestCriteriaResolver p = new WebRequestCriteriaResolver(jdbcTemplate);
        p.setObject("matrix");
        Criteria criteria = p.resolve(webRequest);

        Selection sc = new Selection("matrix")
                .withFields("segcust_eff")
                .withAggregates("sum(ORDER_AMT_SALES_DOLLARS) as sales, sum(ORDER_AMT_COGS_DOLLARS) as costs")
                .withCriteria( criteria )
                .withObject("matrix")
                ;
        String sql = sc.get();
        System.out.println("Generated SQL: \n" + sql);
    }

}
