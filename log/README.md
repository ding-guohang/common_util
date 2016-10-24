--------------------------------------------------------------------------------
#### **项目信息**:
#### *日志小助手-wiki*:(http://wiki.corp.qunar.com/pages/viewpage.action?pageId=138710608)<br />
--------------------------------------------------------------------------------

### **Overview**

写了一些通用的小工具，小组件，增加开发效率，或者增加开发乐趣。
// 比较尴尬，v1写了个com.qunar.guohang的maven坐标

```
<groupId>com.qunar.flight</groupId>
<artifactId>common_util</artifactId>
<version>1.0.1-SNAPSHOT</version>
```


### **简述**

    更友好的迁移
        修改Logger的实例化方式即可
    更普遍的需求
        默认对日志的参数进行Json序列化
    更细致的把控
        @LogPerformance关注方法性能
    更牵强的拓展
        实现MethodFilter完成自定义性能监控、 实现ParamStrategy并手动注入完成日志参数的自定义处理