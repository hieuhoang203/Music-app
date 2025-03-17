package com.example.music.auth;

import lombok.Data;
import lombok.Getter;
import lombok.NonNull;

@Data
@Getter
public class LoginDto {

    @NonNull
    private String login;

    @NonNull
    private String pass;

}
