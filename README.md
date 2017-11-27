---
title: java高并发秒杀API_SSM
date: 2017-11-25
tags: [SSM, 项目md]
categories: [小项目]
---

#### 第2部分

##### 相关技术的介绍

###### mysql

- 表设计(手写代码的方式而不是第三方工具创建表)
- sql技巧(利用sql技巧解决一些实际的问题)
- 事务和行级锁

###### mybatis

数据访问层的主要工作
	
- dao层设计与开发
- mybatis的合理使用(mybatis提供了非常多的一些使用方式，比如基于注解和xml提供sql语句，和基于原生的api和接口访问我们的数据库)
- mybatis和spring的整合(如何高效的整合)

###### spring

spring ioc整合service以及service所有的依赖

spring的依赖注入提供了很多种使用方式,大概有：
	
- 基于xml的依赖注入
- 基于注解
- 基于java config
- spring ioc整合service
- 声明式事务的应用(spring声明式事务分析)

###### spring mvc

- restful接口设计和使用(restful现在更多的是构建在我们互联网web公司的框架中一些接口设计规范中，理解restful如何去设计和使用)
- springmvc框架如何运作和流程
- Controller开发技巧

###### 前端
- 交互设计
- bootstrap
- jquery

###### 高并发
- 高并发点和高并发分析(以及如何去找到高并发的瓶颈)
- 优化思路并实现

##### 项目创建和依赖

基于maven来创建项目

项目之前的说明：零开始，官网获取相关配置，maven创建项目

###### maven配置

- maven 命令创建项目

```
mvn archetype:generate -DgroupId=org.seckill -DartifactId=seckill -DarchetypeArtifactId=maven-archetype-webapp
```

* eclipse中引入maven项目
* 修改web.xml(tomcat中找web头文件)
* 目录配置main中new java .... src/test/java,test/resources
* pom.xml 修改junit的版本号(3.0版本的junit默认使用编程的方式4.0使用注解的方式运行junint)
* 补全项目依赖，具体看配置了

#### 第3部分

##### 秒杀业务分析

##### mysql实现秒杀的难点分析

多用户在用一时间对同一个商品进行秒杀的时候，产生的竞争(技术：事务+行级锁)

##### 实现那些秒杀功能

秒杀接口暴露 执行秒杀 相关查询

代码开发阶段：dao设计编码 service设计编码 web设计编码

#### dao层设计编码

##### mysql数据库设计与编码 

```
main/sql/schema.sql (seckill.seckill/ seckill.success_killed)
```
##### dao实体和接口编码

```
java/(package)org.seckill.dao+org.seckill.entity
```

- 实体

```
entity: Class Seckill/ Class SuccessKilled
```

变通就是：java实体其实是对应相应的数据表的，属性对应列，成功秒杀可能需要拿到seckill的实体，多对一：一个秒杀对应多个成功记录(在多方添加一方的entity，方便存取)

```
dao: interface SeckillDao(reduceNumber:减库存，查询接口：Seckill queryById, queryAll)/SuccessKilledDao
```

##### 基于mybatis实现dao接口

- 参数+sql=entity

- 怎么用：

sql文件写在哪里(xml中，注解提供sql)，如何实现已经设计好的dao接口(mqpper自动实现dao接口，api编程方式实现dao接口)

##### 使用mybatis实现dao接口编码

配置mybatis(结合管官方文档)resources/mybatis-config.xml

```
resources/mapper/SeckillDao.xml  SuccessKilledDao.xml(为dao接口提供sql语句配置)
```

##### SuccessKilledDao.xml

##### mybatis整合spring理论

```
resources/spring/spring-dao.xml(结合spring官方文档)
```

##### 单元测试

```
src/test/java src/test/resources
```

自动加载test类：eclips如何自动加载呢？利用junit

##### 各种dao接口测试

#### service层

开始之前：dao编码之后的思考：之前的工作其实并没有逻辑代码

dao层工作：接口设计+sql编写：好处：代码和sql的分离，方便review

package：

service：接口和实现类

exception：service所需要的异常

dto：数据传输层

##### new package

```
org/seckill/service:service
org/seckill/dto:
org/seckill/exceptin:
```

```
interface SeckillService{

List<Seckill> geiSeckillList():查询所有的秒杀记录

Seckill getById(seckillId):查询单个秒杀记录

Exposer exportSeckillUrl(seckillId)

/*秒杀开启就是输出秒杀接口地址，否则输出系统时间和秒杀时间，这个时候需要一个dto Exposer:用来暴露一个秒杀接口*/
class Exposer：这里有一个加密措施
}

SeckillExecution  executeSeckill(seckillId, userPjone, md5)
/*这时封装一个dto数据传输层类SeckillSExecution：
秒杀执行后结果：id+执行状态标识+秒杀成功对象
执行秒杀过程中会发现各种异常(都应该是运行期异常RuntimeException：spring声明式事务只接受运行期异常)*/

RepeateKillException:重复秒杀异常

SeckillCloseException
/*秒杀关闭异常：秒杀关闭了用户还执行秒杀,秒杀关闭的原因有很多*/

SeckillException:秒杀异常上面是子异常，是两种不同的异常
```

###### 设计业务接口：
	
设计业务接口站在使用者角度设计接口

###### 如何站在使用者设计接口：

- 方法定义粒度(非常明确的，像秒杀业务，肯定有一个接口执行秒杀，而不是关注在减库存之类的点上，友好方便)
- 参数：简练直接 
- 返回类型：return(return的类型一定要友好而不是一个map或者是entity)异常

上面的mysql的表列命名很重要

##### 实现SeckillService接口

```
service/impl/SeckillServiceImpl.java
```

实现四个接口：

使用枚举标识常量

##### 基于spring管理service依赖

###### Spring IOC功能理解(本质上通过IOC管理)：

- 对象工厂：IOC依赖注入：创建对象的过程，也就是对象工厂，还有一些其他的依赖(dao)

- 依赖管理：

- 达到一致的访问接口:

IOC理解

###### 业务对象依赖图：

```
SeckillService(依赖)-->SeckillDao + successKIllDao --> SqlSessionFactory-->DataSource(spring)
```

- 为什么要用IOC：
	
对象创建统一托管；规范的生命周期管理；灵活的依赖注入(多种依赖注入方式)；一致的获取对象方式(有了ioc容器后，可以从容器中get到已有的对象)

###### Spring-IOC：注入方式和场景：

- 注入方式：

xml(bean+命名空间)+注解+java配置类(通过代码控制对象创建逻辑的场景)

- 本项目IOC使用：
	
主要采用xml配置+配置了一个机制：pacakge-sacn(service去扫描service类)+Annotation注解

```
spring/spring-service.xml(放所有的service，扫描service包(子包)下所有使用注解的类型)
```

- 有哪些注解呢？

ioc容器拿到对象实例，注入service依赖

##### Spring 声明式事务：

###### 简单流程：

- 开启事务，修改sql-i，提交或者回滚事务

- 解脱事务代码

###### 声明式事务使用方式：

- 早期：ProxyFactoryBean+XML
- (发展)tx:aop命名空间(一次配置永久生效)
- 注解@Transactional (注解控制，在方法中添加注解)

###### 事务方法嵌套
	
体现在传播行为(propagation_required...)

###### 什么时候回滚事务：

spring声明式事务只有在抛出运行期异常RuntimeException，小心不当的try-catch

###### 声明式事务配置(spring-service.xml)：
	
注入数据库连接池 + 配置基于注解的声明式事务：默认使用注解来管理事务行为

###### 使用注解控制事务方法的优点：

- 开发团队达成一致，明确标注事务方法的编程风格

- 保证事务方法的执行时间尽可能短，不要穿插其他网络操作RPC/HTTP请求或者剥离到事务方法外部

- 不是所有的方法都需要事务，如果只有一条修改操作，只读才做不需要事务控制，可以了解mysql行级锁相关的文档

##### 集成测试service层

```
test/java/../SeckillServiceTest.java
```

配置logbakck

slf4j只是一套接口，真正实现是logback(查看官网)

```
main/resource/logback.xml
```