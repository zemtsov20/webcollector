package com.common.entity;

import lombok.Data;
import com.common.enums.State;
import lombok.NoArgsConstructor;


import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "parsed_content")
@Data
@NoArgsConstructor
public class ParsedContent {

    @Id
    @Column(name = "url")
    private String url;

    @Column(name = "product_id")
    private Long productId;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "date")
    private Date urlTs;

    @Enumerated(EnumType.STRING)
    private State state;

    @Lob
    private String html;

    public ParsedContent(String url) {
        this.url = url;
        this.urlTs = new Date();
        this.state = State.QUEUED;
    }
}
