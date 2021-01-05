
<p align="center">
  <a href="https://github.com/Yiuman/citrus">
   <img alt="citrus-logo" src="./logo.png" />
  </a>
</p>

<h1 align="center">Citrus: 低代码开发脚手架</h1>

<p  align="center">
 <a href="https://github.com/Yiuman/citrus/blob/master/LICENSE">
    <img alt="code style" src="https://img.shields.io/badge/license-Apache%20License%202.0-blue.svg">
  </a>
  <a href="http://spring.io/projects/spring-boot">
    <img alt="springboot-2.3.2" src="https://img.shields.io/badge/spring--boot-2.3.2-release.svg">
  </a>
  <a href="http://mp.baomidou.com">
    <img alt="mybatis-plus-3.4.1" src="https://img.shields.io/badge/mybatis--plus-3.4.1-blue.svg">
  </a>
</p>

### 项目简介

基于SpringBoot 2.3.2 + Mybatis-Plus + SpringSecurity + JWT 的前后分离后台管理系统

**前端仓库地址**：https://github.com/Yiuman/citrus-vuetify

[LiveDemo](http://42.192.95.146/#/login)



### 项目特性

1. 开箱即用，引入starter依赖后即可启动
2. 高效开发，只需要定义实体与库表，入口继承基类的Controller，即可完成基础的增删改查操作
3. 常用数据结构的封装与基础的CRUD实现（左右值预遍历树、普通树等）
4. 统一的认证入口，方便的安全认证扩展，可实现多种方式的认证，且支持表单与接口
5. 灵活的权限钩子，既可全局进行权限验证、亦可定义于类与方法，验证方式易与扩展
6. 细粒度的RBAC权限控制，可自定义验证方式，支持数据范围注入
7. 动态数据源+多数据源事务管理



### 项目结构

- `citrus-boot-starter` 项目自动配置相关
- `citrus-main` 项目的运行入口（体验开箱即用的快感）
- `citrus-security` 项目安全相关的代码，统一认证、验证码类型、鉴权、jwt等  [安全模块传送门](https://github.com/Yiuman/citrus/tree/master/doc/安全模块设计.md)
- `citrus-support` 项目通用支持相关的代码，通用Service层、Controller层，工具类、缓存、异常、注入、数据结构、动态数据源及相关扩展 [通用CRUD指南](https://github.com/Yiuman/citrus/tree/master/doc/通用CRUD指南.md)
- `citrus-system` 项目系统设计的主要实现  包含用户、角色、权限、资源、菜单、数据范围等模块的实现与处理，数据范围注入也在这里 [权限数据范围设计](https://github.com/Yiuman/citrus/tree/master/doc/权限设计.md)
- `citrus-workflow` 整合activiti7的工作流模块



### 如何使用

#### 方式一

 1. springboot项目中引入依赖

    ```xml
    <dependency>
      <groupId>com.github.yiuman</groupId>
      <artifactId>citrus-boot-starter</artifactId>
      <version>0.0.8</version>
    </dependency>
    ```

2. 下载 https://github.com/Yiuman/citrus/tree/master/sql  中的sql文件，创建你的数据并执行

3. 在`application.yml`中配置数据库及应用信息

    ```yml
    spring:
      datasource:
        driver-class-name: com.mysql.jdbc.Driver
        url: jdbc:mysql://localhost:3306/citrus?zeroDateTimeBehavior=convertToNull&characterEncoding=UTF-8
        username: root
        password: yiuman
    server:
      port: 8082
    
    mybatis-plus:
      configuration:
        #    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
        local-cache-scope: statement
    ```

4. 启动项目

#### 方式二

1. ```sh
   git clone https://github.com/Yiuman/citrus.git
   ```

2. 自行修改项目配置与代码

3. 启动项目













