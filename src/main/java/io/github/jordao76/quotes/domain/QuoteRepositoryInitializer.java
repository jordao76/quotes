package io.github.jordao76.quotes.domain;

import java.util.*;

import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

@Component
public class QuoteRepositoryInitializer {

  @Autowired
  public QuoteRepositoryInitializer(QuoteRepository repo) {
    if (repo.count() > 0) return;
    repo.save(getInitialQuotes());
  }

  private List<Quote> getInitialQuotes() {
    return Arrays.asList(
      new Quote(
        "Any sufficiently advanced technology is indistinguishable from magic.",
        "Arthur C. Clarke"),
      new Quote(
        "Perfection (in design) is achieved not when there is nothing more to add, but rather when there is nothing more to take away.",
        "Antoine de Saint-Exupery"),
      new Quote(
        "On two occasions I have been asked, 'Pray, Mr. Babbage, if you put into the machine wrong figures, will the right answers come out?' I am not able rightly to apprehend the kind of confusion of ideas that could provoke such a question.",
        "Charles Babbage"));
  }

}
