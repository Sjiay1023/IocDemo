package com.ioc.demo;

public class Camera implements IUsb{
    @Override
    public void useUSB() {
        System.out.println("********我是相机，我在使用USB传输照片！*********");
    }
}
