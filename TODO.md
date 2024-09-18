# TODO

    1) 文档                  OK
    2) 测试                  ING
    2) 数据库操作             MyBatisPlus, MyBatisPlusJoin, MyBatisPlusExt + Redis
    3) 代码自动生成
    4) 基础模块之系统模块搭建
    5) 

mybatis-plus的动态数据源DynamicRoutingDataSource 适用于springboot 单体服务
使用场景：SaaS服务部署，单服务多租户。
当一个系统中需要多个数据库参与，每个公司（租户）一个数据库，多个公司（租户）共用同一个域名同一个网站。
用户操作时用公司号登录，进行的操作都会查询指定公司的数据库。
```
<dependency>
  <groupId>com.baomidou</groupId>
  <artifactId>dynamic-datasource-spring-boot-starter</artifactId> <!-- 多数据源 -->
  <version>${dynamic-datasource.version}</version>
</dependency>
```