package com.example.music.own;

import com.example.music.song_genres.SongGenres;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WorkDto {

    private String name;

    private MultipartFile avatar;

    private MultipartFile sound;

    private String album;

    private Set<Own> artis;

    private Set<SongGenres> genres;

    private Short duration;

}
