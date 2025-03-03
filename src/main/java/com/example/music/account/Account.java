package com.example.music.account;

import com.example.music.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.util.Date;

@Document(collection = "tbl_account")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Account implements Serializable {

    @Id
    @Field(name = "login")
    private String login;

    @Field(name = "pass")
    private String pass;

    @Field(name = "create_date")
    private Date createDate;

    @Field(name = "create_by")
    private String createBy;

    @Field(name = "update_date")
    private Date update_date;

    @Field(name = "update_by")
    private String update_by;

    @Field(name = "status")
    private String status;

    @Field(name = "user")
    private User user;

    @Field(name = "role")
    private String role;


}
