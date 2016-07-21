package io.github.jordao76.quotes;

import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.context.annotation.*;

import io.github.jordao76.quotes.domain.*;

@SpringBootApplication
public class QuoteApplication {

  public static void main(String[] args) {
    SpringApplication.run(QuoteApplication.class, args);
  }

  @Bean
  public CommandLineRunner populateRepo(QuoteRepository repo) {
    return args -> repo.populate();
  }

}
