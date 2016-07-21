package io.github.jordao76.quotes.controllers;

import static org.springframework.web.bind.annotation.RequestMethod.*;

import org.springframework.beans.factory.annotation.*;
import org.springframework.web.bind.annotation.*;

import io.github.jordao76.quotes.domain.*;

@RestController
public class QuoteController {

  private QuoteRepository repo;

  @Autowired
  public QuoteController(QuoteRepository repo) {
    this.repo = repo;
  }

  @RequestMapping(value = "/quotes", method = GET)
  public Iterable<Quote> getQuotes() {
    return repo.findAll();
  }

}
