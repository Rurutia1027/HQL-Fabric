package com.hql.fabric.example.entity.reconciliation;

import com.hql.fabric.persistence.entity.NamedArtifact;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "v2_reconciliation_strategy")
public class ReconciliationStrategy extends NamedArtifact {

    @Column(name = "batch_type")
    private String batchType;

    @Column(name = "match_key")
    private String matchKey;

    @Column(name = "compare_fields")
    private String compareFields;

    private Boolean enabled;

    @Column(name = "cron_expr")
    private String cronExpr;

    @OneToMany(mappedBy = "strategy")
    private List<ReconciliationBatch> batches;
}