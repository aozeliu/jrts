package org.example;

public class AService implements IService{

    String msg;

    public AService(String msg) {
        this.msg = msg;
        System.out.println("AService constructor");
    }


    @Override
    public void say() {
        System.out.println(msg);
    }

}
