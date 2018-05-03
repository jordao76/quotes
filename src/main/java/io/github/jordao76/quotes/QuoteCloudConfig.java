package io.github.jordao76.quotes;

import javax.sql.*;

import org.springframework.cloud.config.java.*;
import org.springframework.context.annotation.*;

@Configuration
@Profile("cloud")
public class QuoteCloudConfig extends AbstractCloudConfig {

  @Bean
  public DataSource quoteDataSource() {
    return connectionFactory().dataSource("quotes-db");
  }

}
