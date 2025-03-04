package com.example.music.own;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OwnRepository extends MongoRepository<Own, String> {
}
