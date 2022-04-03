package com.common.entity;

import com.common.enums.State;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "page_url")
@Data
@NoArgsConstructor
public class PageUrl {
    @Id
    private Long id;

    @Column(name = "url")
    private String url;

    @Column(name = "parent_url")
    private String parentUrl;

    @Column(name = "has_no_child")
    private boolean hasNoChild;

    @Enumerated
    private State state;

    @Lob
    private String html;

    public PageUrl(Long id, String url, String parentUrl, boolean hasNoChild, State state) {
        this.id = id;
        this.url = url;
        this.parentUrl = parentUrl;
        this.hasNoChild = hasNoChild;
        this.state = state;
    }
}
