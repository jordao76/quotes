package io.github.jordao76.quotes.filters;

import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;

import org.slf4j.*;
import org.springframework.stereotype.*;
import org.springframework.web.filter.*;

@Component
public class RequestLoggingFilter extends OncePerRequestFilter {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    filterChain.doFilter(request, response);

    if (logger.isDebugEnabled()) {
      logger.debug("HTTP {} for {} {}",
        value("status", response.getStatus()),
        value("method", request.getMethod()),
        value("endpoint", request.getRequestURI()));
    }

  }

  // note: later to be replaced,
  // see https://github.com/logstash/logstash-logback-encoder/tree/logstash-logback-encoder-4.7#loggingevent_custom_event
  private Object value(String key, Object value) {
    return value;
  }

}
