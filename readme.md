## 简介
基于Spring MVC的Restful框架，用于快速实现Restful Api接口。

## 功能
### 1.0
* [支持组合接口](https://github.com/ismartx/summer/wiki/%E7%BB%84%E5%90%88%E6%8E%A5%E5%8F%A3)
* 接口认证（基于JWT）
* 接口缓存
* 接口Logging统一处理
* 接口Exception统一处理
* 接口版本控制
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

## License
Apache License V2

## Contributors
[Contributors](./CONTRIBUTORS.md)
