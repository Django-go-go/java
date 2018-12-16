package com.jkingone;

public class GenericDemo {
    private static class Test<T> {
        T t;
        static int i;
    }

    public static void main(String[] args) {
        Test<String> test = new Test<>();
        String t = test.t;
        System.out.println(Test.i);
    }
}
