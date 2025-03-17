package com.example.music.auth;

import com.example.music.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.sql.Date;

@Entity
@Table(name = "tbl_account")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Account implements Serializable {

    @Id
    @Column(name = "login")
    private String login;

    @Column(name = "pass")
    private String pass;

    @Column(name = "create_date")
    private Date create_date;

    @Column(name = "create_by")
    private String create_by;

    @Column(name = "update_date")
    private Date update_date;

    @Column(name = "update_by")
    private String update_by;

    @Column(name = "status")
    private String status;

    @OneToOne(mappedBy = "account")
    private User user;

    @Column(name = "role")
    private String role;

}
