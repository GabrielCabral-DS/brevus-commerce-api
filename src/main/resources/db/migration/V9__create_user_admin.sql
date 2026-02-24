INSERT INTO users (id, name, email, date_birth, phone, password, date_registered)
VALUES (
     'bb6dc8a7-1cb9-4d4e-8713-1b0359e03f99',
    'Brevus Commerce API',
    'brevuscode@gmail.com',
    '1998-05-15',
    '(83) 98765-4321',
    '$2a$10$orBwT9U9Ccl3m584W4bKKOODOOF4tKE20rc8/5l2iLxKLw242A7eW',
	'2025-12-24'
);

-- senha: Brevus_code@2025

INSERT INTO user_roles (id, role_user_id, role_id)
VALUES (
    '550e8400-e29b-41d4-a716-446655440000',
    'bb6dc8a7-1cb9-4d4e-8713-1b0359e03f99',
    'e1f7a5c1-7f8c-4bda-9d57-4a7a8bdf0a01'
);