ALTER TABLE payments
RENAME COLUMN txid TO gateway_transaction_id;

ALTER TABLE payments
RENAME CONSTRAINT uc_payments_txid TO uc_payments_gateway_transaction_id;

ALTER INDEX idx_payments_txid
RENAME TO idx_payments_gateway_transaction_id;