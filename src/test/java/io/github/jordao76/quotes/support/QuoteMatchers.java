package io.github.jordao76.quotes.support;

import static org.junit.Assert.*;

import java.util.stream.*;

import org.hamcrest.*;
import org.springframework.test.web.servlet.*;

import com.fasterxml.jackson.databind.*;

import io.github.jordao76.quotes.domain.*;

public class QuoteMatchers {

  public static ResultMatcher contentAsQuote(Matcher<Quote> matcher) {
    return contentAs(Quote.class, matcher);
  }

  public static ResultMatcher contentAsQuotes(Matcher<Quote[]> matcher) {
    return contentAs(Quote[].class, matcher);
  }

  public static Matcher<Quote> matching(String text, String author) {
    return new TypeSafeMatcher<Quote>() {
      @Override
      public void describeTo(Description description) {
        description.appendText(
          String.format("Quote with text=[%s], author=[%s]", text, author));
      }
      @Override
      protected boolean matchesSafely(Quote quote) {
        return
          quote != null &&
            text.equals(quote.getText()) && author.equals(quote.getAuthor());
      }
    };
  }

  public static Matcher<Quote[]> hasQuote(Matcher<Quote> matcher) {
    return new TypeSafeMatcher<Quote[]>() {
      @Override
      public void describeTo(Description description) {
        description.appendText("has ");
        description.appendDescriptionOf(matcher);
      }
      @Override
      protected boolean matchesSafely(Quote[] quotes) {
        return Stream.of(quotes).anyMatch(quote -> matcher.matches(quote));
      }
    };
  }

  public static <T> ResultMatcher contentAs(Class<T> type, Matcher<? super T> matcher) {
    return result ->
      assertThat(deserializeJson(result.getResponse().getContentAsString(), type), matcher);
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
