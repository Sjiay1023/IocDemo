package com.ioc.demo;

public class Phone implements IUsb{
    //手机品牌
    private String brand;
    @Override
    public void useUSB() {
        System.out.println("******我是"+brand+"手机，我在使用USB充电！*******");
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }
}
