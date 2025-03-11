package com.example.music.account;

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
