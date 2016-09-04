package io.github.jordao76.quotes.liquibase;

import java.util.*;

import org.springframework.core.io.*;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.dataformat.yaml.*;

import io.github.jordao76.quotes.domain.*;
import liquibase.change.custom.*;
import liquibase.database.*;
import liquibase.exception.*;
import liquibase.resource.*;
import liquibase.statement.*;
import liquibase.statement.core.*;

public class InsertQuotesFromYaml implements CustomSqlChange {

  private String fileName;

  @Override
  public SqlStatement[] generateStatements(Database database) throws CustomChangeException {
    return readQuotes()
      .stream()
      .map(this::insertQuote)
      .toArray(size -> new SqlStatement[size]);
  }

  private List<Quote> readQuotes() {
    try {
      ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
      return Arrays.asList(
        mapper.readValue(
          new ClassPathResource("db/changelog/" + fileName).getInputStream(),
          Quote[].class));
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private InsertStatement insertQuote(Quote quote) {
    return new InsertStatement(null, null, "quote")
      .addColumnValue("author", quote.getAuthor())
      .addColumnValue("text", quote.getText());
  }

  @Override
  public String getConfirmationMessage() {
    return "Initial quotes inserted";
  }

  @Override
  public void setFileOpener(ResourceAccessor resourceAccessor) {
  }

  @Override
  public void setUp() throws SetupException {
  }

  @Override
  public ValidationErrors validate(Database database) {
    return new ValidationErrors();
  }

  public String getFileName() {
    return fileName;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

}
