package com.example.music.album;

import com.example.music.song.Song;
import com.example.music.user.User;
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

@Document(collection = "tbl_album")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Album {

    @Id
    @Field(name = "id")
    private String id;

    @Field(name = "name")
    private String name;

    @Field(name = "avatar")
    private String avatar;

    @Field(name = "artis")
    @JsonIgnore
    private User artis;

    @Field(name = "release_date")
    private Date release_date;

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

    @Field(name = "songs")
    private Set<Song> songs;

}
