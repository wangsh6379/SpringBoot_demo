package com.ehcache.springboot.ehcache.controller;

import com.ehcache.springboot.ehcache.service.EhcacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("ehcache")
public class EhcacheController {
    @Autowired
    private EhcacheService ehcacheService;
    private static Logger logger = LoggerFactory.getLogger(EhcacheController.class);

    //      @Cacheable : Spring在每次执行前都会检查Cache中是否存在相同key的缓存元素，如果存在就不再执行该方法，而是直接从缓存中获取结果进行返回，否则才会执行并将返回结果存入指定的缓存中。
    //      @CacheEvict : 清除缓存。
    //      @CachePut : @CachePut也可以声明一个方法支持缓存功能。使用@CachePut标注的方法在执行前不会去检查缓存中是否存在之前执行过的结果，而是每次都会执行该方法，并将执行结果以键值对的形式存入指定的缓存中。

    /**
     * timeToIdleSeconds：缓存空闲状态为10S，在10秒内。有访问过的请求会继续延长缓存。如果10秒内没有请求则会失效。
     * timeToLiveSeconds：缓存最长时间为20S，指最长缓存时间是20S，如果超过则强行刷新缓存。
     */
    @RequestMapping("query")
    public void ehcacheQuery(@RequestParam String param) {
        String ret = ehcacheService.query(param);
        logger.info("当前最新缓存的数据是：" + ret);
    }

    /**
     * 刪除緩存
     */
    @RequestMapping("delete")
    public void ehcacheDelete(@RequestParam String param) {
        logger.info("删除缓存" + param);
        ehcacheService.delete(param);
    }

    /**
     * 更新緩存
     */
    @RequestMapping("update")
    public void ehcacheupdate(@RequestParam String param) {
        logger.info("更新缓存" + param);
        String params = ehcacheService.update(param);
        logger.info("更新缓存，当前最新缓存的数据是：" + params);
    }
}
