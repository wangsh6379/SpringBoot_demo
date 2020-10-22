package com.ehcache.springboot.ehcache.service.impl;

import com.ehcache.springboot.ehcache.service.EhcacheService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;


@Service
public class EhcacheServiceImpl implements EhcacheService {

    @Override
    @Cacheable(value = "users", key = "'ehcache_'+#param")
    public String query(String param) {
        Long timestamp = System.currentTimeMillis();
        return timestamp.toString();
    }

    @Override
    @CacheEvict(value = "users",key = "'ehcache_'+#param")
    public void delete(String param) {
        //这里删除真实数据
    }

    @Override
    @CachePut(value = "users",key = "'ehcache_'+#param")
    public String update(String param) {
        Long timestamp = System.currentTimeMillis();
        //更新数据库数据，更新完毕后返回，即将数据自动更新缓存
        return timestamp.toString();
    }
}
