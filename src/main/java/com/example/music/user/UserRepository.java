package com.example.music.user;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

    @Query(value = "select user.id as 'id', user.name as 'name', user.avatar as 'avatar', user.account as 'email', user.gender as 'gender', user.birthday as 'birthday', " +
            "user.create_date as 'dateCreate', user.status as 'status' from user join account a on a.login = user.account " +
            "where a.role = ?1 and user.status = 'Activate' order by user.create_date desc limit 3")
    List<UserResponse> getNewUserOrArtis(String role);

}
