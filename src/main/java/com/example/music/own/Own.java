package com.example.music.own;

import com.example.music.song.Song;
import com.example.music.user.User;
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

@Document(collection = "tbl_own")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Own {

    @Id
    @Field(name = "id")
    private String id;

    @Field(name = "author")
    private User author;

    @Field(name = "work")
    private Song work;

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
