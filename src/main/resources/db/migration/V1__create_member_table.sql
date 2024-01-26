CREATE TABLE IF NOT EXISTS member
(
    id                BIGINT AUTO_INCREMENT PRIMARY KEY,
    social_type       VARCHAR(30)  NULL,
    social_id         VARCHAR(200) NULL UNIQUE,
    email             VARCHAR(200) NULL UNIQUE,
    profile_image_url VARCHAR(200) NOT NULL,
    nickname          VARCHAR(30)  NULL UNIQUE,
    gender            VARCHAR(20)  NOT NULL,
    birth             DATE         NOT NULL,
    status            VARCHAR(20)  NOT NULL,
    created_at        DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_modified_at  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    UNIQUE (social_id, email)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;
