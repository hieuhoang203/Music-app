package com.example.music.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data
public class ArtisResponse {

    private String id;

    private String name;

    private String avatar;

    private Boolean gender;

    private Date birthday;

    private Integer songs;

    private Integer follows;

    private String status;

}
