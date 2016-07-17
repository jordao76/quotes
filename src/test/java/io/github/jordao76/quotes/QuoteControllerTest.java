package io.github.jordao76.quotes;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.*;
import org.junit.runner.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.*;
import org.springframework.test.context.junit4.*;
import org.springframework.test.context.web.*;
import org.springframework.test.web.servlet.*;
import org.springframework.test.web.servlet.setup.*;
import org.springframework.web.context.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = QuoteApplication.class)
@WebAppConfiguration
public class QuoteControllerTest {

  @Autowired
  private WebApplicationContext wac;

  private MockMvc client;

  @Before
  public void setup() {
    client = MockMvcBuilders.webAppContextSetup(wac).build();
  }

  @Test
  public void getQuotes() throws Exception {
    client
      .perform(get("/quotes"))
      .andExpect(status().isOk())
      .andExpect(content().contentType("application/json;charset=UTF-8"));
  }

}
