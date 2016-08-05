package io.github.jordao76.quotes.domain;

import java.util.*;

import org.springframework.beans.factory.annotation.*;
import org.springframework.core.io.*;
import org.springframework.stereotype.*;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.dataformat.yaml.*;

@Component
public class QuoteRepositoryInitializer {

  @Autowired
  public QuoteRepositoryInitializer(QuoteRepository repo) {
    if (repo.count() > 0) return;
    repo.save(getInitialQuotes());
  }

  private List<Quote> getInitialQuotes() {
    try {
      ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
      return Arrays.asList(
        mapper.readValue(
          new ClassPathResource("quotes.yml").getInputStream(),
          Quote[].class));
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

}
