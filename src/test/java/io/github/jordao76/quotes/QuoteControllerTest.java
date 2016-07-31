package io.github.jordao76.quotes;

import static io.github.jordao76.quotes.support.QuoteMatchers.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;

import org.junit.*;
import org.junit.runner.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.*;
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
    client = webAppContextSetup(wac).build();
  }

  // first quote added to the repository (@see QuoteRepositoryInitializer),
  // should have ID = 1
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
      .perform(get("/quotes/42"))
      .andExpect(status().isNotFound());
  }

  @Test
  public void getQuote_invalidId() throws Exception {
    client
      .perform(get("/quotes/blah"))
      .andExpect(status().isBadRequest());
  }

  @Test
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
  public void postQuote_invalidText() throws Exception {
    String quoteText = null,
      quoteAuthor = "Sophocles";
    Quote quote = new Quote(quoteText, quoteAuthor);
    client
      .perform(post("/quotes")
        .contentType(APPLICATION_JSON)
        .content(serializeJson(quote)))
      .andExpect(status().isBadRequest());
  }

  @Test
  public void postQuote_wrongJson() throws Exception {
    client
      .perform(post("/quotes")
        .contentType(APPLICATION_JSON)
        .content("{\"citation\":\"to be or not to be\"}"))
      .andExpect(status().isBadRequest());
  }

  @Test
  public void postQuote_notJson() throws Exception {
    client
      .perform(post("/quotes")
        .contentType(APPLICATION_XML)
        .content("<root>not JSON</root>"))
      .andExpect(status().isUnsupportedMediaType());
  }

  @Test
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
  public void deleteQuote_notFound() throws Exception {
    client
      .perform(delete("/quotes/99999"))
      .andExpect(status().isNotFound());
  }

  @Test
  public void deleteQuote_invalidId() throws Exception {
    client
      .perform(delete("/quotes/jazzy"))
      .andExpect(status().isBadRequest());
  }

}
