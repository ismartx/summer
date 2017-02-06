## 简介
基于Spring MVC的Restful框架，用于快速实现Restful Api接口。

## 功能
### v0.1
* 支持组合接口
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
    <version>0.1</version>
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
}
```
具体示例代码可以参考**summer-demo**项目。

## License
Apache License V2
