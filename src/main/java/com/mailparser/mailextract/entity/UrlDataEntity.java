package com.mailparser.mailextract.entity;

import com.mailparser.mailextract.enums.HtmlState;
import com.sun.istack.NotNull;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Table
@Data
@ToString
public class UrlDataEntity {

    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long urlId;
    @Column(name="date")
    private Date urlTs;
    // rename columns
    @NotNull
    @Column(name="state")
    private HtmlState state;
    @Column(name="text",columnDefinition="TEXT")
    private String text;
    @ElementCollection
    Set<String> emailsOnPage;

    UrlDataEntity() {}

    public UrlDataEntity(String html) {
        urlTs = new Date();
        state = HtmlState.UNCHECKED;
        text = html;
    }

    public Long getUrlId() {
        return urlId;
    }

    public Date getUrlTs() {
        return urlTs;
    }

    public HtmlState getState() {
        return state;
    }

    public String getText() {
        return text;
    }
}
