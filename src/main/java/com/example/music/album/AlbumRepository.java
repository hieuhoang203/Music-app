package com.example.music.album;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlbumRepository extends MongoRepository<Album, String> {

}
