-- 1. account_user
INSERT INTO example_account_user (id, account_id, display_name, user_name, user_type, region, disabled, locked, created_date, modified_date, version_number)
VALUES
    (1, 'acc-001', 'Alice', 'alice01', 'admin', 'US', false, false, NOW(), NOW(), 1),
    (2, 'acc-002', 'Bob', 'bob02', 'viewer', 'EU', false, false, NOW(), NOW(), 1);
