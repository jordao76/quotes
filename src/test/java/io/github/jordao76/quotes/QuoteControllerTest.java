package io.github.jordao76.quotes;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;

import org.hamcrest.*;
import org.junit.*;
import org.junit.runner.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.*;
import org.springframework.test.context.junit4.*;
import org.springframework.test.context.web.*;
import org.springframework.test.web.servlet.*;
import org.springframework.web.context.*;

import com.fasterxml.jackson.databind.*;

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

  @Test
  public void getQuotes() throws Exception {
    client
      .perform(get("/quotes"))
      .andExpect(status().isOk())
      .andExpect(content().contentType("application/json;charset=UTF-8"))
      .andExpect(content().string(containsString("Any sufficiently advanced technology is indistinguishable from magic.")))
      .andExpect(fromJsonTo(Quote[].class, is(arrayWithSize(3))));
  }

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
