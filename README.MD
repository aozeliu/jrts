## 简介
jrts是一个回归测试选择工具，以减少冗余测试，提高测试效率。
当前版本是一个demo版本，仅用于记录访问类信息，并输出到控制台


## 运行流程
1. 编译打包
 ```bash
 mvn -DskipTests package
 ```
2. 运行
 1. 在idea上, 点击sample模块下测试路径的org/example目录, 运行该目录下的两个测试, 第一次运行会生成一个运行配置.
 2. 接下来修改该配置, 添加JVM参数-javaagent:agent/target/jrts-agent-jar-with-dependencies.jar=junit4.
 3. 然后使用该配置再次运行, 运行完成后会在会项目根目录下的.jrts目录下生成每个测试类的依赖信息(org.example.AServiceTest.dpi和org.example.BServiceTest.dpi)
 4. 再次运行, 此时不会执行任何测试, 因为sample中的代码没有变动
 5. 如果修改sample中的代码, 比如修改AService中的代码, 再次编译sample, 然后运行测试, 此时只有AService运行, 而BService不运行

