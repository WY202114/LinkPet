-- 创建数据库
CREATE DATABASE IF NOT EXISTS linkpet DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE linkpet;

-- 用户表
CREATE TABLE IF NOT EXISTS user (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
    password VARCHAR(100) NOT NULL COMMENT '密码',
    nickname VARCHAR(50) COMMENT '昵称',
    avatar VARCHAR(255) COMMENT '头像URL',
    phone VARCHAR(20) COMMENT '手机号',
    role VARCHAR(20) DEFAULT 'ROLE_USER' COMMENT '角色: ROLE_USER, ROLE_ADMIN',
    status INT DEFAULT 1 COMMENT '状态: 0-禁用, 1-正常',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 插入一个默认管理员 (密码是 123456，假设没有做特殊加密注册逻辑，但 security 这里做了 BCrypt。
-- 这里提供的密码是 123456 经过 BCrypt 后的密文)
INSERT IGNORE INTO user (username, password, nickname, role) VALUES 
('admin', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '超级管理员', 'ROLE_ADMIN');

-- 宠物分类表
CREATE TABLE IF NOT EXISTS pet_type (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL COMMENT '分类名称: 狗, 猫等',
    sort INT DEFAULT 0 COMMENT '排序',
    status INT DEFAULT 1 COMMENT '状态'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='宠物分类表';

-- 插入默认宠物分类
INSERT IGNORE INTO pet_type (name, sort) VALUES 
('猫', 1), ('狗', 2), ('兔子', 3), ('鸟类', 4), ('其他', 99);

-- 宠物表
CREATE TABLE IF NOT EXISTS pet (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL COMMENT '宠物名称',
    type_id BIGINT NOT NULL COMMENT '分类ID',
    age VARCHAR(20) COMMENT '年龄',
    gender INT COMMENT '性别: 0-母, 1-公, 2-未知',
    description TEXT COMMENT '描述',
    images JSON COMMENT '宠物图片',
    address VARCHAR(255) COMMENT '所在地址',
    location VARCHAR(100) COMMENT '发现地点',
    status INT DEFAULT 1 COMMENT '状态: 1-待领养, 2-已领养, 3-已下架',
    user_id BIGINT NOT NULL COMMENT '发布人ID',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='宠物表';

-- 领养申请表
CREATE TABLE IF NOT EXISTS adoption_apply (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    pet_id BIGINT NOT NULL COMMENT '宠物ID',
    user_id BIGINT NOT NULL COMMENT '申请人ID',
    experience VARCHAR(255) COMMENT '养宠经验',
    home_condition VARCHAR(255) COMMENT '家庭条件',
    contact VARCHAR(50) COMMENT '联系方式',
    status INT DEFAULT 0 COMMENT '状态: 0-待审核, 1-通过, 2-拒绝',
    review_comment VARCHAR(255) COMMENT '审核意见',
    review_user_id BIGINT COMMENT '审核人ID',
    review_time DATETIME COMMENT '审核时间',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '申请时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='领养申请表';

-- 帖子表
CREATE TABLE IF NOT EXISTS post (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL COMMENT '发帖人ID',
    title VARCHAR(100) NOT NULL COMMENT '标题',
    content TEXT NOT NULL COMMENT '内容',
    images JSON COMMENT '配图',
    like_count INT DEFAULT 0 COMMENT '点赞数',
    comment_count INT DEFAULT 0 COMMENT '评论数',
    status INT DEFAULT 1 COMMENT '状态: 1-正常, 0-封禁',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '发布时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='社区帖子表';

-- 评论表
CREATE TABLE IF NOT EXISTS comment (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    post_id BIGINT NOT NULL COMMENT '帖子ID',
    user_id BIGINT NOT NULL COMMENT '评论人ID',
    parent_id BIGINT DEFAULT 0 COMMENT '父评论ID(0为一级评论)',
    content VARCHAR(500) NOT NULL COMMENT '评论内容',
    like_count INT DEFAULT 0 COMMENT '点赞数',
    status INT DEFAULT 1 COMMENT '状态',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '评论时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='帖子评论表';

-- 用户互动表 (点赞/收藏等)
CREATE TABLE IF NOT EXISTS user_action (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL COMMENT '用户ID',
    target_type INT NOT NULL COMMENT '目标类型: 1-帖子, 2-评论',
    target_id BIGINT NOT NULL COMMENT '目标ID',
    action_type INT NOT NULL COMMENT '动作类型: 1-点赞, 2-收藏',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
    UNIQUE KEY uk_user_action (user_id, target_type, target_id, action_type)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户互动表';
