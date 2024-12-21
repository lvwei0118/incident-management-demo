package com.incident.management.controller;

import com.incident.management.domain.Incident;
import com.incident.management.service.IncidentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description: IncidentController
 * @Author: lvwei
 * @Date: 2024/12/20
 * @Version: V1.0
 */
@RestController
@RequestMapping("api")
@Slf4j
public class IncidentController {
    @Resource
    private IncidentService incidentService;

    /**
     * 事件列表查询
     */
    @GetMapping("/list")
    public Page<Incident> list(@RequestParam(value = "page", defaultValue = "1") Integer page, @RequestParam(value ="size",defaultValue ="15") Integer size) {
        return incidentService.findAll(page,size,"id");
    }

    /**
     * 事件名称查询
     */
    @GetMapping("/findByName/{name}")
    public List<Incident> listByName(@PathVariable(value = "name") String name) {
        return incidentService.list(name);
    }

    /**
     * 事件查看
     */
    @GetMapping("/findById/{id}")
    public Incident findById(@PathVariable(value = "id") Long id) {
        return incidentService.findById(id);
    }

    /**
     * 事件保存
     */
    @PostMapping("/save")
    public String save(@RequestBody Incident incident) {
        return incidentService.save(incident);
    }

    /**
     * 事件批量保存
     */
    @PostMapping("/saveBatch")
    public String saveBatch(@RequestBody List<Incident> incList) {
        return incidentService.saveBatch(incList);
    }

    /**
     * 事件更新
     */
    @PutMapping("/update")
    public String update(@RequestBody Incident incident) {
        return incidentService.save(incident);
    }

    /**
     * 事件删除
     */
    @DeleteMapping("/deleteById/{id}")
    public void deleteById(@PathVariable(value = "id") Long id) {
        incidentService.deleteById(id);
    }


}
