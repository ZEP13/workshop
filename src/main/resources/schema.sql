CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    mail TEXT UNIQUE NOT NULL,
    mobil VARCHAR(20) UNIQUE NOT NULL,
    firstname VARCHAR(255) NOT NULL, 
    lastname VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(255) NOT NULL,
    createdAt DATE
);

CREATE TABLE IF NOT EXISTS appointment (
    id SERIAL PRIMARY KEY,
    date DATE NOT NULL,
    time TIME NOT NULL,
    id_client INT NOT NULL,
    id_creator INT NOT NULL,
    status VARCHAR(255) NOT NULL,
    createdAt DATE,
    CONSTRAINT fk_users_client
        FOREIGN KEY (id_client)
        REFERENCES users(id)
        ON DELETE CASCADE,
    CONSTRAINT fk_users_creator
        FOREIGN KEY (id_creator)
        REFERENCES users(id)
        ON DELETE CASCADE
);
