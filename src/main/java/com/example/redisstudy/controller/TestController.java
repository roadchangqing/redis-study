package com.example.redisstudy.controller;



import com.example.redisstudy.service.orderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.annotation.Id;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@Api(tags = "redis测试相关")
@RequestMapping(value = "/order")
public class TestController {


    @Resource
    public orderService orderService;


    @ApiOperation("添加订单")
    @PostMapping("/add")
    public String add(){
         orderService.add();
         return "添加成功";
    }

    @ApiOperation("按orderId查订单信息")
    @GetMapping("/{id}")
    public String add(@PathVariable Integer id){
        String s = orderService.get(id);
        System.out.println(s);
        return s;
    }

}
