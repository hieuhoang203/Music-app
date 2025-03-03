package com.example.music.user;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UserDto {

    private String id;

    private String name;

    private String birthday;

    private String gender;

    private MultipartFile avatar;

    private String email;

    private String role;

}
