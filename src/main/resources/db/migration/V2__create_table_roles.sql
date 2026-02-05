CREATE TABLE roles (
    id UUID PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

INSERT INTO roles (id, name) VALUES

        ('e1f7a5c1-7f8c-4bda-9d57-4a7a8bdf0a01', 'ADMIN'),
        ('a4b2fdd3-cc35-44a0-9b28-61f3d52b38d2', 'SELLER'),
        ('d3b2e9e1-2d4c-4f58-bb77-3a8c74bb28f9', 'CLIENT')
