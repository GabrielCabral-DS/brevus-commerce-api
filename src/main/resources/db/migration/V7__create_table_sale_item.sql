CREATE TABLE sale_items (
    id UUID NOT NULL,
    sale_id UUID NOT NULL,
    product_id UUID NOT NULL,
    quantity INTEGER NOT NULL,
    unit_price DECIMAL(19, 2) NOT NULL,
    subtotal DECIMAL(19, 2) NOT NULL,

    CONSTRAINT pk_sale_items PRIMARY KEY (id),

    CONSTRAINT fk_sale_items_on_sale
        FOREIGN KEY (sale_id) REFERENCES sales (id)
        ON DELETE CASCADE,

    CONSTRAINT fk_sale_items_on_product
        FOREIGN KEY (product_id) REFERENCES products (id)
);

CREATE INDEX idx_sale_items_sale_id ON sale_items(sale_id);