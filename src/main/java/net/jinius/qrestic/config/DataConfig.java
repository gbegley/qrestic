package net.jinius.qrestic.config;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.inject.Inject;
import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement // (mode=AdviceMode.ASPECTJ)
@PropertySource(value="classpath:application.properties")
public class DataConfig {

    @Inject
    Environment environment;

    @Inject
    DataSource dataSource;

    @Bean
    public DataSource dataSource(){
        BasicDataSource ds = new BasicDataSource();
        ds.setTestOnBorrow(true);
        ds.setTestWhileIdle(true);
        ds.setValidationQuery("show variables like 'version'");
        ds.setDriverClassName(environment.getProperty("database.driverClassName"));
        ds.setUsername(environment.getProperty("database.username"));
        ds.setPassword(environment.getProperty("database.password"));
        ds.setUrl(environment.getProperty("database.url"));
        return ds;
    }

    /**
     * Allows repositories to access RDBMS data using the JDBC API.
     */
    @Bean
    @Autowired
    public JdbcTemplate jdbcTemplate( DataSource dataSource ) {
        return new JdbcTemplate(dataSource);
    }

    /**
     * Allows transactions to be managed against the RDBMS using the JDBC API.
     */
    @Bean
    public PlatformTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource);
    }

}
