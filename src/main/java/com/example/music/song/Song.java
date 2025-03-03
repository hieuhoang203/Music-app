package com.example.music.song;

import com.example.music.album.Album;
import com.example.music.own.Own;
import com.example.music.song_favorite.SongFavorite;
import com.example.music.song_genres.SongGenres;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;
import java.util.Set;

@Document(collection = "tbl_song")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Song {

    @Id
    @Field(name = "id")
    private String id;

    @Field(name = "name")
    private String name;

    @Field(name = "avatar")
    private String avatar;

    @Field(name = "album")
    private Album album;

    @Field(name = "work")
    @JsonIgnore
    private Set<Own> owns;

    @Field(name = "url")
    private String url;

    @Field(name = "duration")
    private Short duration;

    @Field(name = "view")
    private Integer view;

    @Field(name = "genres")
    @JsonIgnore
    private Set<SongGenres> songGenres;

    @Field(name = "create_date")
    private Date create_date;

    @Field(name = "create_by")
    private String create_by;

    @Field(name = "update_date")
    private Date update_date;

    @Field(name = "update_by")
    private String update_by;

    @Field(name = "status")
    private String status;

    @Field(name = "favorites")
    @JsonIgnore
    private Set<SongFavorite> songFavorites;

}
