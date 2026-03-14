CREATE TABLE products (
    id UUID NOT NULL,
    name VARCHAR(255) NOT NULL,
    price DECIMAL(19, 2) NOT NULL,
    stock_quantity INTEGER NOT NULL DEFAULT 0,
    category_id UUID,
    CONSTRAINT pk_products PRIMARY KEY (id),
    CONSTRAINT fk_products_on_category FOREIGN KEY (category_id) REFERENCES categories (id)
);