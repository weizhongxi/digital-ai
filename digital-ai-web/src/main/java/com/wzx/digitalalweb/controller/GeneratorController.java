package com.wzx.digitalalweb.controller;

import com.wzx.digitalalweb.service.GeneratorService;
import com.wzx.digitalalweb.util.PageUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/generator")
@Api(tags = "系统：代码生成管理")
public class GeneratorController {

    @Autowired
    private GeneratorService generatorService;

    @ApiOperation("查询数据库数据")
    @GetMapping(value = "/tables/all")
    public ResponseEntity<Object> queryTables(){
        return new ResponseEntity<>(generatorService.getTables(), HttpStatus.OK);
    }

    @ApiOperation("查询数据库数据")
    @GetMapping(value = "/tables")
    public ResponseEntity<Object> queryTables(@RequestParam(defaultValue = "") String name,
                                              @RequestParam(defaultValue = "0")Integer page,
                                              @RequestParam(defaultValue = "10")Integer size){
        int[] startEnd = PageUtil.transToStartEnd(page, size);
        return new ResponseEntity<>(generatorService.getTables(name,startEnd), HttpStatus.OK);
    }

    @ApiOperation("查询数据库表数据")
    @GetMapping(value = "/queryTableInfo")
    public ResponseEntity<Object> queryTableInfo(@RequestParam(defaultValue = "") String name){
        return new ResponseEntity<>(generatorService.query(name), HttpStatus.OK);
    }

    @ApiOperation("查询数据库表数据")
    @GetMapping(value = "/genTableInfo")
    public ResponseEntity<Object> genTableInfo(@RequestParam(defaultValue = "") String name){
        return new ResponseEntity<>(generatorService.genInfo(name), HttpStatus.OK);
    }

}
