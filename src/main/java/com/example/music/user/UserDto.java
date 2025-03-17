package com.example.music.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private String name;

    private String birthday;

    private Boolean gender;

    private MultipartFile avatar;

    private String email;

    private String password;

    private String role;

}
