# IocDemo
SpringIoCDemo

引入dom4j架包用于解析xml
主要步骤如下：
a. 自定义 framework 框架，其中三个 ApplicationConext，ClassBean 和 PropertyBean。三个类具体源码及作用后面会介绍。主要是容器框的核心，负责加载配置文件，实例化 Bean，注入 bean 依赖关系。

b. 定义 IUsb 接口, 相机类 Camera 和 手机类 Phone 分别实现 IUsb 接口。

c. 定义 TestSpring 测试类，用来测试自定义容器框架
