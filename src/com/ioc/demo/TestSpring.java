package com.ioc.demo;

import com.ioc.framework.ApplicationContext;

public class TestSpring {
    public static void main(String[] args) {
        //实例化上下文
        ApplicationContext context = new ApplicationContext("/applicationContext.xml");
        //根据 id 从容器中获取 Computer 的 bean 对象
        Computer computer = context.getBean("computer");
        //运行主机
        computer.run();
    }
}
