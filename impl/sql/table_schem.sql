CREATE TABLE IF NOT EXISTS conf
(
    `namespace`       VARCHAR(255)     NOT NULL,                           # 命名空间
    `path`            VARCHAR(255)     NOT NULL,                           # 配置路径 (ConfigPath)
    `type`            TINYINT UNSIGNED NOT NULL DEFAULT 0,                 # 数据类型 (Integer/Byte/List/Map/...)
    `value`           MEDIUMTEXT,                                          # 配置项的值 (可能为JSON格式)
    `inline_comments` TEXT,                                                # 行内注释
    `header_comments` MEDIUMTEXT,                                          # 顶部注释
    `create_time`     DATETIME         NOT NULL DEFAULT CURRENT_TIMESTAMP, # 创建时间
    `update_time`     DATETIME         NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`namespace`, `path`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

