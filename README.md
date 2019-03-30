# Teacher Assistant 教师助手
[![GitHub stars](https://img.shields.io/github/stars/itning/Ta.svg?style=social&label=Stars)](https://github.com/itning/Ta/stargazers)
[![GitHub forks](https://img.shields.io/github/forks/itning/Ta.svg?style=social&label=Fork)](https://github.com/itning/Ta/network/members)
[![GitHub watchers](https://img.shields.io/github/watchers/itning/Ta.svg?style=social&label=Watch)](https://github.com/itning/Ta/watchers)
[![GitHub followers](https://img.shields.io/github/followers/itning.svg?style=social&label=Follow)](https://github.com/itning?tab=followers)

[![GitHub issues](https://img.shields.io/github/issues/itning/Ta.svg)](https://github.com/itning/Ta/issues)
[![GitHub license](https://img.shields.io/github/license/itning/Ta.svg)](https://github.com/itning/Ta/blob/master/LICENSE)
[![GitHub last commit](https://img.shields.io/github/last-commit/itning/Ta.svg)](https://github.com/itning/Ta/commits)
[![GitHub release](https://img.shields.io/github/release/itning/Ta.svg)](https://github.com/itning/Ta/releases)
[![GitHub repo size in bytes](https://img.shields.io/github/repo-size/itning/Ta.svg)](https://github.com/itning/Ta)
[![HitCount](http://hits.dwyl.io/itning/Ta.svg)](http://hits.dwyl.io/itning/Ta)
[![language](https://img.shields.io/badge/language-JAVA-green.svg)](https://github.com/itning/Ta)

### 简介
> 教师助手（以下简称Ta）是一款能帮助教师管理学生信息，学生请假记录，请假批复的平台

> Ta为改善教师办公效率，实现办公自动化的目的而生

> Ta的功能目前在不断开发测试完善中

> 项目使用Spring Boot搭建,前端使用了Material Design风格的框架MDUI

目前已经实现:

1. 两种方式添加学生信息
     - 通过网页添加
     - 通过Excel文件批量导入
2. 删除学生信息
     - 通过复选框进行选择删除
3. 修改学生信息
     - 在学生详情页面进行修改学生信息
4. 根据班级信息查找该班所有学生信息
5. 批量下载学生信息到Excel文件
6. 历史请假信息显示
7. 条件搜索查询学生请假信息
     - 自由选择筛选条件
8. 通过网页添加学生请假信息
9. 增加班级信息
10. 删除班级信息
11. 登陆功能
12. 细颗粒度安全控制
    - 方法级别权限控制
13. 更改主题
14. 夜间模式
15. 多用户登陆支持

### 安装使用
- 项目使用jdk9编译，项目使用编译器为IntelliJ IDEA
- 要求最低jdk版本为9 （项目使用了jdk9新特性）
- 项目数据库目前使用的是mysql，更改数据库请在配置文件中修改（application-dev.yml和application-prod.yml [开发环境和生产环境]）
- 使用mysql用户名为root密码为kingston,修改也在配置文件中修改
- 项目使用ORM技术 无需创建数据库和表
- 启动
    ```
    java -jar xxx.jar
    ```
- 登陆用户名密码目前需要手动添加到数据库中
- user表建表语句
    ```
    SET FOREIGN_KEY_CHECKS=0;
    DROP TABLE IF EXISTS `user`;
    CREATE TABLE `user` (
      `id` varchar(255) NOT NULL,
      `name` varchar(255) NOT NULL,
      `password` varchar(255) NOT NULL,
      `role` varchar(255) NOT NULL,
      `tc` varchar(255) DEFAULT NULL,
      `tca` varchar(255) DEFAULT NULL,
      `username` varchar(255) NOT NULL,
      PRIMARY KEY (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
    ```
    其中role可填A/B/C 
    
    A为教师权限;B为班委权限;C为普通学生权限;
### 技术架构
#### 前端
- [MDUI](https://www.mdui.org/)
- [canvas-nest](https://github.com/hustcc/canvas-nest.js)
- [My97 DatePicker](http://www.my97.net/index.asp)
- [Jquery](https://jquery.org/)
#### 后端
- Spring Boot
- Spring JPA
- Spring Security
- Apache POI
### 预览

![i](https://github.com/itning/Ta/blob/master/pic/login.png).
![i](https://github.com/itning/Ta/blob/master/pic/show1.png)
![i](https://github.com/itning/Ta/blob/master/pic/show2.png)
![i](https://github.com/itning/Ta/blob/master/pic/show3.png)
![i](https://github.com/itning/Ta/blob/master/pic/show4.png)

### 开源协议
- [MIT](https://github.com/itning/Ta/blob/master/LICENSE)
