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

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "v2_transaction_flow_clearing")
public class TransactionFlowClearing extends NamedArtifact {

    @Column(name = "clearing_id", unique = true)
    private String clearingId;

    @Column(name = "transaction_id", unique = true)
    private String transactionId;

    private String clearingChannel;

    private BigDecimal amount;

    private String currency;

    private String status;

    @Column(name = "clearing_time")
    private Date clearingTime;

    private String sourceFile;

    @OneToMany(mappedBy = "clearingTransaction")
    private List<ReconciliationResult> reconciliationResults;
}