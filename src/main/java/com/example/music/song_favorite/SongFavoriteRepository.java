package com.example.music.song_favorite;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SongFavoriteRepository extends MongoRepository<SongFavorite, String> {
}
