package com.example.music.favorite;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class FavoriteDto {

    private String name;

    private MultipartFile avatar;

}
