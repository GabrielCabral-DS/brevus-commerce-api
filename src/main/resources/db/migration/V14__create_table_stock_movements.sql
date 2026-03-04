CREATE TABLE stock_movements (
    id UUID PRIMARY KEY,
    product_id UUID NOT NULL,
    movement_type VARCHAR(20) NOT NULL, -- ENTRY / EXIT / ADJUSTMENT
    quantity INTEGER NOT NULL,
    reference_type VARCHAR(30), -- SALE / MANUAL / RESTOCK
    reference_id UUID,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,

    CONSTRAINT fk_stock_product
        FOREIGN KEY (product_id)
        REFERENCES products(id)
        ON DELETE CASCADE
);