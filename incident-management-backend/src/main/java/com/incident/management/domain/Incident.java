package com.incident.management.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

/**
 * 事件表
 * @TableName Incident
 */
@Data
@Entity
public class Incident implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 事件编码
     */
    private String code;

    /**
     * 事件名称
     */
    private String name;

    /**
     * 事件描述
     */
    private String description;

    /**
     * 事件类型
     */
    private Integer type;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 是否缓存
     */
    private String isCache;


}
