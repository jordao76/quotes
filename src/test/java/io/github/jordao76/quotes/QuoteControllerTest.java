package io.github.jordao76.quotes;

import static io.github.jordao76.quotes.support.QuoteMatchers.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;

import org.junit.*;
import org.junit.runner.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.*;
import org.springframework.security.test.context.support.*;
import org.springframework.test.context.junit4.*;
import org.springframework.test.web.servlet.*;
import org.springframework.web.context.*;

import io.github.jordao76.quotes.domain.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class QuoteControllerTest {

  @Autowired
  private WebApplicationContext wac;

  private MockMvc client;

  @Before
  public void setup() {
    client = webAppContextSetup(wac)
      .apply(springSecurity())
      .build();
  }

  // first quote added to the repository, should have ID = 1
  String firstQuoteText = "Any sufficiently advanced technology is indistinguishable from magic.";
  String firstQuoteAuthor = "Arthur C. Clarke";

  @Test
  public void getQuotes_checkFirstQuote() throws Exception {
    client
      .perform(get("/quotes"))
      .andExpect(status().isOk())
      .andExpect(content().contentType(APPLICATION_JSON_UTF8))
      .andExpect(contentAsQuotes(hasQuote(matching(firstQuoteText, firstQuoteAuthor))));
  }

  @Test
  public void getQuotes_pagingSize() throws Exception {
    client
      .perform(get("/quotes?size=7")) // first page is page=0
      .andExpect(status().isOk())
      .andExpect(content().contentType(APPLICATION_JSON_UTF8))
      .andExpect(jsonPath("$", hasSize(7)))
      .andExpect(contentAsQuotes(not(hasQuote(withAuthor("Erich Gamma"))))); // on second page
  }

  @Test
  public void getQuotes_paging() throws Exception {
    client
      .perform(get("/quotes?size=7&page=1")) // pages are 0-based
      .andExpect(status().isOk())
      .andExpect(content().contentType(APPLICATION_JSON_UTF8))
      .andExpect(jsonPath("$", hasSize(7)))
      .andExpect(contentAsQuotes(hasQuote(withAuthor("Erich Gamma"))));
  }

  @Test
  public void getQuotes_byAuthor() throws Exception {
    client
      .perform(get("/quotes?author=Martin Fowler"))
      .andExpect(status().isOk())
      .andExpect(content().contentType(APPLICATION_JSON_UTF8))
      .andExpect(contentAsQuotes(everyQuote(hasAuthor("Martin Fowler"))));
  }

  @Test
  public void getFirstQuote() throws Exception {
    client
      .perform(get("/quotes/1"))
      .andExpect(status().isOk())
      .andExpect(content().contentType(APPLICATION_JSON_UTF8))
      .andExpect(contentAsQuote(matching(firstQuoteText, firstQuoteAuthor)));
  }

  @Test
  public void getRandomQuote_checkResultNotNull() throws Exception {
    client
      .perform(get("/quotes/any"))
      .andExpect(status().isOk())
      .andExpect(content().contentType(APPLICATION_JSON_UTF8))
      .andExpect(contentAsQuote(not(nullValue())));
  }

  @Test
  public void getQuote_notFound() throws Exception {
    client
      .perform(get("/quotes/99999"))
      .andExpect(status().isNotFound())
      .andExpect(content().contentType(APPLICATION_JSON_UTF8))
      .andExpect(jsonPath("$.status", is(404)))
      .andExpect(jsonPath("$.title", is("Not Found")));
  }

  @Test
  public void getQuote_invalidId() throws Exception {
    client
      .perform(get("/quotes/blah"))
      .andExpect(status().isBadRequest());
  }

  @Test
  @WithMockUser(roles="MAINTAINER")
  public void postQuote() throws Exception {
    String quoteText = "Quick decisions are unsafe decisions.",
      quoteAuthor = "Sophocles";
    Quote quote = new Quote(quoteText, quoteAuthor);
    MvcResult result = client
      .perform(post("/quotes")
        .contentType(APPLICATION_JSON)
        .content(serializeJson(quote)))
      .andExpect(status().isCreated())
      .andExpect(header().string("Location", both(startsWith("http")).and(containsString("/quotes/"))))
      .andExpect(content().contentType(APPLICATION_JSON_UTF8))
      .andExpect(contentAsQuote(matching(quoteText, quoteAuthor)))
      .andReturn();
    String location = result.getResponse().getHeader("Location");
    client
      .perform(get(location))
      .andExpect(status().isOk())
      .andExpect(content().contentType(APPLICATION_JSON_UTF8))
      .andExpect(contentAsQuote(matching(quoteText, quoteAuthor)));
  }

  @Test
  public void postQuote_unauthenticated() throws Exception {
    String quoteText = "Quick decisions are unsafe decisions.",
      quoteAuthor = "Sophocles";
    Quote quote = new Quote(quoteText, quoteAuthor);
    client
      .perform(post("/quotes")
        .contentType(APPLICATION_JSON)
        .content(serializeJson(quote)))
      .andExpect(status().isUnauthorized());
  }

  @Test
  @WithMockUser(roles="ADMIN")
  public void postQuote_wrong_role() throws Exception {
    String quoteText = "Quick decisions are unsafe decisions.",
      quoteAuthor = "Sophocles";
    Quote quote = new Quote(quoteText, quoteAuthor);
    client
      .perform(post("/quotes")
        .contentType(APPLICATION_JSON)
        .content(serializeJson(quote)))
      .andExpect(status().isForbidden());
  }

  @Test
  @WithMockUser(roles="MAINTAINER")
  public void postQuote_invalidText() throws Exception {
    String quoteText = null,
      quoteAuthor = "Sophocles";
    Quote quote = new Quote(quoteText, quoteAuthor);
    client
      .perform(post("/quotes")
        .contentType(APPLICATION_JSON)
        .content(serializeJson(quote)))
      .andExpect(status().isBadRequest())
      .andExpect(content().contentType(APPLICATION_JSON_UTF8))
      .andExpect(jsonPath("$.errors", hasSize(1)))
      .andExpect(jsonPath("$.errors[0]", is("Quote text must not be null")));
  }

  @Test
  @WithMockUser(roles="MAINTAINER")
  public void postQuote_wrongJson() throws Exception {
    client
      .perform(post("/quotes")
        .contentType(APPLICATION_JSON)
        .content("{\"citation\":\"to be or not to be\"}"))
      .andExpect(status().isBadRequest());
  }

  @Test
  @WithMockUser(roles="MAINTAINER")
  public void postQuote_notJson() throws Exception {
    client
      .perform(post("/quotes")
        .contentType(APPLICATION_XML)
        .content("<root>not JSON</root>"))
      .andExpect(status().isUnsupportedMediaType())
      .andExpect(content().contentType(APPLICATION_JSON_UTF8))
      .andExpect(jsonPath("$.status", is(415)))
      .andExpect(jsonPath("$.title", is("Unsupported Media Type")));
  }

  @Test
  @WithMockUser(roles="MAINTAINER")
  public void deleteSecondQuote() throws Exception {
    client
      .perform(get("/quotes/2"))
      .andExpect(status().isOk());
    client
      .perform(delete("/quotes/2"))
      .andExpect(status().isNoContent());
    client
      .perform(get("/quotes/2"))
      .andExpect(status().isNotFound());
  }

  @Test
  public void delete_unauthenticated() throws Exception {
    client
      .perform(delete("/quotes/2"))
      .andExpect(status().isUnauthorized());
  }

  @Test
  @WithMockUser(roles="ADMIN")
  public void delete_wrong_role() throws Exception {
    client
      .perform(delete("/quotes/2"))
      .andExpect(status().isForbidden());
  }

  @Test
  @WithMockUser(roles="MAINTAINER")
  public void deleteQuote_notFound() throws Exception {
    client
      .perform(delete("/quotes/99999"))
      .andExpect(status().isNotFound())
      .andExpect(content().contentType(APPLICATION_JSON_UTF8))
      .andExpect(jsonPath("$.status", is(404)))
      .andExpect(jsonPath("$.title", is("Not Found")));
  }

  @Test
  @WithMockUser(roles="MAINTAINER")
  public void deleteQuote_invalidId() throws Exception {
    client
      .perform(delete("/quotes/jazzy"))
      .andExpect(status().isBadRequest());
  }

}
