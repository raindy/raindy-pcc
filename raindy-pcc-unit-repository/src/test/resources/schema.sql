drop table if exists `account`;
CREATE TABLE if not exists `account` (
  `user_id` int unsigned NOT NULL,
  `user_name` varchar(32) DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`user_id`),
  UNique username_idx(`user_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

drop table if exists `follow`;
CREATE TABLE if not exists `follow` (
  `source_uid` int UNSIGNED NOT NULL,
  `follow_uid` varchar(64) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`source_uid`,`follow_uid`)
) ENGINE=InnoDB;

drop table if exists `target_like`;
CREATE TABLE if not exists `target_like` (
  `id` int unsigned auto_increment,
  `target_id` int unsigned NOT NULL,
  `own_id` int unsigned NOT NULL,
  `user_id` varchar(64) NOT NULL,
  `type` smallint NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  unique index (target_id,user_id)
) ENGINE=InnoDB;