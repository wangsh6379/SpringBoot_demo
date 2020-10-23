package com.springboot.swagger.controller;

import com.springboot.swagger.entity.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("swagger-ui")
@Api(value = "这是一个测试swagger的controller")
public class SwaggerController {

    //    paramType：表示参数放在哪个地方
//    header-->请求参数的获取：@RequestHeader(代码中接收注解)
//    query-->请求参数的获取：@RequestParam(代码中接收注解)
//    path（用于restful接口）-->请求参数的获取：@PathVariable(代码中接收注解)
//    body-->请求参数的获取：@RequestBody(代码中接收注解)
//    form（不常用）
    @GetMapping("hello")
    @ApiOperation(value = "这是第一个swagger-ui测试接口", notes = "这是详细说明测试接口的用途")
//    , paramType = "path",
    @ApiImplicitParam(name = "param", value = "参数", required = true, dataType = "String", paramType = "query")
    public String helloWorld(@RequestParam(required = true) String param) {
        System.out.println("请求的参数是" + param);
        return param;
    }

    /**
     * 传递JSON数据
     *
     * @param user
     * @return
     */
    @PostMapping("helloJsonWorld")
    @ApiOperation(value = "这是第一个swagger-ui测试JSON接口", notes = "这是详细说明测试JSON接口的用途")
    @ApiImplicitParam(name = "user", value = "参数", required = true, dataType = "User", paramType = "body")
    public User helloJsonWorld(@RequestBody User user) {
        System.out.println("请求的参数是" + user);
        return user;
    }
}
