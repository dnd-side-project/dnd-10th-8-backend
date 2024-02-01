CREATE TABLE IF NOT EXISTS relation
(
    id               BIGINT AUTO_INCREMENT PRIMARY KEY,
    member_id        BIGINT      NOT NULL,
    group_id         BIGINT      NOT NULL,
    name             VARCHAR(30) NOT NULL,
    phone            VARCHAR(20) NOT NULL,
    memo             TEXT        NULL,
    created_at       DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_modified_at DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

ALTER TABLE relation
    ADD CONSTRAINT fk_relation_member_id_from_member
        FOREIGN KEY (member_id)
            REFERENCES member (id);

ALTER TABLE relation
    ADD CONSTRAINT fk_relation_group_id_from_member_group
        FOREIGN KEY (group_id)
            REFERENCES member_group (id);
