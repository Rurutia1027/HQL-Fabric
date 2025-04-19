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

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "example_transaction_flow_core")
public class TransactionFlowCore extends NamedArtifact {
    @Column(name = "transaction_id", unique = true, nullable = false)
    private String transactionId;

    @ManyToOne
    @JoinColumn(name = "account_id", referencedColumnName = "account_id")
    private AccountUser accountUser;

    @ManyToOne
    @JoinColumn(name = "product_code", referencedColumnName = "product_code")
    private ProductConfig product;

    @Column(name = "txn_type")
    private String txnType;

    private BigDecimal amount;

    private String currency;

    private String status;

    @Column(name = "txn_time")
    private Date txnTime;

    @Column(name = "source_system")
    private String sourceSystem;

    @OneToMany(mappedBy = "coreTransaction")
    private List<ReconciliationResult> reconciliationResults;
}