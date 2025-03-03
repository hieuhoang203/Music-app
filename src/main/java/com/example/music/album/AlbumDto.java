package com.example.music.album;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class AlbumDto {

    private String name;

    private MultipartFile avatar;

    private String artis;

    private String release_date;

    private List<String> songs;

}
