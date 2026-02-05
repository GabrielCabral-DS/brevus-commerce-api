CREATE TABLE user_roles (
    id UUID PRIMARY KEY,
    role_user_id UUID NOT NULL,
    role_id UUID NOT NULL,

    CONSTRAINT fk_user_roles_user
        FOREIGN KEY (role_user_id) REFERENCES users(id) ON DELETE CASCADE,

    CONSTRAINT fk_user_roles_role
        FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE,

    CONSTRAINT uk_user_role UNIQUE (role_user_id, role_id)
);
