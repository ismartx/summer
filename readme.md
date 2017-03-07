## summer
[![License](https://img.shields.io/badge/license-Apache%202-4EB1BA.svg)](https://www.apache.org/licenses/LICENSE-2.0.html)
[![Build Status](https://travis-ci.org/ismartx/summer.svg?branch=master)](https://travis-ci.org/ismartx/summer)

## 简介
基于Spring MVC的Restful框架，用于快速实现Restful Api接口。

## 功能
### 1.0
* [支持组合接口](https://github.com/ismartx/summer/wiki/%E7%BB%84%E5%90%88%E6%8E%A5%E5%8F%A3)
* [接口认证（基于JWT）](https://github.com/ismartx/summer/wiki/%E6%8E%A5%E5%8F%A3%E8%AE%A4%E8%AF%81)
* [接口缓存](https://github.com/ismartx/summer/wiki/%E6%8E%A5%E5%8F%A3%E7%BC%93%E5%AD%98)
* [接口Logging统一处理](https://github.com/ismartx/summer/wiki/Logging)
* [接口Exception统一处理](https://github.com/ismartx/summer/wiki/API-Exception)
* [接口版本控制](https://github.com/ismartx/summer/wiki/API%E7%89%88%E6%9C%AC%E6%8E%A7%E5%88%B6)
* 支持请求数据分页

## 使用
### maven
```
<dependency>
    <groupId>org.smartx</groupId>
    <artifactId>summer-core</artifactId>
    <version>1.0</version>
</dependency>
```

## Demo
```
@RestController
public class HelloController {

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
}
```
具体示例代码可以参考[summer-demo](./summer-demo)项目。

## Contributors
[Contributors](./CONTRIBUTORS.md)
