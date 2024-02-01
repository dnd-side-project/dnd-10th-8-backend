CREATE TABLE IF NOT EXISTS member_group
(
    id        BIGINT AUTO_INCREMENT PRIMARY KEY,
    member_id BIGINT      NOT NULL,
    name      VARCHAR(30) NOT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

ALTER TABLE member_group
    ADD CONSTRAINT fk_member_group_member_id_from_member
        FOREIGN KEY (member_id)
            REFERENCES member (id);
