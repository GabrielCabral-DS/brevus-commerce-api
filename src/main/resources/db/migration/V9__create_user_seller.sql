INSERT INTO users (id, name, email, date_birth, phone, password, date_registered)
VALUES (
     'bb6dc8a7-1cb9-4d4e-8713-1b0359e03d88',
    'Admin Seller',
    'admin@gmail.com',
    '1998-05-15',
    '(83) 98765-4321',
    '$2a$10$YWPCtgZW5l6r/EAefBNJxOBCRM2kfAuUSoc/glYeZh3S0Lqevk3DS',
	'2025-12-24'
);


INSERT INTO user_roles (id, role_user_id, role_id)
VALUES (
    '550e8400-e29b-41d4-a716-446655441111',
    'bb6dc8a7-1cb9-4d4e-8713-1b0359e03d88',
    'a4b2fdd3-cc35-44a0-9b28-61f3d52b38d2'
);