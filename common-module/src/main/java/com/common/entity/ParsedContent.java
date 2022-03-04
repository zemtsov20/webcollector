package com.common.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "parsed_content")
@Data
public class ParsedContent {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;
    @Column
    private Integer productId;
    @Column
    private String productName;
    @Column
    private Integer productPrice;
    @OneToOne(mappedBy = "parsedContent")
    private UrlDataEntity urlDataEntity;

    public ParsedContent() { }

    public ParsedContent(Integer productId, String productName, Integer productPrice) {
        this.productId = productId;
        this.productName = productName;
        this.productPrice = productPrice;
    }
}
