package com.springboot.zookeeper.product.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;

/**
 * 节点上的服务
 */
@RestController
public class ProductController {
    @RequestMapping(value = "/product/id",method = RequestMethod.GET)
    public String  get(HttpServletRequest request){
        return "访问 product  服务端口："+ request.getLocalPort();
    }
 
}