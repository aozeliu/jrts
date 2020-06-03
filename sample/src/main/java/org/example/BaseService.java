package org.example;

public abstract class BaseService implements IService{

    public void run(){
        System.out.println("run");
        say();
    }
}
