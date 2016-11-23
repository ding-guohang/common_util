##Overview
写了一些通用的小工具，小组件，增加开发效率，或者增加开发乐趣。

```java
<groupId>com.ca.util</groupId>
<artifactId>common_util</artifactId>
<version>1.0.1-SNAPSHOT</version>
```

##简述

* 更友好的`迁移`
   * 修改Logger的实例化方式即可
* 更普遍的`需求`
   * 默认对日志的参数进行Json序列化
* 更细致的`把控`
   * @LogPerformance关注方法性能
* 更牵强的`拓展`
   * 实现MethodFilter完成自定义性能监控
   * 实现ParamStrategy并SPI注入完成日志参数的自定义处理

##使用方法
###迁移
*建议使用LogService，几乎与原始日志使用方式毫无区别，并支持更多自定义功能*
```java
// 原有代码
Logger log = LoggerFactory.getLogger(XXX.class);
Logger log = LoggerFactory.getLogger(getClass());
Logger log = LoggerFactory.getLogger("name");
// 简单迁移为
LogService log = LogService.create(XXX.class);
LogService log = LogService.create(getClass());
LogService log = LogService.create("name");
// 或者你喜欢更明显的代理语义
LogService log = LogService.create( ${yourLogger} );
```

*如果你执意使用LogUtil,来完成对特定日志的迁移*
```java
// 用info举例
LogUtil.info(@Nullable ${yourLogger}, String messageTemplate, Object... params);
```

###方法的性能监控
* 依赖于Spring，使用@LogPerformance对目标方法进行监控
* 注解支持使用 flow 和 finish 属性来完成对流程块的性能监控， 使用exclusion属性来完成对各种性能监控的Filter的屏蔽。
* 支持使用实现MethodFilter，并纳入Spring容器管理的方式，完成自定义性能监控的扩充。
* 更多详情看注释。


###日志参数解析

* 支持对日志参数进行自定义解析，例如对某些敏感字段进行加密。
  * 实现ParamStrategy
  * 在LogService实例化之前，执行LogConstants.addStrategy();


*毫无疑问，这种使用方式蠢透了，在真正确定有这方面的需求之后，我会更新它的使用方式。*

*好了，现在用SPI就可以了…… at 11-09*


###日志正确的行数问题
_引入 log-over-logback_
```java
方法一: 在logback.xml中，指定<encoder class="CAPatternLayout">
方法二: 在logback.xml中，指定
<conversionRule conversionWord="L"  converterClass="RealLineConverter" />
<conversionRule conversionWord="line"  converterClass="RealLineConverter" />
方法三: 手动修改defaultConverterMap //我很好奇怎么做，办到了请教我
  ```
  
## TODO
_考虑使用异步的方式处理参数、写日志……需要解决logback中的线程展示问题_<br />
_这样会不会太重了？ 这取决于我的定位是工具还是框架_<br />