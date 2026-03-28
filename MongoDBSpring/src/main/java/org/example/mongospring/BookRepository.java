package org.example.mongospring;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface BookRepository extends MongoRepository<Book, String> {
     List<Book> findByGenre(String genre);
}
