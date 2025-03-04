package com.example.music.account;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends MongoRepository<Account, String> {

    boolean existsByLogin(String login);

    @Query(value = "{ 'login': ?0 }")
    Optional<Account> findAccountByLogin(String login);

}
