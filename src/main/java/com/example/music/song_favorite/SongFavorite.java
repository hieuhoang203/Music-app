package com.example.music.song_favorite;

import com.example.music.favorite.Favorite;
import com.example.music.song.Song;
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

@Document(collection = "tbl_song_favorite")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class SongFavorite {

    @Id
    @Field(name = "id")
    private String id;

    @Field(name = "song")
    private Song song;

    @Field(name = "favorite")
    private Favorite favorite;

    @Field(name = "status")
    private String status;

    @Field(name = "create_date")
    private Date create_date;

    @Field(name = "create_by")
    private String create_by;

    @Field(name = "update_date")
    private Date update_date;

    @Field(name = "update_by")
    private String update_by;

}
