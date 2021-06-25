package com.springboot.result.controller;

import com.springboot.result.common.lang.Result;
import com.springboot.result.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@RestController
public class HelloWorldController {

    public static final Logger logger = LoggerFactory.getLogger(HelloWorldController.class);

    /**
     * 针对没有body数据传输的条件，进行校验
     *
     * @param user
     * @param bindingResult
     * @return
     */
    @RequestMapping("/hello")
    public Result hello(@Validated User user, BindingResult bindingResult) {
        logger.info("user{}",user);
        if (bindingResult.hasErrors()) {
            return Result.fail(4001, bindingResult.getFieldError().getDefaultMessage());
        }
        return Result.succ(user);
    }

    /**
     * 针对有body数据传输的条件，进行校验
     *
     * @param user
     * @param bindingResult
     * @return
     */
    @RequestMapping("/body")
    public Result body(@Validated @RequestBody User user, BindingResult bindingResult) {
        logger.info("user{}",user);
        if (bindingResult.hasErrors()) {
            return Result.fail(4001, bindingResult.getFieldError().getDefaultMessage());
        }
        return Result.succ(user);
    }

}