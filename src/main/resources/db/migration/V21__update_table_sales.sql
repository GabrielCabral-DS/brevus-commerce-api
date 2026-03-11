ALTER TABLE sales
ADD COLUMN delivery_code VARCHAR(100),
ADD COLUMN delivery_code_confirmed BOOLEAN DEFAULT FALSE,
ADD COLUMN delivery_code_confirmed_at TIMESTAMP;