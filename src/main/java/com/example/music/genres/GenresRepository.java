package com.example.music.genres;

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
public interface GenresRepository extends JpaRepository<Genres, String> {

    @Query(value = "SELECT 1 FROM tbl_genres WHERE LOWER(:column) = LOWER(:value) LIMIT 1", nativeQuery = true)
    Integer checkCodeOrNameExists(@Param("column") String column, @Param("value") String value);

    @Modifying
    @Transactional
    @Query(value = "update tbl_genres set status = ?2 where id = ?1", nativeQuery = true)
    void changeStatus(String id, String status);

    @Query(value = "select * from tbl_genres where status = ?1 order by create_date desc, id desc", nativeQuery = true)
    Page<Genres> getGenres(String status, Pageable pageable);

    @Query(value = "select * from tbl_genres order by create_date desc, id desc", nativeQuery = true)
    Page<Genres> getAll(Pageable pageable);

    @Query(value = "select id as 'value', name as 'label' from tbl_genres where status = 'Activate'", nativeQuery = true)
    List<ComboboxValue> getGenresForSelect();

}
