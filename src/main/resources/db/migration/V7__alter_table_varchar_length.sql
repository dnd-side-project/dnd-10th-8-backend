ALTER TABLE member
    MODIFY COLUMN nickname VARCHAR(100) NULL UNIQUE;

ALTER TABLE member_group
    MODIFY COLUMN name VARCHAR(100) NOT NULL;

ALTER TABLE relation
    MODIFY COLUMN name VARCHAR(100) NOT NULL;

ALTER TABLE heart
    MODIFY COLUMN event VARCHAR(200) NOT NULL;

ALTER TABLE heart_tag
    MODIFY COLUMN name VARCHAR(100) NOT NULL;

ALTER TABLE schedule
    MODIFY COLUMN event VARCHAR(200) NOT NULL;
