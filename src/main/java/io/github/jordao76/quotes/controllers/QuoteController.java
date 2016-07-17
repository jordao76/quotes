package io.github.jordao76.quotes.controllers;

import static org.springframework.web.bind.annotation.RequestMethod.*;

import org.springframework.web.bind.annotation.*;

import io.github.jordao76.quotes.domain.*;

@RestController
public class QuoteController {

  @RequestMapping(value = "/quote", method = GET)
  public Quote getRandomQuote() {
    return new Quote("It's OK to figure out murder mysteries, but you shouldn't need to figure out code. You should be able to read it.", "Steve McConnell");
  }

}
