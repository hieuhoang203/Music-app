package com.example.music.user;

import com.example.music.account.Account;
import com.example.music.album.Album;
import com.example.music.favorite.Favorite;
import com.example.music.follow.Follow;
import com.example.music.own.Own;
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

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Document(collection = "tbl_user")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class User implements Serializable {

    @Id
    @Field(name = "id")
    private String id;

    @Field(name = "name")
    private String name;

    @Field(name = "gender")
    private Boolean gender;

    @Field(name = "birth_day")
    private Date birthday;

    @Field(name = "avatar")
    private String avatar;

    @Field(name = "account")
    private Account account;

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

    @Field(name = "followers")
    @JsonIgnore
    private Set<Follow> user;

    @Field(name = "idols")
    @JsonIgnore
    private Set<Follow> idol;

    @Field(name = "albums")
    @JsonIgnore
    private Set<Album> albums;

    @Field(name = "favorites")
    @JsonIgnore
    private Set<Favorite> favorite;

    @Field(name = "authors")
    @JsonIgnore
    private Set<Own> owns;

}
