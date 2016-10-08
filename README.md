# Quotes

[![Build Status](https://travis-ci.org/jordao76/quotes.svg)](https://travis-ci.org/jordao76/quotes)
[![License](http://img.shields.io/:license-mit-blue.svg)](https://github.com/jordao76/quotes/blob/master/LICENSE)

> “Quotation, n: The act of repeating erroneously the words of another.”<br/>— _Ambrose Bierce_

Simple spring-boot service for citations.

Run with maven:

```sh
$ mvn spring-boot:run
```

Or package and run (change version as appropriate):

```sh
$ mvn package
...
$ java -jar target/quotes-0.0.1-SNAPSHOT.jar
```

## REST API

Using `http://localhost:8080` as the base URL:

`/quotes/any` returns a random quote in JSON (using [jq](https://stedolan.github.io/jq/)):

```sh
$ curl -s localhost:8080/quotes/any | jq .
```
```json
{
  "id": 46,
  "text": "There is no silver bullet.",
  "author": "Frederick P. Brooks"
}
```

`/quotes` returns all quotes (paged) and `/quotes/{id}` returns the quote with ID `{id}`.

`/quotes?author={author}` returns all quotes (paged) by author `{author}`, e.g. `/quotes?author=Martin+Fowler`.

Paging is done with parameters `page` (zero-based, default `0`) for the page of results and `size` (default `20`) for the number of quotes per page, e.g. `/quotes?page=3&size=10`.

To create and delete quotes, HTTP basic authentication is required, with a user with the role `MAINTAINER` (the default user `admin:password` has all the relevant roles).

To create a quote issue a POST request to `/quotes`:

```sh
$ curl -i -u admin:password -H "Content-Type: application/json" \
  -d '{"text":"Quick decisions are unsafe decisions","author":"Sophocles"}' \
  -X POST localhost:8080/quotes
```

To delete quote with ID `{id}` issue a DELETE request to `/quotes/{id}`:

```sh
$ curl -i -u admin:password -X DELETE localhost:8080/quotes/23
```

The [actuator](http://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#production-ready) endpoints are exposed through `/manage`, and also require authentication, with a user with the role `ADMIN`.

## UI

A simple UI to get random quotes is available through the submodule [quotes-ui](https://github.com/jordao76/quotes-ui) (don't forget to also [clone submodules](http://stackoverflow.com/questions/3796927/how-to-git-clone-including-submodules)). `quotes-ui` is a `node.js` sub-project built with the [frontend-maven-plugin](https://github.com/eirslett/frontend-maven-plugin). The UI can be built by activating the maven profile `ui`, e.g.:

```sh
$ mvn spring-boot:run -Pui
```

Then go to `http://localhost:8080` to get random quotes.

A sample with hard-coded quotes can be found [here](https://jordao76.github.io/quotes-ui/).
