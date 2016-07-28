package io.github.jordao76.quotes.controllers;

import static org.springframework.web.bind.annotation.RequestMethod.*;

import java.util.*;

import org.springframework.beans.factory.annotation.*;
import org.springframework.data.domain.*;
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

  @RequestMapping(value = "/any", method = GET)
  public Quote getRandomQuote() {
    int total = (int)repo.count();
    int which = new Random().nextInt(total);
    return repo.findAll(new PageRequest(which, 1)).getContent().get(0);
  }

}
