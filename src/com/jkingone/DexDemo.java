package com.jkingone;

public class DexDemo implements DexInterface {
    public String name;
    private String age = "10";
    private int height;

    public static final int STATIC = 1;

    public DexDemo(String name, String age, int height) {
        this.name = name;
        this.age = age;
        this.height = height;
    }

    private int getHeight() {
        return height;
    }

    public void setName(String name) {

    }

    public static void main(String[] args) {

    }

    @Override
    public void doSomething(String arg) {

    }
}