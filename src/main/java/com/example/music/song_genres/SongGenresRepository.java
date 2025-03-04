package com.example.music.song_genres;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SongGenresRepository extends MongoRepository<SongGenres, String> {
}
