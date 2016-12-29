# 说明文档 #

[https://github.com/raindy/raindy-pcc](https://github.com/raindy/raindy-pcc "项目地址")

## 整体架构 ##

- 技术选型：nginx+spring+thrift+redis+mysql
- 对数据库进行水平切割，每一组单元包含独立的库和独立的服务，切割的规模事实际请求量定，水平切割后，每条数据的id都会带有一个库标识，id的格式为`库标识_id`，其中id为具体库中的存储id，保障每个用户的target，follow，like都在一个单元服务中
- 若有可能让单元服务的数据量较小，方便服务迁移
- 在每一个单元服务中，将常用数据缓存在redis中，缓存未命中从库中读取
- 数据写入到队列中，批量入库

![架构图](https://github.com/raindy/raindy-pcc/blob/master/archnote_raindy.png)

### web服务 ###

- 接收restful请求
- 服务转发：根据请求的id获取到库标识后到字典表中查询库标识对应的库服务地址，调用库服务完成请求

### 缓存服务 ###

- 基于thrift做服务调用
- 将近期活跃数据缓存到redis中，在redis未命中时从数据库中拉取数据
- 新增数据写入到redis队列中，由写入服务批量写入到库中

### 写入服务 ###
- 读取redis队列，写入对应的表中
- 根据redis队列长度，调整写入频率

## 接口描述 ##

### 用户 ###

- /user/new?user_name=xx #新增用户
- /user/get?user_id=xx #获取用户

### 点赞 ###

- /like/new?target_id=xx&own_id=xx&user_id=xx #点赞
- /like/is_liked?target_id=xx&user_id=xx #是否点赞
- /like/count?target_id=xx #点赞数
- /like/user_list?target_id=xx #点赞列表前20（好友优先）

## 数据库设计 ##

```sql
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
  `from_uid` int UNSIGNED NOT NULL,
  `to_uid` varchar(64) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`from_uid`,`to_uid`),
  unique index (to_uid,from_uid)
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
```


## 缺陷及问题 ##
1. 未作异常处理，重复检测
2. redis中数据多久过期尚未处理
3. redis队列存在数据丢失的可能，需要转换成mq做ack确认写入
4. 使用spring template批量写入存在某个写入失败，全都失败的问题
5. 如果一个target有100w个赞，若全部放入到redis中会有问题，也没必要，考虑只写入前1000条，定期清理
6. 在每个单元服务中进行读写分离拆分
7. 增加获取用户列表的分页方法
8. 取消follow的时候,写库并进行通知redis