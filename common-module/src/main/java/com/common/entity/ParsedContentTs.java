package com.common.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "parsed_content_ts")
@Data
@IdClass(ParsedContentTsId.class)
@NoArgsConstructor
public class ParsedContentTs {

    @Id
    private Long productId;
    @Id
    @Column(name = "taken_in")
    private Date takenIn;

    @Column(name = "product_price")
    private Integer productPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "url", referencedColumnName = "url", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private ParsedContent parsedContent;

    public ParsedContentTs(Long productId, Integer productPrice) {
        this.productId = productId;
        this.productPrice = productPrice;
    }
}
