package com.common.entity;

import com.common.enums.State;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "category_data")
public class CategoryData {
    @Id
    @Column
    private Long id;

    @Column(name = "parent_id")
    private Long parentId;

    @Column
    private String name;

    @Column(name = "page_url")
    private String pageUrl;

    @Enumerated
    private State state;

    @Lob
    private String json;
}
