package com.powell.cardapi.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDate;

@Data
public abstract class ModificationTrackingEntity {

    @JsonIgnore
    @LastModifiedDate
    private LocalDate lastModified;

    @JsonIgnore
    @CreationTimestamp
    private LocalDate creationDate;
}
