package org.smartx.summer.controller;

import com.alibaba.fastjson.JSONObject;

import org.smartx.summer.annotation.VerifyJwtRole;
import org.smartx.summer.bean.LoginDTO;
import org.smartx.summer.session.SessionManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by Ming on 2016/10/27.
 */
@RestController
public class HelloController {

    @Resource
    private SessionManager sessionManager;

    @GetMapping(value = "/")
    public String summer() {
        return "Hello Summer";
    }

    @VerifyJwtRole(roles = "user,admin", verifyJwt = true)
    @GetMapping(path = "/test-annotation")
    public ResponseEntity<?> version() {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(value = "/api/1")
    public ResponseEntity<?> delete() {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/test/{value}")
    public ResponseEntity<?> get2(@PathVariable("value") String value) {
        System.out.println("-----------" + value);
        return ResponseEntity.ok(value);
    }

    @PutMapping("api/post")
    public ResponseEntity<?> post(@RequestBody LoginDTO loginDTO, HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", 1);
        return ResponseEntity.ok(jsonObject);
    }

    @PutMapping("api/put")
    public ResponseEntity<?> put(@RequestBody LoginDTO loginDTO, HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", 1);
        return ResponseEntity.ok(jsonObject);
    }


    /*
    @GetMapping("allKey")
    public ResponseEntity<?> allKey() {
        TreeSet<String> treeSet = sessionManager.getKeysByPattern(SessionAndTokenConstants.SESSION_KEY_PREFIX);
        return ResponseEntity.ok(treeSet);
    }
    */
}
