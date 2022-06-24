-- liquibase formatted sql

--changeset yperunov:1
CREATE TABLE if not exists notification_task (
    id SERIAL,
    chat_id SERIAL NOT NULL,
    message_content TEXT NOT NULL,
    time_stamp TIMESTAMP NOT NULL,
    sent_message BOOLEAN NOT NULL DEFAULT FALSE
);
