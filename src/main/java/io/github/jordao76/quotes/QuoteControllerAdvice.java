package io.github.jordao76.quotes;

import static org.springframework.http.HttpStatus.*;

import org.springframework.core.convert.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@ControllerAdvice
public class QuoteControllerAdvice {

  @ExceptionHandler(ConversionFailedException.class)
  public ResponseEntity<?> conversionFailedHandler(ConversionFailedException ex) {
    return new ResponseEntity<>(Error.forException(ex), BAD_REQUEST);
  }

  public static class Error {
    public String message;
    public static Error forException(Exception ex) {
      Error error = new Error();
      error.message = ex.getLocalizedMessage();
      return error;
    }
  }

}
