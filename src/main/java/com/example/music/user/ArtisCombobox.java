package com.example.music.user;

import lombok.Value;

public interface ArtisCombobox {

    @Value("#{target.value}")
    String getValue();

    @Value("#{target.label}")
    String getLabel();

}
