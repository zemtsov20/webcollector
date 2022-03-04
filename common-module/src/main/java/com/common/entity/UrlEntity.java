package com.common.entity;

import com.sun.istack.NotNull;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "url")
@Data
@ToString
public class UrlEntity {
    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @Column(unique = true)
    private String url;

    UrlEntity() {}

    public UrlEntity(String url) {
        this.url = url;
    }

    public Long getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }
}
