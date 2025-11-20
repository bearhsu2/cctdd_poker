CREATE TABLE hand (
    id VARCHAR(255) PRIMARY KEY,
    status VARCHAR(50) NOT NULL,
    version INT NOT NULL,
    user_ids TEXT NOT NULL,
    bet INT NOT NULL,
    hole_cards_json TEXT NOT NULL,
    board_json TEXT NOT NULL
);

CREATE INDEX idx_hand_status ON hand(status);
