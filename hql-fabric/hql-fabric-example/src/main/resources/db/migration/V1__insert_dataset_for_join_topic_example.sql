-- Sample Accounts
INSERT INTO example_account_user (account_id, user_name, user_type, region) VALUES
                                                                        ('A001', 'Alice', 'individual', 'CN'),
                                                                        ('A002', 'Bob', 'institutional', 'EU');

-- Sample Products
INSERT INTO example_product_config (product_code, category, is_active, risk_level) VALUES
                                                                               ('P001', 'FUND', true, 'LOW'),
                                                                               ('P002', 'BOND', true, 'MEDIUM');

-- Core Transactions
INSERT INTO example_transaction_flow_core (id, transaction_id, account_id, product_code, txn_type, amount, currency, status, txn_time, source_system)
VALUES
    (1, 'TXN001', 'A001', 'P001', 'BUY', 1000.00, 'USD', 'SUCCESS', now(), 'system-A'),
    (2, 'TXN002', 'A002', 'P002', 'SELL', 500.00, 'EUR', 'SUCCESS', now(), 'system-B'),
    (3, 'TXN003', 'A001', 'P001', 'TRANSFER', 300.00, 'USD', 'INITIATED', now(), 'system-C');

-- Clearing Transactions
INSERT INTO example_transaction_flow_clearing (id, clearing_id, transaction_id, clearing_channel, amount, currency, status, clearing_time, source_file)
VALUES
    (100, 'CL001', 'TXN001', 'SWIFT', 1000.00, 'USD', 'CLEARED', now(), 'file-001.csv'),
    (101, 'CL002', 'TXN003', 'UnionPay', 300.00, 'USD', 'PENDING', now(), 'file-002.csv');

-- Reconciliation Strategy
INSERT INTO example_reconciliation_strategy (id, name, batch_type, match_key, compare_fields, enabled, cron_expr)
VALUES
    (1, 'Daily Core vs Clearing', 'DAILY', 'transaction_id', 'amount,status', true, '0 0 * * *');

-- Reconciliation Batch
INSERT INTO example_reconciliation_batch (batch_id, strategy_id, batch_type, scheduled_time, executed_at, status)
VALUES
    (1000, 1, 'DAILY', now(), now(), 'SUCCESS');

-- Reconciliation Results
INSERT INTO example_reconciliation_result (id, batch_id, transaction_id, core_id, clearing_id, match_status, details)
VALUES
    (9001, 1000, 'TXN001', 1, 100, 'MATCHED', '{}'),
    (9002, 1000, 'TXN002', 2, NULL, 'MISSING', '{}'),
    (9003, 1000, 'TXN004', NULL, NULL, 'MISMATCH', '{"amount": "diff"}');

-- Reconciliation Exception
INSERT INTO example_reconciliation_exception (id, result_id, exception_type, severity, description, resolved)
VALUES
    (5001, 9003, 'AMOUNT_MISMATCH', 'CRITICAL', 'Amount not equal between core and clearing', false);