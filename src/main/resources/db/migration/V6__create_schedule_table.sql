CREATE TABLE IF NOT EXISTS schedule
(
    id               BIGINT AUTO_INCREMENT PRIMARY KEY,
    member_id        BIGINT       NOT NULL,
    relation_id      BIGINT       NOT NULL,
    day              DATE         NOT NULL,
    event            VARCHAR(50)  NOT NULL,
    repeat_type      VARCHAR(30)  NULL,
    repeat_finish    DATE         NULL,
    alarm            DATETIME     NULL,
    time             TIME         NULL,
    link             VARCHAR(250) NULL,
    location         VARCHAR(250) NULL,
    memo             TEXT         NULL,
    created_at       DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_modified_at DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

ALTER TABLE schedule
    ADD CONSTRAINT fk_schedule_member_id_from_member
        FOREIGN KEY (member_id)
            REFERENCES member (id);

ALTER TABLE schedule
    ADD CONSTRAINT fk_schedule_relation_id_from_relation
        FOREIGN KEY (relation_id)
            REFERENCES relation (id);
