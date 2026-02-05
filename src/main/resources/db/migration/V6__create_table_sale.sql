CREATE TABLE sales (
    id UUID NOT NULL,
    sale_date TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(50) NOT NULL,
    total_amount DECIMAL(19, 2) NOT NULL DEFAULT 0.00,
    client_id UUID,
    seller_id UUID,
    CONSTRAINT pk_sales PRIMARY KEY (id),
    CONSTRAINT fk_sales_on_client FOREIGN KEY (client_id) REFERENCES users (id),
    CONSTRAINT fk_sales_on_seller FOREIGN KEY (seller_id) REFERENCES users (id)
);

CREATE INDEX idx_sales_client ON sales(client_id);
CREATE INDEX idx_sales_seller ON sales(seller_id);