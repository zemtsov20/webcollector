package com.common.entity;

import com.sun.istack.NotNull;
import lombok.Data;
import lombok.ToString;
import com.common.enums.HtmlState;


import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "url_data")
@Data
@ToString
public class UrlDataEntity {

    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long urlId;
    @Column(name="date")
    private Date urlTs;
    @NotNull
    @Column(name="state")
    private HtmlState state;
    @Column(name="text",columnDefinition="TEXT")
    private String text;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "content_id", referencedColumnName = "id")
    private ParsedContent parsedContent;

    UrlDataEntity() {}

    public UrlDataEntity(String html) {
        urlTs = new Date();
        state = HtmlState.UNCHECKED;
        text = html;
    }
}
