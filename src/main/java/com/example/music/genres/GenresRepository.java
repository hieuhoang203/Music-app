package com.example.music.genres;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GenresRepository extends MongoRepository<Genres, String> {
}
