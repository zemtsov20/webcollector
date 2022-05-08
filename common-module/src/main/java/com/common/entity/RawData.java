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
@Table(name = "raw_data")
public class RawData {
    @Id
    @Column(name = "data_ref", columnDefinition="TEXT")
    private String dataRef;

    @Column(name = "parent_ref")
    private String parentRef;

    @Enumerated
    private State state;

    @Lob
    private String json;

    // constructor for tests
    public RawData(String dataRef, String parentRef, State state) {
        this.dataRef = dataRef;
        this.parentRef = parentRef;
        this.state = state;
    }
}
