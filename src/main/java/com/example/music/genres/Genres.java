package com.example.music.genres;

import com.example.music.song_genres.SongGenres;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;
import java.util.Set;

@Document(collection = "tbl_genres")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Genres {

    @Id
    @Field(name = "id")
    private String id;

    @Field(name = "code")
    private String code;

    @Field(name = "name")
    private String name;

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

    @Field(name = "genres")
    @JsonIgnore
    private Set<SongGenres> songGenres;

}
