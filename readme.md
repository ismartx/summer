## 简介
基于Spring MVC的Restful框架，用于快速实现Restful Api接口。

## 功能
### 1.0
* [支持组合接口](https://github.com/ismartx/summer/wiki/%E7%BB%84%E5%90%88%E6%8E%A5%E5%8F%A3)
* 基于 Jwt 的身份认证机制
* 基于 redis 简单封装了 session ，以存取 token 相关数据
* 添加了 @VerifyJwtRol 注解以校验身份
* 添加了 @EnableCache 注解实现了 Etage 缓存
* 包装 Request 和 Response 对象，实现了在 LoggingFilter 中输出请求与响应数据
* 在 WebAppExceptionAdvice 中实现了对常见 Exception 的处理
* 接口版本控制
* 支持分页数据

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
具体示例代码可以参考**[summer-demo](./summer-demo)**项目。

## License
Apache License V2
