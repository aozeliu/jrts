package org.example;

public class ServiceImpl extends BaseService {

    String msg;

    public ServiceImpl(String msg) {
        this.msg = msg;
        System.out.println("constructor");
    }


    @Override
    public void say() {
        System.out.println(msg);
    }
}
