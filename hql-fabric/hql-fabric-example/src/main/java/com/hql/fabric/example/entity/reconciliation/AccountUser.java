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
@Table(name = "example_account_user")
public class AccountUser extends NamedArtifact {
    @Column(name = "account_id", unique = true, nullable = false)
    private String accountId;

    private String userName;

    private String userType;

    private String region;

    @OneToMany(mappedBy = "accountUser")
    private List<TransactionFlowCore> transactions;
}