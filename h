CREATE TABLE `sms_bargain_config` (
  `id` bigint(11) unsigned NOT NULL COMMENT 'id',
  `plug_ins_id` bigint(11) NOT NULL DEFAULT '0' COMMENT '插件id',
  `can_num` int(11) DEFAULT NULL COMMENT '能砍的次数',
  `help_num` int(11) DEFAULT NULL COMMENT '每天最多帮别人砍的次数',
  `parameter` char(11) DEFAULT NULL COMMENT '每次砍价的参数',
  `invalid_time` int(11) DEFAULT NULL COMMENT '逾期失效时间',
  `add_time` datetime NULL DEFAULT NULL COMMENT '修改时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='砍价免单设置表';

CREATE TABLE `sms_bargain_record` (
  `id` bigint(11) unsigned NOT NULL COMMENT 'id',
  `s_id` bigint(11) NOT NULL DEFAULT '0' COMMENT '属性id',
  `user_id` bigint(15) NOT NULL COMMENT '用户ID',
  `money` decimal(12,2) NOT NULL DEFAULT '0.00' COMMENT '金额',
  `add_time` datetime NULL DEFAULT NULL COMMENT '添加时间',
  `event` varchar(200) DEFAULT NULL COMMENT '事件',
  `name` varchar(15) NOT NULL COMMENT '收货人',
  `tel` char(15) NOT NULL COMMENT '联系方式',
  `sheng` int(11) NOT NULL DEFAULT '0' COMMENT '省',
  `city` int(11) NOT NULL DEFAULT '0' COMMENT '市',
  `quyu` int(11) NOT NULL DEFAULT '0' COMMENT '县',
  `address` varchar(255) NOT NULL COMMENT '收货地址（不加省市区）',
  `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '状态 0:砍价中 1:砍价成功 2:逾期失效 3:生成订单',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='砍价免单记录表';

CREATE TABLE `sms_draw_user` (
  `id` bigint(11) NOT NULL COMMENT 'ID',
  `draw_id` bigint(11) DEFAULT NULL COMMENT '拼团ID',
  `user_id` bigint(30) DEFAULT NULL COMMENT '用户ID',
  `time` datetime NULL DEFAULT NULL COMMENT '用户参团时间',
  `role` varchar(30) DEFAULT '0' COMMENT '用户角色（默认 0：团长  userid:该用户分享进来的用户）',
  `lottery_status` int(11) DEFAULT '0' COMMENT '抽奖状态（0.参团中 1.待抽奖 2.参团失败 3.抽奖失败 4.抽奖成功）'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='抽奖与用户关联表';

CREATE TABLE `sms_experience` (
  `id` bigint(11) unsigned NOT NULL COMMENT 'id',
  `user_id` bigint(15) NOT NULL DEFAULT '' COMMENT '用户id',
  `seller_id` bigint(15) NOT NULL DEFAULT '' COMMENT '商家id',
  `name` varchar(20) NOT NULL DEFAULT '' COMMENT '姓名',
  `mobile` varchar(20) DEFAULT NULL COMMENT '手机',
  `address` varchar(300) DEFAULT NULL COMMENT '地址',
  `num` int(11) NOT NULL DEFAULT '0' COMMENT '数量',
  `content` text NOT NULL COMMENT '内容',
  `add_date` timestamp NULL DEFAULT NULL COMMENT '添加时间',
  `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '类型 0:申请预约 1:已预约 2:取消预约 3:完成'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='预约表';


CREATE TABLE `sms_detailed_commission` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `userid` bigint(50) DEFAULT NULL,
  `sNo` varchar(255) DEFAULT NULL COMMENT '订单号',
  `money` float(10,2) DEFAULT '0.00' COMMENT '应发佣金',
  `s_money` float(10,2) DEFAULT '0.00' COMMENT '实发佣金',
  `status` int(2) DEFAULT '1' COMMENT '1.未发放，2.已发放',
  `addtime` datetime DEFAULT NULL COMMENT '添加时间',
  `type` int(2) DEFAULT NULL COMMENT '类型',
  `Referee` varchar(50) DEFAULT NULL COMMENT '上级',
  `recycle` int(2) DEFAULT '0' COMMENT '0 不回收  1.回收',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='分销佣金明细表';


CREATE TABLE `sms_sign_activity` (
  `id` bigint(11) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `image` char(30) NOT NULL DEFAULT '' COMMENT '图片',
  `starttime` char(20) NOT NULL DEFAULT '' COMMENT '签到活动开始时间',
  `endtime` char(20) NOT NULL DEFAULT '' COMMENT '签到活动结束时间',
  `detail` text COMMENT '签到活动详情',
  `add_time` timestamp NULL DEFAULT NULL COMMENT '修改时间',
  `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '状态 0：未启用 1：启用 2：已结束',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='签到活动';

CREATE TABLE `sms_share` (
  `id` bigint(11) unsigned NOT NULL COMMENT 'id',
  `user_id` char(15) DEFAULT NULL COMMENT '用户id',
  `wx_id` varchar(50) DEFAULT NULL COMMENT '微信id',
  `wx_name` varchar(150) DEFAULT NULL COMMENT '微信昵称',
  `sex` int(11) DEFAULT NULL COMMENT '性别 0:未知 1:男 2:女',
  `type` tinyint(4) NOT NULL DEFAULT '0' COMMENT '类别 0：新闻 1：文章',
  `Article_id` bigint(11) NOT NULL DEFAULT '0' COMMENT '新闻id',
  `share_add` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '分享时间',
  `coupon` decimal(12,2) NOT NULL DEFAULT '0.00' COMMENT '礼券',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='分享列表';

CREATE TABLE `sms_sign_config` (
  `id` bigint(11) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `plug_ins_id` bigint(11) NOT NULL DEFAULT '0' COMMENT '插件id',
  `imgurl` varchar(200) NOT NULL COMMENT '图片',
  `min_score` int(11) NOT NULL DEFAULT '0' COMMENT '领取的最少积分',
  `max_score` int(11) NOT NULL DEFAULT '0' COMMENT '领取的最大积分',
  `continuity_three` int(11) NOT NULL DEFAULT '0' COMMENT '连续签到7天',
  `continuity_twenty` int(11) NOT NULL DEFAULT '0' COMMENT '连续签到20天',
  `continuity_thirty` int(11) NOT NULL DEFAULT '0' COMMENT '连续签到30天',
  `activity_overdue` int(11) NOT NULL DEFAULT '0' COMMENT '活动过期删除时间',
  `modify_date` timestamp NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='签到配置表';

CREATE TABLE `sms_sign_record` (
  `bigint` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `user_id` bigint(15) NOT NULL COMMENT '用户ID',
  `sign_score` int(11) NOT NULL DEFAULT '0' COMMENT '签到积分',
  `record` char(20) DEFAULT NULL COMMENT '事件',
  `sign_time` timestamp NULL DEFAULT NULL COMMENT '签到时间',
  `type` int(4) NOT NULL DEFAULT '0' COMMENT '类型: 0:签到 1:消费 2:首次关注得积分 3:转积分给好友 4:好友转积分 5:系统扣除 6:系统充值 7:抽奖',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='签到记录';

CREATE TABLE `sms_system_message` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `senderid` bigint(30) NOT NULL COMMENT '发送人ID',
  `recipientid` bigint(30) NOT NULL COMMENT '接收人ID',
  `title` text COMMENT '标题',
  `content` text COMMENT '内容',
  `time` datetime DEFAULT NULL COMMENT '时间',
  `type` int(2) NOT NULL DEFAULT '1' COMMENT '1未读  2 已读',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='系统消息表';

