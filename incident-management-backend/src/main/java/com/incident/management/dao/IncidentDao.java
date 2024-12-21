package com.incident.management.dao;

import com.incident.management.domain.Incident;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * @Description: IncidentDao
 * @Author: lvwei
 * @Date: 2024/12/20
 * @Version: V1.0
 */
public interface IncidentDao extends PagingAndSortingRepository<Incident, Long> {

    List<Incident> findByName(String name);

    Incident findByCode(String code);
}
