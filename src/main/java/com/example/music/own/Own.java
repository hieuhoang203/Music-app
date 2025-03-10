package com.example.music.own;

import com.example.music.song.Song;
import com.example.music.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.sql.Date;

@Entity
@Table(name = "tbl_own")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Own implements Serializable {

    @Id
    @Column(name = "id", length = 40)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author")
    private User author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "work")
    private Song work;

    @Column(name = "status")
    private String status;

    @Column(name = "create_date")
    private Date create_date;

    @Column(name = "create_by", length = 40)
    private String create_by;

    @Column(name = "update_date")
    private Date update_date;

    @Column(name = "update_by", length = 40)
    private String update_by;

}
