CREATE TABLE IF NOT EXISTS conf
(
    `namespace`       VARCHAR(255) NOT NULL, # 记录配置文件的命名空间
    `section`         VARCHAR(255) NOT NULL, # 记录配置文件数值的指定路径
    `type`            VARCHAR(255) NOT NULL,
    `value`           LONGTEXT,              # 记录该配置项的值 (可能为JSON格式)
    `inline_comments` TEXT,                  # 记录配置文件的行内注释
    `header_comments` MEDIUMTEXT,            # 记录配置文件的顶部注释
    `created_at`      DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at`      DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`namespace`, `section`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;