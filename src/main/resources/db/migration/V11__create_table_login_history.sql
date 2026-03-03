CREATE TABLE login_history (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    email VARCHAR(255),
    access_token TEXT,
    login_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_login_history_user FOREIGN KEY (user_id) REFERENCES users(id)
);