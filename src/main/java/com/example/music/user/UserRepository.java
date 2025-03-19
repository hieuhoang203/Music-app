package com.example.music.user;

import com.example.music.auth.DetailAccount;
import com.example.music.common.ComboboxValue;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    @Transactional
    @Query(value = "select * from tbl_user where status = :status order by create_date desc", nativeQuery = true)
    List<User> selectUser(@Param("status") String status);

    @Query(value = "select * from tbl_user where status = :status order by create_date desc", nativeQuery = true)
    Page<User> getUser(@Param("status") String status, Pageable pageable);

    @Query(value = "select tbl_user.id as 'id', tbl_user.name as 'name', tbl_user.avatar as 'avatar', tbl_user.account as 'email', tbl_user.gender as 'gender', tbl_user.birthday as 'birthday', " +
            "tbl_user.create_date as 'dateCreate', tbl_user.status as 'status' from tbl_user join tbl_account a on a.login = tbl_user.account " +
            "where a.role = :role and tbl_user.status = 'Activate' order by tbl_user.create_date desc limit 3", nativeQuery = true)
    List<UserResponse> getNewUserOrArtis(@Param("role") String role);

    @Transactional
    @Query(value = "select tbl_user.id as 'id', tbl_user.name as 'name', tbl_user.avatar as 'avatar', tbl_user.account as 'email', tbl_user.gender as 'gender', tbl_user.birthday as 'birthday', tbl_user.create_date as 'dateCreate', tbl_user.status as 'status' from tbl_user join tbl_account a on a.login = tbl_user.account where a.role = 'USER' or a.role = 'ADMIN' order by tbl_user.create_date desc", nativeQuery = true)
    Page<UserResponse> getAllUser(Pageable pageable);

    @Query(value = "select tbl_user.id as 'id', tbl_user.name as 'name', tbl_user.avatar as 'avatar', tbl_user.gender as 'gender', tbl_user.account as 'email', tbl_user.birthday as 'birthday', tbl_user.create_date as 'dateCreate', tbl_user.status as 'status' from tbl_user join tbl_account a on a.login = tbl_user.account where a.role = 'USER' or a.role = 'ADMIN' and tbl_user.status = :status order by tbl_user.create_date desc", nativeQuery = true)
    Page<UserResponse> getUserByStatus(@Param("status") String status, Pageable pageable);

    @Query(value = "select tbl_user.id as 'id', tbl_user.name as 'name', tbl_user.avatar as 'avatar', tbl_user.gender as 'gender'\n" +
            ", tbl_user.birthday as 'birthday', COALESCE(count(o.work), 0) as 'songs', COALESCE(count(f.tbl_user), 0) as 'follows', tbl_user.status as 'status' from tbl_user\n" +
            "left join follow f on tbl_user.id = f.idol\n" +
            "left join own o on tbl_user.id = o.author\n" +
            "join tbl_account a on a.login = tbl_user.account\n" +
            "where a.role = 'ARTIS'\n" +
            "group by tbl_user.id order by tbl_user.create_date desc ", nativeQuery = true)
    Page<ArtisResponse> getAllArtis(Pageable pageable);

    @Query(value = "select tbl_user.id as 'id', tbl_user.name as 'name', tbl_user.avatar as 'avatar', tbl_user.gender as 'gender'\n" +
            ", tbl_user.birthday as 'birthday', COALESCE(count(o.work), 0) as 'songs', COALESCE(count(f.tbl_user), 0) as 'follows', tbl_user.status as 'status' from tbl_user\n" +
            "left join follow f on tbl_user.id = f.idol\n" +
            "left join own o on tbl_user.id = o.author\n" +
            "join tbl_account a on a.login = tbl_user.account\n" +
            "where a.role = 'ARTIS' and tbl_user.status = :status\n" +
            "group by tbl_user.id order by tbl_user.create_date desc ", nativeQuery = true)
    Page<ArtisResponse> getArtisByStatus(@Param("status") String status, Pageable pageable);

    @Transactional
    @Modifying
    @Query(value = "update tbl_user set status = :stauts where id = :id", nativeQuery = true)
    void updateStatusUser(@Param("id") String id, @Param("status") String status);

    @Transactional
    @Query(value = "select tbl_user.id as 'value', tbl_user.name as 'label' from tbl_user\n" +
            "where a.role = 'ARTIS' and tbl_user.status = 'Activate'", nativeQuery = true)
    List<ComboboxValue> getArtisForSelect();

    @Transactional
    @Query(value = "select tbl_user.account from tbl_user", nativeQuery = true)
    List<String> getEmailUser();

    @Transactional
    @Query(value = "select u.id, u.name, u.gender, u.birthday, u.avatar, u.role from tbl_user u\n" +
            "where u.login = :login", nativeQuery = true)
    DetailAccount getUserResponse(@Param("login") String login);

    @Query(value = "select 1 from tbl_user WHERE account = :email LIMIT 1 ", nativeQuery = true)
    Integer getAllEmail(@Param("email") String email);

    @Query(value = "select * from tbl_user where login = :email and status = 'Activate'", nativeQuery = true)
    User getUserByEmail(@Param("email") String email);

}
