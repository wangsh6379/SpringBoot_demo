package com.ehcache.springboot.ehcache.service;

public interface EhcacheService {

    String query(String param);

    void delete(String param);

    String update(String param);
}
