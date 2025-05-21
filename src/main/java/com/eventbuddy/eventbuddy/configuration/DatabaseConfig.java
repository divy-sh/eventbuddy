package com.eventbuddy.eventbuddy.configuration;

import java.util.Objects;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configuration
public class DatabaseConfig {

  @Autowired
  private Environment environment;

  @Bean
  DataSource dataSource() {
    DriverManagerDataSource dataSource = new DriverManagerDataSource();
    dataSource.setDriverClassName(Objects.requireNonNull(environment.getProperty("db.type")));
    dataSource.setUrl(Objects.requireNonNull(environment.getProperty("db.url")));
    dataSource.setUsername(Objects.requireNonNull(environment.getProperty("db.username")));
    dataSource.setPassword(Objects.requireNonNull(environment.getProperty("db.password")));
    return dataSource;
  }

  @Bean
  JdbcTemplate jdbcTemplate(DataSource dataSource) {
    return new JdbcTemplate(dataSource);
  }
}