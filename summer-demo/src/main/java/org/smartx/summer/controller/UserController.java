package org.smartx.summer.controller;

import org.apache.commons.io.IOUtils;
import org.smartx.summer.annotation.EnableCache;
import org.smartx.summer.bean.CombineRequest;
import org.smartx.summer.bean.Pageable;
import org.smartx.summer.bean.User;
import org.smartx.summer.bean.XUserAgent;
import org.smartx.summer.exception.BaseServiceException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import io.swagger.annotations.ApiOperation;

/**
 * Created by binglin on 2016/10/20.
 */

@RestController
@RequestMapping("/users")
public class UserController {

    @ApiOperation(value = "测试 EnableCache ", notes = "支持缓存")
    @GetMapping
    @EnableCache
    public User get() {
        User user = new User();
        user.setId(1);
        user.setName("binglin");
        user.setAge(18);
        return user;
    }


    @PostMapping
    public String post() {
        return "post";
    }

    @GetMapping(path = "bean", headers = "version=v2")
    public String get1(Integer tt, @RequestHeader("Range") Pageable pageable, String test) {
        System.out.println(pageable);
        return pageable.toString();
    }

    @GetMapping(path = "bean", headers = "version=v1")
    public String get3() {
        return "version=v1";
    }

    @GetMapping(path = "bean", headers = "version=10")
    public User get3(String version) {
        CombineRequest request = new CombineRequest();
        request.setUrl("test");
        return null;
    }

    @GetMapping(path = "bean", headers = "version=11")
    public User get4(@RequestHeader(name = "version", defaultValue = "11") String version) {
        CombineRequest request = new CombineRequest();
        request.setUrl("test");
        return null;
    }


    @GetMapping("{id:\\d+}")
    public String get2(@PathVariable Integer id) throws BaseServiceException {
        throw new BaseServiceException(111, "test exception handle");
    }

    @PostMapping("file")
    public String upload(@RequestParam("file") MultipartFile file, MultipartRequest multipartRequest) {
        System.out.println(file);
        DefaultMultipartHttpServletRequest request = (DefaultMultipartHttpServletRequest) multipartRequest;
        String fileName = request.getParameter("fileName");
        StringBuilder stringBuilder = new StringBuilder("fileName" + " : " + fileName);
        try {
            InputStream inputStream = file.getInputStream();
            InputStreamReader in = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(in);
            bufferedReader.lines().forEach(stringBuilder::append);
            IOUtils.closeQuietly(inputStream, in, bufferedReader);
        } catch (IOException e) {
            e.printStackTrace();
        }


        return stringBuilder.toString();
    }

    @GetMapping(path = "x-user-agent")
    public String xUserAgent(@RequestHeader("X-User-Agent") XUserAgent userAgent) {
        return userAgent.toString();
    }

    @ApiOperation(value = "测试分页", notes = "参考 default 传入相应参数")
    @GetMapping(path = "page")
    public ResponseEntity<?> page(@RequestHeader(value = "Range", defaultValue = "sort=name,order=desc;sort=age,order=asc;page=0,size=10") Pageable pageable) {
        return ResponseEntity.ok(pageable);
    }
}
