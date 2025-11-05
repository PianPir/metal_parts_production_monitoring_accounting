CREATE TABLE roles (
                       id BIGSERIAL PRIMARY KEY,
                       name VARCHAR(255) NOT NULL UNIQUE CHECK (name IN ('ROLE_ADMIN', 'ROLE_USER'))
);

CREATE TABLE users (
                       id BIGSERIAL PRIMARY KEY,
                       username VARCHAR(255) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL
);

CREATE TABLE user_roles (
                            user_id BIGINT NOT NULL,
                            role_id BIGINT NOT NULL,
                            PRIMARY KEY (user_id, role_id),
                            FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
                            FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE
);

CREATE TABLE machines (
                          id BIGSERIAL PRIMARY KEY,
                          name VARCHAR(255) NOT NULL UNIQUE,
                          machine_type VARCHAR(255) NOT NULL CHECK (machine_type IN ('MILLING', 'TURNING', 'CUTTING'))
);

CREATE TABLE material_batches (
                                  id BIGSERIAL PRIMARY KEY,
                                  supplier VARCHAR(255) NOT NULL,
                                  alloy_type VARCHAR(255) NOT NULL,
                                  weight_kg NUMERIC(19,4) NOT NULL,
                                  arrival_date TIMESTAMP(6) NOT NULL
);

-- Начальные данные
INSERT INTO roles (name) VALUES ('ROLE_USER'), ('ROLE_ADMIN');