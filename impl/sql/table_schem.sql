CREATE TABLE IF NOT EXISTS conf
(
    `namespace`       VARCHAR(255) NOT NULL, # 命名空间
    `section`         VARCHAR(255) NOT NULL, # 配置路径 (ConfigPath)
    `type`            VARCHAR(255) NOT NULL, # 数据类型 (Integer/Byte/List/Map/...)
    `value`           MEDIUMTEXT,            # 配置项的值 (可能为JSON格式)
    `inline_comments` TINYTEXT,              # 行内注释
    `header_comments` TEXT,                  # 顶部注释
    `created_at`      DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at`      DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`namespace`, `section`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

