# writeSimpleSpringMvc
手写一个简单的springMvc框架

利用空余时间 手写一个简易的SpringMvc框架，实现一些常用的、基本的MVC框架功能
实现的功能:  
1.指定扫描路径 扫描路径下所有bean，并注册成beanDefinition；  
2.实现了Spring容器对bean的管理，bean属性的自动注入（暂未处理循环依赖问题）；  
3.实现SpringMvc HandlerMapping初始化；  
4.通过DispatcherServlet接收请求，后对请求的处理，页面的转发，@RequestBody注解的效果实现；  
5.实现调用Controller时基本参数类型和引用类型参数自动注入功能(采用Asm框架获取参数名列表)；  