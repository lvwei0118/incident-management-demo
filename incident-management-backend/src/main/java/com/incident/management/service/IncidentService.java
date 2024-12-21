package com.incident.management.service;

import com.incident.management.domain.Incident;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Description: IncidentService
 * @Author: lvwei
 * @Date: 2024/12/20
 * @Version: V1.0
 */
public interface IncidentService {

    /**
     * 事件列表
     * @return 事件列表
     */
    Page<Incident> findAll(int pageNo, int pageSize, String sortBy);

    /**
     * 事件列表-名称模糊查询
     * @param name 事件名称
     * @return 事件列表
     */
    List<Incident> list(String name);

    /**
     * 事件查看
     *
     * @param id 事件名称
     * @return 事件信息
     */
    Incident findById(Long id);

    /**
     * 事件添加
     * @param data 事件
     * @return 事件信息
     */
    String save(Incident data);

    @Transactional(rollbackFor = Exception.class)
    String saveBatch(List<Incident> incList);

    /**
     * 事件删除
     * @param  id 事件id
     */
    void deleteById(Long id);
}
