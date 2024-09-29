GRANT ALL PRIVILEGES ON DATABASE "core-booking-master" TO "core-booking-owner";

CREATE TABLE IF NOT EXISTS reservations (
    id VARCHAR(255) PRIMARY KEY,
    user_id VARCHAR(255) NOT NULL,
    book_id VARCHAR(255) NOT NULL,
    idempotency_token VARCHAR(255) NOT NULL,
    created_at timestamptz  NOT NULL,
    status VARCHAR(50),
    reason VARCHAR(50),
    UNIQUE (id, user_id)
);

CREATE INDEX user_id_book_id_index ON reservations (user_id, book_id);