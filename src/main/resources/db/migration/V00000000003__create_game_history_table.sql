CREATE TABLE game_history (
    hand_id VARCHAR(255) PRIMARY KEY,
    hand_result_json TEXT NOT NULL,
    version INT NOT NULL
);