package com.common.entity;

import com.sun.istack.NotNull;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "url")
@Data
public class Url {
    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "url", unique = true)
    private String url;

    @Column
    private String parentUrl;

    public Url(String url) {
        this.url = url;
    }
}
