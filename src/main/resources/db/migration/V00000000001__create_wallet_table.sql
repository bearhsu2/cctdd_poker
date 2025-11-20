CREATE TABLE wallet (
    player_id VARCHAR(255) PRIMARY KEY,
    balance BIGINT NOT NULL,
    version INT NOT NULL
);
