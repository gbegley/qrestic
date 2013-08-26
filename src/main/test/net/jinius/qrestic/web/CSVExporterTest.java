package net.jinius.qrestic.web;

import net.jinius.qrestic.config.DataConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import javax.sql.DataSource;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.LinkedHashMap;

/**
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={DataConfig.class}, loader=AnnotationConfigContextLoader.class)
public class CSVExporterTest {

    @Autowired
    private DataSource dataSource;

    @Test
    public void testExportJobs() throws IOException {
        CSVExporter csvExporter = new CSVExporter();
        csvExporter.setFlushInterval(50);
        csvExporter.setDataSource(dataSource);

        LinkedHashMap<String,String> ch = new LinkedHashMap<String, String>();
        ch.put("NAME","Job Name");
        ch.put("JOB_CLASS","Classy");
        ch.put("FAKE_FIELD","Fake Field");
        ch.put("JOB_STATUS","Status");
        csvExporter.addConfig(new ExportConfig("Jobs", "select * from job",ch));
        log.info("Exporting Jobs");
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        csvExporter.exportTo(bos);
        log.info("Exported:\n{}",new String(bos.toByteArray()));
    }

    Logger log = LoggerFactory.getLogger(getClass());
}
