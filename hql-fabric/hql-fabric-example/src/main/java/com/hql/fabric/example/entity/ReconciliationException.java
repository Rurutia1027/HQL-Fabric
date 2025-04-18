package com.hql.fabric.example.entity;

import com.hql.fabric.persistence.entity.Artifact;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "reconciliation_exception")
public class ReconciliationException extends Artifact {

    @ManyToOne
    @JoinColumn(name = "result_id")
    private ReconciliationResult result;

    @Column(name = "exception_type")
    private String exceptionType;

    private String severity;

    private String description;

    private Boolean resolved;
}