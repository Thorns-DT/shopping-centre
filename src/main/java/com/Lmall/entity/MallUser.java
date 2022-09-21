/**
 * 严肃声明：
 * 开源版本请务必保留此注释头信息，若删除我方将保留所有法律责任追究！
 * 本软件已申请软件著作权，受国家版权局知识产权以及国家计算机软件著作权保护！
 * 可正常分享和学习源码，不得用于违法犯罪活动，违者必究！
 * Copyright (c) 2020 楼楼商城 all rights reserved.
 * 版权所有，侵权必究！
 */
package com.Lmall.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
@TableName("tb_mall_user")
public class MallUser {
    @TableId(type = IdType.INPUT)
    @TableField("user_id")
    private Long userId;
    @TableField("nick_name")
    private String nickName;
    @TableField("login_name")
    private String loginName;
    @TableField("password_md5")
    private String passwordMd5;
    @TableField("introduce_sign")
    private String introduceSign;
    @TableField("is_deleted")
    private Byte isDeleted;
    @TableField("locked_flag")
    private Byte lockedFlag;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
}