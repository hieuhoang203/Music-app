package com.example.music.album;

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
public interface AlbumRepository extends JpaRepository<Album, String> {

    @Modifying
    @Transactional
    @Query(value = "update tbl_album set status = 'ShutDown' where id = ?1", nativeQuery = true)
    void updateStatus(@Param("id") String id);

    @Transactional
    @Query(value = "select * from tbl_album where status = ?1 order by create_date desc, id desc", nativeQuery = true)
    List<Album> select(@Param("status") String status);

    @Transactional
    @Query(value = "select * from tbl_album where status = ?1", nativeQuery = true)
    Page<Album> getAlbum(@Param("status") String status, Pageable pageable);

    @Transactional
    @Query(value = "select id as 'value', name as 'label' from tbl_album where status = 'Activate'", nativeQuery = true)
    List<ComboboxValue> getAlbumForSelect();

    @Transactional
    @Query(value = "select id as 'value', name as 'label' from tbl_album where status = 'Activate' and artis = :artis", nativeQuery = true)
    List<ComboboxValue> getAlbumForSelect(@Param("artis") String artis);

}
