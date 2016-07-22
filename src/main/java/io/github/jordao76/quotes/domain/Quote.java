package io.github.jordao76.quotes.domain;

import javax.persistence.*;

@Entity
public class Quote {

  @Id
  @GeneratedValue
  private Long id;

  @Column(nullable = false)
  private String text;

  @Column(nullable = false)
  private String author;

  @SuppressWarnings("unused")
  private Quote() {}

  public Quote(String text, String author) {
    this.text = text;
    this.author = author;
  }

  public String getText() { return text; }
  public String getAuthor() { return author; }

  @Override
  public String toString() {
    return String.format("Quote with text=[%s], author=[%s]", text, author);
  }

}
