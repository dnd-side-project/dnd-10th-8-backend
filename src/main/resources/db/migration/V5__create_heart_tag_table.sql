CREATE TABLE IF NOT EXISTS heart_tag
(
    id       BIGINT AUTO_INCREMENT PRIMARY KEY,
    heart_id BIGINT      NOT NULL,
    name     VARCHAR(30) NOT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

ALTER TABLE heart_tag
    ADD CONSTRAINT fk_heart_tag_heart_id_from_heart
        FOREIGN KEY (heart_id)
            REFERENCES heart (id);
