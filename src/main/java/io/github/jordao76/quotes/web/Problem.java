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
  private int status;
  private List<String> errors;

  public Problem(HttpStatus httpStatus, List<String> errors) {
    this(null, httpStatus.getReasonPhrase(), httpStatus, errors);
  }

  public Problem(String title, HttpStatus httpStatus, List<String> errors) {
    this(null, title, httpStatus, errors);
  }

  public Problem(String type, String title, HttpStatus httpStatus, List<String> errors) {
    this.type = type;
    this.title = title;
    this.status = httpStatus.value();
    this.errors = errors;
  }

  public String getType() { return type; }
  public String getTitle() { return title; }
  public int getStatus() { return status; }
  public List<String> getErrors() { return errors; }

}