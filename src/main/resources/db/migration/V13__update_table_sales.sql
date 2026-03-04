ALTER TABLE sales
ADD COLUMN delivery_address_id UUID;

ALTER TABLE sales
ADD CONSTRAINT fk_sale_address
FOREIGN KEY (delivery_address_id)
REFERENCES addresses(id);