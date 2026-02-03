package com.awerreviews.desafio.webapp.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@EnableWebMvc
@EnableTransactionManagement
@ComponentScan({"com.awerreviews.desafio.webapp.controller", "com.awerreviews.desafio.service", "com.awerreviews.desafio.persistence"})
@Configuration
@PropertySource("classpath:config/database.properties")
public class WebConfig {
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        final LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
        factoryBean.setPackagesToScan("com.awerreviews.desafio.model");
        factoryBean.setDataSource(dataSource());

        final JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        factoryBean.setJpaVendorAdapter(vendorAdapter);

        final Properties properties = new Properties();
        properties.setProperty("hibernate.hbm2ddl.auto", "update");
        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL8Dialect");


        properties.setProperty("hibernate.show_sql", "true");
        properties.setProperty("format_sql", "true");

        factoryBean.setJpaProperties(properties);
        return factoryBean;
    }

    @Value("${db.username}")
    private String dbUser;
    @Value("${db.password}")
    private String dbPassword;
    @Value("${db.url}")
    private String dbUrl;

    @Bean
    public DataSource dataSource() {

        final SimpleDriverDataSource ds = new SimpleDriverDataSource();
        ds.setDriverClass(com.mysql.cj.jdbc.Driver.class);
        ds.setUrl(dbUrl);
        ds.setUsername(dbUser);
        ds.setPassword(dbPassword);
        return ds;
    }

    @Bean
    public PlatformTransactionManager transactionManager(final EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

    @Bean
    public MultipartResolver multipartResolver() {
        return new StandardServletMultipartResolver();
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    public Executor taskExecutor() {
        return Executors.newFixedThreadPool(4);
    }

}
