package com.common.entity;

import com.common.enums.State;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "site_data")
public class SiteData {
    @Id
    @Column(name = "date_ts")
    private Date dateTs;

    @Enumerated
    private State state;

    @Lob
    String json;
}
