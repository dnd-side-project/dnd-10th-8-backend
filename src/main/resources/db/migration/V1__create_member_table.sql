CREATE TABLE IF NOT EXISTS member
(
    id               BIGINT AUTO_INCREMENT PRIMARY KEY,
    social_type      VARCHAR(30)  NOT NULL,
    social_id        VARCHAR(100) NOT NULL UNIQUE,
    email            VARCHAR(200) NOT NULL UNIQUE,
    name             VARCHAR(100) NOT NULL,
    gender           VARCHAR(20)  NULL,
    birth            DATE         NULL,
    created_at       DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_modified_at DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    UNIQUE (social_id, email)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;
