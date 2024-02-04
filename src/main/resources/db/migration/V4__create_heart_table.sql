CREATE TABLE IF NOT EXISTS heart
(
    id               BIGINT AUTO_INCREMENT PRIMARY KEY,
    member_id        BIGINT      NOT NULL,
    relation_id      BIGINT      NOT NULL,
    is_give          TINYINT     NOT NULL,
    money            BIGINT      NOT NULL,
    day              DATE        NOT NULL,
    event            VARCHAR(50) NOT NULL,
    memo             TEXT        NULL,
    created_at       DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_modified_at DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

ALTER TABLE heart
    ADD CONSTRAINT fk_heart_member_id_from_member
        FOREIGN KEY (member_id)
            REFERENCES member (id);

ALTER TABLE heart
    ADD CONSTRAINT fk_heart_relation_id_from_relation
        FOREIGN KEY (relation_id)
            REFERENCES relation (id);
