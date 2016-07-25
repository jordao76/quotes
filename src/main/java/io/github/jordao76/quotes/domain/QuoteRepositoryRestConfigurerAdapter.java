package io.github.jordao76.quotes.domain;

import org.springframework.beans.factory.annotation.*;
import org.springframework.data.rest.core.event.*;
import org.springframework.data.rest.webmvc.config.*;
import org.springframework.stereotype.*;
import org.springframework.validation.*;

// from http://stackoverflow.com/a/37563735/31158 Can JSR 303 Bean Validation be used with Spring Data Rest?

@Component
public class QuoteRepositoryRestConfigurerAdapter extends RepositoryRestConfigurerAdapter {

  @Autowired
  private Validator validator;

  @Override
  public void configureValidatingRepositoryEventListener(ValidatingRepositoryEventListener validatingListener) {
    validatingListener.addValidator("beforeCreate", validator);
    validatingListener.addValidator("beforeSave", validator);
  }
}
