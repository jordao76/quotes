package io.github.jordao76.quotes.support;

import static org.junit.Assert.*;

import org.hamcrest.*;
import org.springframework.test.web.servlet.*;

import com.fasterxml.jackson.databind.*;

public class ResultMatchers {

  public static <T> ResultMatcher fromJsonTo(Class<T> type, Matcher<? super T> matcher) {
    return result -> assertThat(deserializeJson(result.getResponse().getContentAsString(), type), matcher);
  }

  public static String serializeJson(Object obj) {
    try {
      ObjectMapper mapper = new ObjectMapper();
      return mapper.writeValueAsString(obj);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public static <T> T deserializeJson(String json, Class<T> type) {
    try {
      ObjectMapper mapper = new ObjectMapper();
      return mapper.readValue(json, type);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

}
