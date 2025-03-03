package com.example.music.genres;

import lombok.Value;

public interface GenresCombobox {

    @Value("#{target.value}")
    String getValue();

    @Value("#{target.label}")
    String getLabel();

}
