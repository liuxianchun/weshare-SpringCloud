# We Share（视频文件分享平台）

## 前言

> 这是本人学习了springcloud后的实践项目
## 说明

> 本项目前后端不完全分离，前端页面通过ajax请求访问后端。项目一共有system,userservice,fileservice,management,gateway等五个服务，服务间通过openfeign完成服务调用或是通过rabbitmq完成跨服务功能。面向外部访问的使用https保证安全性，服务内部通信使用http。使用redis作为分布式锁，尽可能重复操作。由于本项目重点在于微服务，所以功能点并不是很全面。

## 技术栈

- **前端：**`Vue`+`Element-ui`+`Axios`

- **后端：**`SpringBoot`+`MybatisPlus`+`SpringCloud`+`SpringCloudAlibaba`+`RabbitMQ`

- **数据库：**`Mysql`+`Redis`+`Elasticsearch`

## 功能模块

```
调试:设置knife4j.setting.enableDebug=true启用knife4j调试
访问路径:http(https)://localhost:{{port}}/doc.html
```

项目演示:     [huanming.top](huanming.top)

### system

这是用户访问的模块，用户访问的前端页面都在这个模块，通过openfeign调用其他服务。

### userservice

这是用户相关的服务，包括登录、注册、关注、收藏、投币等，用户的点赞、观看数等数据通过rabbitmq发送到fileservice并进行更新。

### fileservice

这是文件、投稿相关的服务，这个模块不对外直接开放，通过gateway路由以及虚拟路径来访问文件。并且，用户上传的文件投稿以及其他公共文件不在同一路径，只能通过服务调用的方式来进行下载。本服务通过elasticsearch进行高性能的搜索，通过定时任务、消息队列来非实时更新数据。

### management

这是本项目的管理模块，目前只实现了更新轮播图的功能。除了用户密码的校验外，还加上了ip限制，及一个账号只能在某一特定ip下才能访问。本服务为非必要模块，可以不启动。

### gateway

本服务用于访问文件服务的文件。

## 运行项目

- 设置main.js的src和api
- 在userservice配置自己邮件服务
- 启动elasticsearch,rabbitmq,redis,nacos等服务
- 配置fileservice文件的存储路径，将doc中的pfx文件放在指定目录开启https
- 启动各个微服务

**作者** [liuxianchun](https://github.com/liuxianchun)

2021年9月25日