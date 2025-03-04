package com.example.music.favorite;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FavoriteRepository extends MongoRepository<Favorite, String> {
}
