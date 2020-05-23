package com.aga.security.app.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.OffsetDateTime;

@MappedSuperclass
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AbstractDomainClass {

    private static final long serialVersionUID = -95768746485L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Version
    private Integer version;
    private OffsetDateTime dateCreated;
    private OffsetDateTime lastUpdated;


    @PreUpdate
    @PrePersist
    public void updateTimeStamps() {
        lastUpdated =  OffsetDateTime.now();
        if (dateCreated==null) {
            dateCreated = OffsetDateTime.now();
        }
    }
}