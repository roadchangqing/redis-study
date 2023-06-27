package com.example.redisstudy.controller;


import com.example.redisstudy.entities.Customer;
import com.example.redisstudy.service.CustomerSerivce;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.ExecutionException;

@Api(tags = "客户Customer接口+布隆过滤器讲解")
@RestController
@RequestMapping("/customer")
@Slf4j
public class CustomerController {

    @Resource
    private CustomerSerivce customerSerivce;

    @ApiOperation("数据库初始化2条Customer数据")
    @PostMapping("/add")
    public void addCustomer() {
        for (int i = 0; i < 2; i++) {
            Customer customer = new Customer();
            customer.setCname("customer" + i);
            customer.setAge(new Random().nextInt(30) + 1);
            customer.setPhone("1381111xxxx");
            customer.setSex((byte) new Random().nextInt(2));
            customer.setBirth(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
            customerSerivce.add(customer);
        }
    }

    @ApiOperation("单个用户查询，按customerid查用户信息")
    @GetMapping("/{customerId}")
    public Customer findCustomerById(@PathVariable int customerId) {
        return customerSerivce.select(customerId);
    }

    @ApiOperation("修改数据库的同时，清除旧的redis缓存，然后添加新的redis缓存")
    @PutMapping
    public Customer update(@RequestBody Customer customer) {
        return customerSerivce.update(customer);
    }

    @ApiOperation("删除数据库的同时，清除旧的redis缓存")
    @DeleteMapping("/{customerId}")
    public Customer delete(@PathVariable Integer customerId) {
        return customerSerivce.delete(customerId);
    }

    @ApiOperation("BloomFilter案例讲解")

    @GetMapping("/customerbloomfilter/{id}")
    public Customer findCustomerByIdWithBloomFilter(@PathVariable int id) throws ExecutionException, InterruptedException
    {
        return customerSerivce.findCustomerByIdWithBloomFilter(id);
    }
}

