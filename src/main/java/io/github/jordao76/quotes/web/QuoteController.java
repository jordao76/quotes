package io.github.jordao76.quotes.web;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.web.bind.annotation.RequestMethod.*;

import java.net.*;
import java.util.*;

import javax.validation.*;

import org.springframework.beans.factory.annotation.*;
import org.springframework.data.domain.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.*;

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
  public Iterable<Quote> getQuotes(
      @RequestParam(value = "page", required = false, defaultValue = "0") int page,
      @RequestParam(value = "size", required = false, defaultValue = "20") int size,
      @RequestParam(value = "author", required = false) String author) {
    PageRequest pageRequest = new PageRequest(page, size);
    if (author != null) {
      return repo.findByAuthor(pageRequest, author).getContent();
    }
    else {
      return repo.findAll(pageRequest).getContent();
    }
  }

  @RequestMapping(value = "/any", method = GET)
  public Quote getRandomQuote() {
    int total = (int)repo.count();
    int which = new Random().nextInt(total);
    return repo.findAll(new PageRequest(which, 1)).getContent().get(0);
  }

  @RequestMapping(value = "/{id}", method = GET)
  public ResponseEntity<?> getQuote(@PathVariable Long id) {
    Quote saved = repo.findOne(id);
    if (saved == null) return new Problem(NOT_FOUND).asResponse();
    return ResponseEntity.ok().body(saved);
  }

  @RequestMapping(method = POST)
  public ResponseEntity<Quote> createQuote(@RequestBody @Valid Quote quote, UriComponentsBuilder uriBuilder) {
    Quote saved = repo.save(quote);
    return ResponseEntity.created(pathFor(saved, uriBuilder)).body(saved);
  }

  @RequestMapping(value = "/{id}", method = DELETE)
  public ResponseEntity<?> deleteQuote(@PathVariable Long id) {
    Quote saved = repo.findOne(id);
    if (saved == null) return new Problem(NOT_FOUND).asResponse();
    repo.delete(id);
    return new ResponseEntity<>(NO_CONTENT);
  }

  public URI pathFor(Quote quote, UriComponentsBuilder uriBuilder) {
    return uriBuilder
      .path("/quotes/{id}")
      .buildAndExpand(quote.getId()).toUri();
  }

}
