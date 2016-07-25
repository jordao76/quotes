package io.github.jordao76.quotes.domain;

import org.springframework.data.repository.*;
import org.springframework.stereotype.Repository;

@Repository
public interface QuoteRepository extends CrudRepository<Quote, Long> {
}
