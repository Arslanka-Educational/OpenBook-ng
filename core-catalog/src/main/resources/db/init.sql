create table book_instance
(
    id              uuid primary key,
    book_content_id uuid not null,
    status          varchar not null
);