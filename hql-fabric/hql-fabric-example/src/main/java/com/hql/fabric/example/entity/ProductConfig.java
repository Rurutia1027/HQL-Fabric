package com.hql.fabric.example.entity;

import com.hql.fabric.persistence.entity.Artifact;
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
@Table(name = "example_product_config")
public class ProductConfig extends Artifact {

    @Column(name = "product_code", unique = true, nullable = false)
    private String productCode;

    private String category;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "risk_level")
    private String riskLevel;

    @OneToMany(mappedBy = "product")
    private List<TransactionFlowCore> transactions;
}