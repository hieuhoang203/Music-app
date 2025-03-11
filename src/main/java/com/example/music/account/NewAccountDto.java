package com.example.music.account;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class NewAccountDto {

    private String name;

    private String login;

    private String pass;

}
