CREATE TABLE payments (
    id UUID NOT NULL,
    sale_id UUID NOT NULL,
    method VARCHAR(50) NOT NULL,
    status VARCHAR(50) NOT NULL,
    txid VARCHAR(255),
    amount DECIMAL(19, 2) NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    confirmed_at TIMESTAMP WITHOUT TIME ZONE,

    CONSTRAINT pk_payments PRIMARY KEY (id),
    CONSTRAINT fk_payments_on_sale FOREIGN KEY (sale_id) REFERENCES sales (id) ON DELETE CASCADE,
    CONSTRAINT uc_payments_sale_id UNIQUE (sale_id),
    CONSTRAINT uc_payments_txid UNIQUE (txid)
);

CREATE INDEX idx_payments_txid ON payments(txid);