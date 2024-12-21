package com.incident.management.service.impl;

import cn.hutool.core.thread.NamedThreadFactory;
import cn.hutool.core.util.ObjectUtil;
import com.google.common.collect.Lists;
import com.incident.management.Redis.BloomFilterUtil;
import com.incident.management.Redis.RedisUtil;
import com.incident.management.common.ErrorCode;
import com.incident.management.dao.IncidentDao;
import com.incident.management.domain.Incident;
import com.incident.management.exception.BusinessException;
import com.incident.management.service.IncidentService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Description: IncidentServiceImpl
 * @Author: lvwei
 * @Date: 2024/12/20
 * @Version: V1.0
 */
@Slf4j
@Service
public class IncidentServiceImpl implements IncidentService {

    @Resource
    private IncidentDao incidentDao;

    @Resource
    private RedisTemplate redisTemplate;
    @Autowired
    private RedisUtil redisUtil;

    /**
     * 事件列表
     * @return 事件列表
     */
    @Override
    public Page<Incident> findAll(int pageNo, int pageSize, String sortBy) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        return incidentDao.findAll(pageable);
    }

    /**
     * 事件列表-名称模糊查询
     * @param name 事件名称
     * @return 事件列表
     */
    @Override
    public List<Incident> list(String name) {
        //先从缓存中获取数据，如果有就直接返回
        //如果缓存里没有数据，则查询数据库，并将数据设置到缓存中去
        Object incObj = redisUtil.get("inc:"+name);
        if(incObj==null){
            //查询数据库
            log.info("——————》查询数据库");
            List<Incident> incidents = incidentDao.findByName(name);
            log.info("——————》{}插入缓存",name);
            redisUtil.set("inc:"+name,incidents);
            return incidents;
        }
        return (List<Incident>) incObj;
    }

    /**
     * 事件查看
     *
     * @param id 事件名称
     * @return 事件信息
     */
    @Override
    public Incident findById(Long id) {
        // 先从布隆过滤器中判断此id是否存在
        Boolean flag = BloomFilterUtil.mightContain(id.toString());
        if(flag){
            log.info("----》不存在ID为{}的数据",id);
            return null;
        }
        // 查询缓存数据
        String key = "inc:"+id;
        Incident incident = (Incident) redisUtil.get(key);
        if(incident == null){
            //查询数据库
            log.info("——————》查询数据库：id为{}",id);
            // 添加锁
            String lockKey = "lock_inc_"+id;
            String lockValue = UUID.randomUUID().toString();
            try{
                //用redis设置一个锁
                Boolean lockResult = redisUtil.set(lockKey, lockValue, 60);
                if(lockResult != null && lockResult){
                    // 查询数据库
                    incident = incidentDao.findById(id).orElse(null);
                    if(incident != null){
                        // 设置随机的过期时间
                        long expireTime = new Random().nextInt(300) + 600;
                        // 将查询到的数据加入缓存，并避免缓存同时失效
                        redisUtil.set(key, incident, expireTime);
                    }else {
                        // 查询结果为空，将请求记录下来，并在布隆过滤器中添加
                        BloomFilterUtil.add(id.toString());
                    }
                }
            }finally{
                // 释放锁
                if(lockValue.equals(redisTemplate.opsForValue().get(lockKey))){
                    redisTemplate.delete(lockKey);
                }
            }
        }

        return incident;
    }

    /**
     * 事件添加
     * @param incident 事件
     * @return 事件信息
     */
    @Override
    public String save(Incident incident) {
        //参数校验
        validator(incident);
        incident.setCreateTime(new Date());
        incident.setCreateTime(new Date());
        String isCache = incident.getIsCache();
        Long id = incident.getId();
        //如果启用缓存
        if("0".equals(isCache)){
            //先将数据插入缓存
            log.info("{}插入缓存",id);
            redisUtil.set("inc:"+id,incident);
        }
        //数据插入数据库
        Incident result = incidentDao.save(incident);
        if(result != null) {
            return "success";
        } else {
            return "error";
        }
    }

    /**
     * 批量事件添加
     * @param incList 事件
     * @return 事件信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String saveBatch(List<Incident> incList) {
        String finish;
        //手动创建线程池。
        ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(
                5,
                5,
                30,
                TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(10),
                //设置守护线程，true主线程结束新的线程就不会继续工作
                new NamedThreadFactory("执行线程", false),
                (r, executor) -> System.out.println("拒绝" + r));
        //拆分list
        List<List<Incident>> partition = Lists.partition(incList, 100);
        //使用CountDownLatch保证所有线程执行完成
        CountDownLatch latch = new CountDownLatch(5);
        //多线程批量插入数据
        partition.forEach(item -> {
            poolExecutor.execute(() -> {
                incidentDao.saveAll(item);
                latch.countDown();
            });
        });
        try {
            latch.await();
            finish = "success";
        } catch (InterruptedException e) {
            finish = "error";
            throw new RuntimeException(e);
        }
        //关闭线程池
        poolExecutor.shutdown();
        return finish;
    }

    /**
     * 事件删除
     * @param  id 事件id
     */
    @Override
    public void deleteById(Long id) {
        Object empObj = redisUtil.get("inc:"+id);
        if(empObj==null){
            log.info("——————》{}不用删除缓存",id);
        } else {
            log.info("——————》{}删除缓存",id);
            redisUtil.delete("inc:"+id);
        }
        log.info("——————》删除数据库");
        incidentDao.deleteById(id);
    }


    /**
     * 参数校验
     * @param  incident 事件
     */
    public void validator(Incident incident){
        //1.校验
        String code = incident.getCode();
        String name = incident.getName();
        String desc = incident.getDescription();
        if(StringUtils.isAnyBlank(code,name)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"必填参数为空");
        }
        if(code.length() > 8){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"事件编码长度不可超过8个字符");

        }
        if(StringUtils.isNotBlank(desc) && desc.length() > 50){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"事件描述长度超过50字");
        }
        //先查找缓存中是否存在该事件
        Object incObj = redisUtil.get("inc:"+code);
        if(ObjectUtil.isNotNull(incObj)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"事件编码重复");
        }else {
            //再查找数据库中是否已存在该事件编码
            Incident inc = incidentDao.findByCode(code);
            if (ObjectUtil.isNotNull(inc)){
                throw new BusinessException(ErrorCode.PARAMS_ERROR,"事件编码重复");
            }
        }

    }
}




