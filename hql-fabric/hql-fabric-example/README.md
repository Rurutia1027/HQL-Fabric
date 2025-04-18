# HQL Fabric Example

## Overview

This example module demonstrates how to integrate **hql-fabric** into a project within a context of **fund
reconciliation** workflows. The goal is to showcase the flexibility and power of **hql-fabric**  when working with both
**native SQL queries** and **Hibernate JPA** in real-world financial scenarios.

## Background

**Fund reconciliation** is a critical operation in financial systems, ensuring that transactional records between the *
*core system** and the **clearing/settlement system** remain consistent.

In this example, we simulate a **simple financial fund reconciliation scenario** using a set of domain-specified
business tables to represent real-world transactional processes.

These test tables are created automatically during application startup via **Flyway migrations** and **JPA entity
mappings**. The corresponding **test data** is also inserted at the Flyway migration phase to ensure that all data is
ready for execution and testing when the example Spring application launches.

- Core transaction flows (`transaction_flow_core`).
- Clearing system flows (`transaction_flow_clearing`).
- Reconciliation results and exceptions.
- Reconciliation strategies and execution batches.

## Objectives

- Demonstrates how to **integrate hql-fabric** into a Spring Boot or Hibernate-based project.
- Explore **multiple query styles** using `hql-fabric`, including:
    - JPA/HQL-based entity queries.
    - Native SQL mappings.
    - Dynamic query assembly using reusable fragments
- Showcase how `hql-fabric` helps with:
    - Reducing boilerplate in complex queries
    - Reusing shared query components across scenarios
    - Ensuring consistent query logic for reconciliation-related data

## Scenarios Covered

This example covers a variety of typical reconciliation queries, such as:

- Matching transactions across core and clearing flows
- Identifying unmatched or duplicate transactions
- Generating reconciliation reports with dynamic filtering
- Querying reconciliation batch results and exception details

## Example Table Descriptions

### E-R Diagrams

### Core Transaction Flow Table `transaction_flow_core`

Represents transactions recorded by the **core system** (business-initiated layer).

- **SQL**

```sql
CREATE TABLE transaction_flow_core
(
    -- primary key 
    id             BIGINT PRIMARY KEY,

    -- unique transaction reference across all systems
    transaction_id VARCHAR(64)    NOT NULL UNIQUE,

    -- ID of the account that initiated the transaction 
    account_id     VARCHAR(64)    NOT NULL,

    -- code of the financial product involved 
    product_code   VARCHAR(32)    NOT NULL,

    -- transaction type: BUY | SELL | TRANSFER 
    txn_type       VARCHAR(16)    NOT NULL, -- BUY/SELL/TRANSFER

    -- transaction amount 
    amount         DECIMAL(18, 4) NOT NULL,

    -- currency (e.g., USD, CNY, EUR)
    currency       CHAR(3)        NOT NULL,

    -- transaction status: INITIATED | SUCCESS | FAILED 
    status         VARCHAR(32)    NOT NULL,-- INITIATED/SUCCESS/FAILED

    -- timestamp of when the transaction occured 
    txn_time       TIMESTAMP      NOT NULL,

    -- original system name 
    source_system  VARCHAR(32),

    -- record creation timestamp 
    created_at     TIMESTAMP DEFAULT now()
);
```

- **Relationships**
    - N:1 with `account_user` (N-transactions : 1-account)
    - N:1 with `product_config` (N-transactions : 1-product)
    - 1:N with `reconciliation_result`(1-transaction : N-reconciliation_result)

### Clearing Flow Table `transaction_flow_clearing`

Represents transactions processed by the **clearing system**, where actual fund transfers happen.

- **SQL**

```sql
CREATE TABLE transaction_flow_clearing
(
    -- primary key 
    id               BIGINT PRIMARY KEY,

    -- unique clearing reference 
    clearing_id      VARCHAR(64) NOT NULL UNIQUE,

    -- corresponding transaction_id from core system 
    transaction_id   VARCHAR(64) NOT NULL UNIQUE,

    -- channel used (e.g., SWIFT, UnionPay) 
    clearing_channel VARCHAR(32),

    -- cleared amount 
    amount           DECIMAL(18, 4),

    -- currency EUR | USD | CNY 
    currency         CHAR(3),

    -- clearing status 
    status           VARCHAR(32),

    -- time when clearing was processed 
    clearing_time    TIMESTAMP,

    -- original file or respor source 
    source_file      VARCHAR(128),

    -- record creation timestamp 
    created_at       TIMESTAMP DEFAULT now()
); 
```

- **Relationships**

### Reconciliation Strategy Table `reconciliation_strategy`

```sql
CREATE TABLE reconciliation_strategy
(
    -- 
    id             BIGINT PRIMARY KEY,
    name           VARCHAR(64),
    batch_type     VARCHAR(16), -- REALTIME / HOURLY / DAILY
    match_key      VARCHAR(64), -- transaction_id 
    compare_fields TEXT,        -- amount, status
    enabled        BOOLEAN   DEFAULT TRUE,
    cron_expr      VARCHAR(64),
    created_at     TIMESTAMP DEFAULT now()
); 
```

### Reconciliation Batch Table `reconciliation_batch`

- **SQL**

```sql
CREATE TABLE reconciliation_batch
(
    batch_id       BIGINT PRIMARY KEY,
    strategy_id    BIGINT REFERENCES reconciliation_strategy (id),
    batch_type     VARCHAR(16),
    scheduled_time TIMESTAMP,
    executed_at    TIMESTAMP,
    status         VARCHAR(16),
    created_at     TIMESTAMP DEFAULT now()
);
```

### Reconciliation Result Table `reconciliation_result`

- **SQL**

```sql
CREATE TABLE reconciliation_result
(
    id             BIGINT PRIMARY KEY,
    batch_id       BIGINT REFERENCES reconciliation_batch (batch_id),
    transaction_id VARCHAR(64),
    core_id        BIGINT REFERENCES transaction_flow_core (id),
    clearing_id    BIGINT REFERENCES transaction_flow_clearing (id),
    match_status   VARCHAR(32), -- MATCHED / MISMATCH / MISSING 
    details        TEXT,
    created_at     TIMESTAMP DEFAULT now()
); 
```

### Exception Detail Table `reconciliation_exception`

- **SQL**

```sql
CREATE TABLE reconciliation_exception
(
    id             BIGINT PRIMARY KEY,
    result_id      BIGINT REFERENCES reconciliation_result (id),
    exception_type VARCHAR(32),
    severity       VARCHAR(16),
    description    TEXT,
    resolved       BOOLEAN   DEFAULT FALSE,
    created_at     TIMESTAMP DEFAULT now()
); 
```

### Account User `account_user`

- **SQL**

```sql
CREATE TABLE account_user
(
    account_id VARCHAR(64) PRIMARY KEY,
    user_name  VARCHAR(64),
    user_type  VARCHAR(32),
    region     VARCHAR(32),
    create_at  TIMESTAMP DEFAULT now()
); 
```

### Product Configuration `product_config`

- **SQL**

```sql
CREATE TABLE product_config
(
    product_code VARCHAR(32) PRIMARY KEY,
    category     VARCHAR(32),
    is_active    BOOLEAN,
    risk_level   VARCHAR(16),
    created_at   TIMESTAMP DEFAULT now()
); 
```

