## 简介
jrts是一个回归测试选择工具，以减少冗余测试，提高测试效率。
当前版本是一个demo版本，仅用于记录访问类信息，并输出到控制台


## 运行流程
1. 编译打包
 ```bash
 mvn -DskipTests package
 ```
2. 运行
```bash
java -javaagent:agent/target/jrts-agent-jar-with-dependencies.jar -cp sample\target\classes org.example.Sample
```
3. 结果
```bash
2020-06-03 10:29:17  INFO handleOnBefore methodClassName=org.example.Sample, targetClassname=null
start
2020-06-03 10:29:17  INFO handleOnStaticAccess ownerClassName=org.example.Container, fieldClassName=org.example.IService
2020-06-03 10:29:17  INFO handleOnBefore methodClassName=org.example.Container, targetClassname=null
2020-06-03 10:29:17  INFO handleOnBefore methodClassName=org.example.BaseService, targetClassname=org.example.ServiceImpl
2020-06-03 10:29:17  INFO handleOnBefore methodClassName=org.example.ServiceImpl, targetClassname=org.example.ServiceImpl
constructor
2020-06-03 10:29:17  INFO handleOnStaticAccess ownerClassName=org.example.Container, fieldClassName=org.example.IService
2020-06-03 10:29:17  INFO handleOnCallBefore classname=org.example.IService
2020-06-03 10:29:17  INFO handleOnBefore methodClassName=org.example.BaseService, targetClassname=org.example.ServiceImpl
run
2020-06-03 10:29:17  INFO handleOnCallBefore classname=org.example.BaseService
2020-06-03 10:29:17  INFO handleOnBefore methodClassName=org.example.ServiceImpl, targetClassname=org.example.ServiceImpl
hello jrts

```