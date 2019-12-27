-- 1、分销员条件表
CREATE TABLE `dms_condition` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `type` tinyint(1) NOT NULL COMMENT '类型:1.购买指定商品,2.累计购满金额,3.累计购满订单数',
  `order_num` int(11) NOT NULL DEFAULT 0 COMMENT '订单数',
  `buy_money` decimal(10,2) NOT NULL DEFAULT 0 COMMENT '购满金额',
  `one_scale` decimal(10,2) NOT NULL DEFAULT 0 COMMENT '直属上级分佣比例',
  `two_scale` decimal(10,2) NOT NULL DEFAULT 0 COMMENT '间属上级分佣比例',
  `state` tinyint(1) NOT NULL COMMENT '状态',
  `add_user_id` bigint(20) NOT NULL COMMENT '添加人id',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='dms|分销|分销员条件表|liuyaxin|20191218';

-- 2、分销条件指定商品
CREATE TABLE `dms_condition_product` (
  `condition_id` bigint(20) NOT NULL COMMENT '分销条件ID',
  `product_id` bigint(20) NOT NULL COMMENT '商品ID',
  PRIMARY KEY  (`condition_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='dms|分销|分销条件指定商品表|liuyaxin|20191218';

-- 3、分销员记录表
CREATE TABLE `dms_distributor_record` (
  `member_id` bigint(20) NOT NULL COMMENT '会员ID',
  `entrance` tinyint(1) NOT NULL COMMENT '入口：1为前端，2位后台',
  `type` tinyint(1) NOT NULL COMMENT '类型：1为成为分销员，2为取消分销员',
  `state` tinyint(1) NOT NULL COMMENT '状态',
  `add_user_id` bigint(20) NOT NULL COMMENT '添加人id',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY  (`member_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='dms|分销|分销员记录表|liuyaxin|20191218';

-- 4、分销记录表
CREATE TABLE `dms_distribution_record` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `role_type` tinyint(1) NOT NULL COMMENT '分佣角色:1为分销员,2为提货点,3为运营商',
  `role_id` bigint(20) NOT NULL COMMENT '角色ID（提货点ID/运营商ID）',
  `condition_id` bigint(20) NOT NULL COMMENT '分销条件ID',
  `member_id` bigint(20) NOT NULL COMMENT '会员ID',
  `type` tinyint(1) NOT NULL COMMENT '类型:1为订单产生分佣',
  `business_id` bigint(20) NOT NULL COMMENT '业务ID,当前为订单ID',
  `amount` decimal(10,2) NOT NULL DEFAULT 0 COMMENT '分佣金额',
  `state` tinyint(1) NOT NULL COMMENT '状态',
  `add_user_id` bigint(20) NOT NULL COMMENT '添加人id',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='dms|分销|分销记录表|liuyaxin|20191218';

-- 5、分销商品记录表
CREATE TABLE `dms_distribution_product_record` (
  `distribution_record_id` bigint(20) NOT NULL COMMENT '分销记录ID',
  `product_id` bigint(20) NOT NULL COMMENT '商品ID',
  `product_num` int(11) NOT NULL COMMENT '商品数量',
  `product_amount` decimal(10,2) NOT NULL COMMENT '商品金额',
  `brokerage_amount` decimal(10,2) NOT NULL COMMENT '分佣金额',
  `pickup_point_amount` decimal(10,2) NOT NULL COMMENT '提货点金额',
  `state` tinyint(1) NOT NULL COMMENT '状态',
  PRIMARY KEY  (`distribution_record_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='dms|分销|分销商品记录表|liuyaxin|20191218';

-- 6、粉丝绑定关系表（编码需使用一位占位符，能更节省空间1_0，后面默认补0）
CREATE TABLE `dms_fans` (
  `member_id` bigint(20) NOT NULL COMMENT '会员ID',
  `parent_id` bigint(20) NOT NULL COMMENT '父类ID',
  `code` varchar(500) NOT NULL COMMENT '上下级编码,每一级以_为分隔符',
  `state` tinyint(1) NOT NULL COMMENT '状态',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY  (`member_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='dms|分销|粉丝关系表|liuyaxin|20191213';

-- 7、会员提货门店绑定记录表
CREATE TABLE `dms_pickup_point_bind` (
  `member_id` bigint(20) NOT NULL COMMENT '会员ID',
  `pickup_point_id` bigint(20) NOT NULL COMMENT '提货点ID',
  `state` tinyint(1) NOT NULL COMMENT '状态',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY  (`member_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='dms|分销|会员提货门店绑定记录表|liuyaxin|20191213';

-- 8、提货点表
CREATE TABLE `dms_pickup_point` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '提货点ID',
  `member_id` bigint(20) NOT NULL COMMENT '会员ID',
  `contact` varchar(30) NOT NULL COMMENT '联系人',
  `mobile` varchar(30) NOT NULL COMMENT '联系号码',
  `name` varchar(200) NOT NULL COMMENT '提货点名称',
  `region` varchar(12) NOT NULL COMMENT '行政区域编码',
  `address` varchar(100) NOT NULL COMMENT '详细地址',
  `x` double(12,8) NOT NULL DEFAULT '0' COMMENT '经度',
  `y` double(12,8) NOT NULL DEFAULT '0' COMMENT '纬度',
  `picture` varchar(255) NOT NULL COMMENT '提货点图片',
  `verify_id` bigint(20) NOT NULL COMMENT '审核ID',
  `verify_state` tinyint(1) NOT NULL COMMENT '审核状态:0为待审核,1为审核通过,2为审核未通过',
  `open_state` tinyint(1) NOT NULL DEFAULT 1 COMMENT '开业状态:1为开业，0为打烊',
  `state` tinyint(1) NOT NULL COMMENT '状态',
  `add_type` tinyint(1) NOT NULL COMMENT '1为前端添加，2位后台添加',
  `add_user_id` bigint(20) NOT NULL COMMENT '添加人id',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_user_id` bigint(20) NOT NULL COMMENT '修改人id',
  `update_time` datetime NOT NULL COMMENT '修改时间',
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='dms|分销|提货点表|liuyaxin|20191213';

-- 9、提货点审核表
CREATE TABLE `dms_pickup_point_verify` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `pickup_point_id` bigint(20) NOT NULL COMMENT '提货点ID',
  `verify_state` tinyint(1) NOT NULL COMMENT '审核状态:1为审核通过,2为审核未通过',
  `verify_desc` varchar(500) NOT NULL COMMENT '审核描述',
  `state` tinyint(1) NOT NULL COMMENT '状态',
  `add_user_id` bigint(20) NOT NULL COMMENT '添加人id',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='dms|分销|提货点审核表|liuyaxin|20191213';




-- 会员表新增提货点状态、分销员状态
alter table ums_member add column `pickup_point_state` tinyint(1) NOT NULL DEFAULT '0' COMMENT '提货点状态:0为非提货点,1为提货点,2为待审核,3为审核失败';
alter table ums_member add column `distributor_state` tinyint(1) NOT NULL DEFAULT '0' COMMENT '分销员状态:0为非分销员,1为分销员';

-- 提货点成立时间
alter table dms_pickup_point add column `establish_time` datetime NOT NULL COMMENT '提货点成立时间';

-- 分销条件新增修改时间与修改人
alter table dms_condition add column `update_user_id` bigint(20) NOT NULL COMMENT '修改人id' after create_time;
alter table dms_condition add column `update_time` datetime NOT NULL COMMENT '修改时间' after update_user_id;
