# start-spring-boot

## 第一次启动

- 会下载大量jar包，可以使用代理服务
```
vim .m2/settings.xml

<mirrors>
    <mirror>
        <id>nexus-aliyun</id>
        <mirrorOf>*</mirrorOf>
        <name>Nexus aliyun</name>
        <url>http://maven.aliyun.com/nexus/content/groups/public</url>
    </mirror>
</mirrors>
```
- 删除不需要的文件
```
.mvn
mvnw
mvnw.cmd
```

- 3种启动方式

```
第一种：通过idea运行application类

第二种：cd到项目目录下，使用命令 mvn spring-boot:run

第三种：cd到项目目录下，
mvn install 
cd target 
java -jar girl-0.0.1-SNAPSHOT.jar  --spring.profiles.active=prod

```

- 属性配置
```java
@Value
@Component
@ConfigurationProperties(prefix="")
```

## Controller的使用

- @Controller
```
处理http请求
```
- @RestController 

```
Spring4新加注解，原理返回json需要@ResponseBody配合@Controller

```
- @RequestMapping 
```
配置URL映射
```

- @PathVariable
```
获取url中的数据
```
- @RequestParam
```
获取请求参数值
```
- @GetMapping
```
组合注解
```

## 数据库操作

- 详见代码/girl

## 事务管理

- org.springframework.transaction.annotation.Transactional 相关方法添加@Transactional注解

## 配置git

- 在IDEA中设置Git
```
在File->Setting->Version Control->Git->Path to Git
executable选择你的git安装后的git.exe文件，然后点击Test，测试是否设置成功
```
- 在IDEA中设置GitHub
```
File->Setting->Version Control->GibHub
Host：github.com
Token：点击Create API Token，输入在github中注册的用户名和密码生成token

项目右键没有git选项：VCS->checkout from version control->github

第一次push，先pull，git pull origin master --allow-unrelated-histories
```

## 表单验证

```java
@Min(value = 18,message = "未成年禁止入内")
private Integer age;

@PostMapping(value = "/girls")
public Girl girlAdd(@Valid Girl girl, BindingResult bindingResult){
    if(bindingResult.hasErrors()){
        //没有验证通过
        System.out.println(bindingResult.getFieldError().getDefaultMessage());//错误信息
        return null;
    }

```

## AOP统一处理请求日志

```java
@Aspect
@Component
public class HttpAspect {
 
    private final static Logger logger = LoggerFactory.getLogger(HttpAspect.class);
 
    //该类下所有方法任何参数都拦截
    @Pointcut("execution(public * com.fangminx.controller.GirlController.*(..))")
    public void log(){
    }
 
    @Before("log()")
    public void doBefore(JoinPoint joinPoint) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        logger.info("url={}",request.getRequestURL());
        logger.info("method={}",request.getMethod());
        logger.info("ip={}",request.getRemoteAddr());
        //类方法
        logger.info("class_method={}",joinPoint.getSignature().getDeclaringTypeName()+"."+joinPoint.getSignature().getName());
        logger.info("args={}",joinPoint.getArgs());
    }
 
    @After("log()")
    public void doAfter(){
        logger.info("2222222222");
    }
 
    /**
     * 获得返回的结果
     * @param object
     */
    @AfterReturning(returning = "object",pointcut = "log()")
    public void doAfterReturning(Object object){
        logger.info("response={}",object);
    }
}

```

## 统一异常处理

- ResultEnum
```java
public enum ResultEnum {
    UNKONW_ERROR(-1,"未知错误"),
    SUCCESS(0,"成功"),
    PRIMARY_SCHOOL(100,"你可能还在上小学"),
    MIDDLE_SCHOOL(101,"你可能还在上初中"),
    ;

    private Integer code;

    private String msg;

    ResultEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

```

- GirlException

```java
public class GirlException extends RuntimeException {

    private Integer code;
    public GirlException(ResultEnum resultEnum){
        super(resultEnum.getMsg());//RuntimeException含有
        this.code = resultEnum.getCode();
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}

```

- ExceptionHandle
```java
/**
 * 它来处理异常
 */
@ControllerAdvice
public class ExceptionHandle {

    private final static Logger logger = LoggerFactory.getLogger(ExceptionHandle.class);

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Result handle(Exception e){
        if(e instanceof GirlException){
            GirlException girlException = (GirlException) e;
            return ResultUtil.error(girlException.getCode(),girlException.getMessage());
        }else{
            logger.error("【系统异常】 {}",e);
            return ResultUtil.error(-1,"未知错误");
        }
    }
}
```

- GirlService
```java
if(age < 10){
    throw new GirlException(ResultEnum.PRIMARY_SCHOOL);
}else if(age > 10 && age < 16){
    throw new GirlException(ResultEnum.MIDDLE_SCHOOL);
}
```

- ResultUtil
```java
public class ResultUtil {

    public static Result success(Object object){
        Result result = new Result();
        result.setCode(0);
        result.setMsg("成功");
        result.setData(object);
        return result;
    }

    public static Result success(){
        return success(null);
    }

    public static Result error(Integer code,String msg){
        Result result = new Result();
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }
}
```
