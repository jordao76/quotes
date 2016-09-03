package io.github.jordao76.quotes.web;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.*;

import java.util.*;

import org.springframework.http.*;

import com.fasterxml.jackson.annotation.*;

// based on https://tools.ietf.org/html/rfc7807
@JsonInclude(NON_NULL)
public class Problem {

  private String type;
  private String title;
  private HttpStatus httpStatus;
  private List<String> errors;

  public Problem(HttpStatus httpStatus) {
    this(null, httpStatus.getReasonPhrase(), httpStatus, null);
  }

  public Problem(HttpStatus httpStatus, List<String> errors) {
    this(null, httpStatus.getReasonPhrase(), httpStatus, errors);
  }

  public Problem(String title, HttpStatus httpStatus, List<String> errors) {
    this(null, title, httpStatus, errors);
  }

  public Problem(String type, String title, HttpStatus httpStatus, List<String> errors) {
    this.type = type;
    this.title = title;
    this.httpStatus = httpStatus;
    this.errors = errors;
  }

  public String getType() { return type; }
  public String getTitle() { return title; }
  public int getStatus() { return httpStatus.value(); }
  public List<String> getErrors() { return errors; }

  public ResponseEntity<Problem> asResponse() {
    return new ResponseEntity<>(this, httpStatus);
  }

}