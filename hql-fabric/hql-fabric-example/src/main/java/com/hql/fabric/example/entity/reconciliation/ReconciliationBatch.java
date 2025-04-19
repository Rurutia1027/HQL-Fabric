package com.hql.fabric.example.entity.reconciliation;

import com.hql.fabric.persistence.entity.NamedArtifact;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "example_reconciliation_batch")
public class ReconciliationBatch extends NamedArtifact {
    @Column(name = "batch_id", unique = true, nullable = false)
    private Long batchId;

    @ManyToOne
    @JoinColumn(name = "strategy_id")
    private ReconciliationStrategy strategy;

    @Column(name = "batch_type")
    private String batchType;

    @Column(name = "scheduled_time")
    private Date scheduledTime;

    @Column(name = "executed_at")
    private Date executedAt;

    private String status;

    @OneToMany(mappedBy = "batch")
    private List<ReconciliationResult> results;
}