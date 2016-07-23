package io.github.jordao76.quotes;

import static io.github.jordao76.quotes.support.QuoteMatchers.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;

import org.junit.*;
import org.junit.runner.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.*;
import org.springframework.test.context.junit4.*;
import org.springframework.test.context.web.*;
import org.springframework.test.web.servlet.*;
import org.springframework.web.context.*;

import io.github.jordao76.quotes.domain.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = QuoteApplication.class)
@WebAppConfiguration
public class QuoteControllerTest {

  @Autowired
  private WebApplicationContext wac;

  private MockMvc client;

  @Before
  public void setup() {
    client = webAppContextSetup(wac).build();
  }

  // first quote added to the repository (@see QuoteApplication.populateRepo),
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
  public void putQuote() throws Exception {
    String quoteText = "Quick decisions are unsafe decisions.",
      quoteAuthor = "Sophocles";
    Quote quote = new Quote(quoteText, quoteAuthor);
    client
      .perform(put("/quotes")
        .contentType(APPLICATION_JSON)
        .content(serializeJson(quote)))
      .andExpect(status().isOk())
      .andExpect(content().contentType(APPLICATION_JSON_UTF8))
      .andExpect(contentAsQuote(matching(quoteText, quoteAuthor)));
  }

}
