package com.example.music.account;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, String> {

    @Transactional
    @Modifying
    @Query(value = "update tbl_account set status = 'ShutDown' where login = :login", nativeQuery = true)
    void updateStatus(@Param("login") String userName);

    @Transactional
    @Query(value = "select * from tbl_account where status = :status order by create_date desc, id desc", nativeQuery = true)
    List<Account> selectAllAccount(@Param("status") String status);

    @Transactional
    @Modifying
    @Query(value = "update tbl_account set status = :status  where login = :login", nativeQuery = true)
    void updateStatusAccount(@Param("login") String id, @Param("status") String status);

    @Transactional
    @Query(value = "select * from tbl_account where login = :login and status = 'Activate'", nativeQuery = true)
    Optional<Account> loginAccount(@Param("login") String user_name);

    @Transactional
    @Modifying
    @Query(value = "update tbl_account set pass = :password where login = :login", nativeQuery = true)
    void updatePassWord(@Param("login") String user_name, @Param("password") String pass_word);

    Optional<Account> findAccountByLogin(String user_name);

}
