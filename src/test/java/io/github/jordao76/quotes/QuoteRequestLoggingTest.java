package io.github.jordao76.quotes;

import static io.github.jordao76.quotes.support.QuoteMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;

import org.junit.*;
import org.junit.runner.*;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.*;
import org.springframework.test.context.junit4.*;
import org.springframework.test.web.servlet.*;
import org.springframework.web.context.*;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.*;
import ch.qos.logback.core.*;
import io.github.jordao76.quotes.web.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { QuoteApplication.class })
public class QuoteRequestLoggingTest {

  @Autowired
  private WebApplicationContext wac;

  private MockMvc client;
  private Appender<ILoggingEvent> appender;

  @Before
  public void setup() {
    client = webAppContextSetup(wac)
      .addFilters(new RequestLoggingFilter())
      .build();
    setupAppender();
  }

  @SuppressWarnings("unchecked")
  private void setupAppender() {
    appender = mock(Appender.class);
    Logger root = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
    root.addAppender(appender);
  }

  @Test
  public void getFirstQuote() throws Exception {
    client.perform(get("/quotes/1"));
    assertLogged("HTTP 200 for GET /quotes/1");
  }

  private void assertLogged(String message) {
    verify(appender).doAppend(logged(message));
  }

}
