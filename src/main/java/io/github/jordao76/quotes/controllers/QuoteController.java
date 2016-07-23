package io.github.jordao76.quotes.controllers;

import static org.springframework.web.bind.annotation.RequestMethod.*;

import javax.validation.*;

import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import io.github.jordao76.quotes.domain.*;

@RestController
@RequestMapping(value = "/quotes")
public class QuoteController {

  private final QuoteRepository repo;

  @Autowired
  public QuoteController(QuoteRepository repo) {
    this.repo = repo;
  }

  @RequestMapping(method = GET)
  public Iterable<Quote> getQuotes() {
    return repo.findAll();
  }

  @RequestMapping(value = "/{id}", method = GET)
  public Quote getQuote(@PathVariable Long id) {
    return repo.findOne(id);
  }

  @RequestMapping(method = PUT)
  public ResponseEntity<Quote> putQuote(@RequestBody @Valid Quote quote) {
    Quote saved = repo.save(quote);
    return ResponseEntity.ok().body(saved);
  }

}
