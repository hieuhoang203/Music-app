package com.example.music;

import com.cloudinary.Cloudinary;
import com.cloudinary.api.ApiResponse;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MusicAppApplication {

    @Bean
    public Cloudinary getCloudinary(){
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "hieuhv203",
                "api_key", "626331438846633",
                "api_secret", "XDVR-B8ZQiCsIZFFqtvkGnh4G6g",
                "secure", true));
    }

    public static void main(String[] args) {
        SpringApplication.run(MusicAppApplication.class, args);
    }

}
