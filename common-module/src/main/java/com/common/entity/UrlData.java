package com.common.entity;

import com.sun.istack.NotNull;
import lombok.Data;
import com.common.enums.HtmlState;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;


import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "url_data")
@Data
public class UrlData {

    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long urlId;

    @Column(name="date")
    private Date urlTs;

    @NotNull
    @Column(name="state")
    private HtmlState state;

    @Column(name="text", columnDefinition="TEXT")
    private String text;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "content_id", referencedColumnName = "id")
    private ParsedContent parsedContent;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "url", referencedColumnName = "url", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Url url;

    public UrlData(String html) {
        urlTs = new Date();
        state = HtmlState.UNCHECKED;
        text = html;
    }
}
