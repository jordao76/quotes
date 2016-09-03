package io.github.jordao76.quotes.web;

import java.util.*;
import java.util.stream.*;

import org.springframework.beans.factory.annotation.*;
import org.springframework.context.*;
import org.springframework.context.i18n.*;
import org.springframework.http.*;
import org.springframework.web.*;
import org.springframework.web.bind.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.*;
import org.springframework.web.servlet.mvc.method.annotation.*;

@ControllerAdvice
public class QuoteExceptionHandler extends ResponseEntityExceptionHandler {

  @Autowired
  private MessageSource messageSource;

  @Override
  protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex,
      HttpHeaders headers, HttpStatus status, WebRequest request) {
    Problem problem = new Problem(status, getErrors(ex));
    return handleExceptionInternal(ex, problem, headers, status, request);
  }

  private List<String> getErrors(HttpMediaTypeNotSupportedException ex) {
    return Arrays.asList(ex.getLocalizedMessage());
  }

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers,
      HttpStatus status, WebRequest request) {
    Problem problem = new Problem(status, getErrors(ex));
    return handleExceptionInternal(ex, problem, headers, status, request);
  }

  private List<String> getErrors(MethodArgumentNotValidException ex) {
    Locale locale = LocaleContextHolder.getLocale();
    return ex
      .getBindingResult()
      .getAllErrors()
      .stream()
      .map(e -> messageSource.getMessage(e, locale))
      .collect(Collectors.toList());
  }
}