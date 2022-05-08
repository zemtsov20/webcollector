package com.common.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@NoArgsConstructor
@Entity
@Table(name = "category_data")
public class CategoryData {
    @Id
    private Long id;

    @Column(name = "parent_id")
    private Long parentId;

    @Column
    private String name;

    @Column(name = "page_url")
    private String pageUrl;

    public Boolean hasChildren;
}
