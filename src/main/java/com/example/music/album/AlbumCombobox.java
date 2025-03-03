package com.example.music.album;

import lombok.Value;

public interface AlbumCombobox {

    @Value("#{target.value}")
    String getValue();

    @Value("#{target.label}")
    String getLabel();

}
