--------------------------------------------------------------------------------
#### **项目信息**:
#### *日志小助手*<br />
--------------------------------------------------------------------------------

### **Overview**

写了一些通用的小工具，小组件，增加开发效率，或者增加开发乐趣。

`
<groupId>com.qunar.flight</groupId>
<artifactId>common_util</artifactId>
<version>1.0.1-SNAPSHOT</version>
`


### **简述**

    更友好的迁移
        修改Logger的实例化方式即可
    更普遍的需求
        默认对日志的参数进行Json序列化
    更细致的把控
        @LogPerformance关注方法性能
    更牵强的拓展
        实现MethodFilter完成自定义性能监控、 实现ParamStrategy并手动注入完成日志参数的自定义处理
        
        
#### **如何解决丢失日志正确的行数问题？**

_引入 log-over-logback_
方法一: 在logback.xml中，指定<encoder class="CAPatternLayout"> <br />
方法二: 在logback.xml中，指定<br />
  <conversionRule conversionWord="L"  converterClass="RealLineConverter" /> <br />
  <conversionRule conversionWord="line"  converterClass="RealLineConverter" /><br />
方法三: 手动修改defaultConverterMap //我很好奇怎么做，办到了请教我<br />


**TODO List**<br />
_考虑使用异步的方式处理参数、写日志……需要解决logback中的线程展示问题_<br />
_这样会不会太重了？ 这取决于我的定位是工具还是框架_<br />