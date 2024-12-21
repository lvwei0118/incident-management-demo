# 项目介绍

事件管理平台，功能包括事件的增、删、改、查等操作。

# 主要功能：
- 事件管理
  - 查询所有事件
  - 查询详细信息
  - 修改事件信息
  - 添加事件
  - 批量添加事件
  - 删除事件


# 技术架构：
-----------------------------------

## 后端

- IDE建议： IDEA (必须安装lombok插件)
- 语言：Java 17
- 依赖管理：Maven  （配置文件： settings.xml）
- 基础框架：Spring Boot 2.7
- - Spring（依赖注入框架，帮助你管理 Java 对象，集成一些其他的内容）
- - Spring MVC（web 框架，提供接口访问、restful接口等能力）
- - Spring Boot（快速启动 / 快速集成项目。不用自己管理 spring 配置，不用自己整合各种框架）
- 持久层框架：Spring Data JPA
- 默认数据库：H2
- 日志打印：logback
- 缓存：Redis 4.0.7
- 单元测试：jUnit
- 压力测试：apache-jmeter-5.4.1
- 其他：hutool, fastjson，jedis, lombok（简化代码）等。


## 前端

- 前端IDE建议：WebStorm、Vscode
- HTML + CSS + JavaScript三件套
- React开发框架（前端开发框架）
- Umi开发框架（对React的进一步封装）
- Ant Design组件库（便于前端开发的组件）
- 依赖管理：node、npm、pnpm


## 支持库

|  数据库   |  支持   |
| --- | --- |
|   HSQLDB   |  √   |
|   H2   |  √   |
|   MySQL   |  √   |
|  Oracle11g   |  √   |
|  Sqlserver2017   |  √   |
|  PostgreSQL   |  √   |


# 项目启动

## 前端

（需要下载node.js，npm或yarn）

环境要求：Node.js 版本推荐16-17

1.使用开发工具（VsCode或WebStorm等）打开前端项目文件

2.终端输入执行以下命令：

安装依赖：

```
npm install
```

启动：

```
npm start
```

部署（需要将项目部署到服务器中执行）：

```
npm build
```

前端访问地址：

```
[http://localhost:3000]
```

执行命令后会得到 dist 目录，可以放到自己的 web 服务器指定的路径；也可以使用 Docker 容器部署，将 dist、Dockerfile、docker 目录（文件）一起打包即可。

## 后端

使用IDEA开发工具打开项目文件并启动。

# 后台目录结构
-----------------------------------
```
项目结构
├─incident-management-backend（POM：项目依赖、modules组织）
│  ├─logs（日志）
│  ├─doc  文档
│  ├─src 
│  │  ├─main 
│  │  ├─java
│  │  │  ├─com.incident.management
│  │  │  ├─common   -- 通用工具类
         ├─config   --通用配置 
         ├─controller  -- 服务控制层
         ├─dao         -- 持久化层层
         ├─domain    -- 实体类
         ├─exception -- 通用异常配置
         ├─redis     -- redis缓存工具
         ├─service   -- 业务服务层
       ├─resources
         ├─application-* --系统配置文件
         ├─logback-spring   -- 日志配置文件
         
```

## 业务逻辑（后端）

### 事件添加（更新）

1、参数校验

\- 必填参数非空

\- 事件编码长度不可超过8个字符

\- 事件描述长度不超过50个字符

\- 事件编码不可重复

2、是否启用缓存

\- 如果启用缓存，先将数据插入Redis缓存

3、将数据保存到数据库中

### 事件批量添加（数据库）

1、创建线程池

2、代码中输入线程池参数

3、使用@Transactional管理事务。

4、分割添加的参数列表。

5、将数据插入数据库中。

6、完成后关闭线程池。

### 事件查询

1、根据Id查询具体事件

2、先从布隆过滤器中判断此id是否存在

\- 如果不存在，直接返回null值；

3、查询缓存数据

\- 如果缓存不存在，则使用redis设置一个锁

\- 然后查询数据库。如果数据存在则将查询到的数据加入缓存，并设置随机的过期时间避免缓存同时失效

\- 如果数据不存在，则在布隆过滤器中添加该请求的数据

4、释放锁。


## 功能测试：

### 单元测试：
    使用 JUnit 和 Postman 对服务层和控制器层进行单元测试。保证代码和接口的可用性。
### 联调测试：
    前端页面与后台交互测试。
### 压力测试：
    使用jmeter性能测试。

## Docker部署：

### 后端服务：
1、 修改配置文件application-dev(prod).yml,  redis:host: 修改为部署服务器ip

2、 后端代码打包。management-0.0.1-SNAPSHOT.jar

3、 服务器创建文件夹：mkdir /home/incd

4、 将jar包，Dockerfile，docker-compose.yaml,start.sh上传服务器文件夹/home/incd

5、 在文件夹下运行start.sh

6、 查看镜像 docker image

7、 查看容器的日志 docker logs -f inc


### 前端服务：
1、 修改nginx配置文件nginx.conf，server_name为服务器ip。

2、 前端代码打包。dist

3、 服务器创建文件夹：mkdir /home/incf

4、 将dist包，Dockerfile，nginx.conf,start.sh上传服务器文件夹/home/incf,

5、 在文件夹下运行start.sh。

6、 查看镜像 docker image

7、 查看容器的日志 docker logs -f inc-front

# 事件管理平台项目迭代升级2.0
1、扩展事件管理功能，增加其他事件项的管理。

2、整合数据库Oracle，MySQL做数据持久化，持久化框架更新为MyBatis的增强工具MyBatis-Plus简化开发。

3、集群化部署，启用redis集群。

4、作为分布式系统集成，整合Nginx、Dubbo、Nacos等微服务框架，并更新自增id为UUID或雪花算法ID实现幂等性，采用redission为分布式锁。

5、添加用户、角色等功能，采用shiro框架借助jwt来完成身份认证及登录，对事件进行更精细的管理。

6、优化并发性能，使用配置文件动态配置线程池参数，优化事务为手动提交，避免事务失效。

7、全局异常及日志优化。
