package com.jkingone.concurrent;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.*;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.LockSupport;

public class Demo {

    public static void main(String[] args) {
        Demo demo = new Demo();

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                demo.returnTest("1");
            }
        });

        thread.start();

        try {
            Thread.sleep(100);
            System.out.println("++++++++");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                demo.returnTest("2");
            }
        });

        thread2.start();

        try {
            Thread.sleep(100);
            System.out.println("----------");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        thread2.interrupt();
    }



    public synchronized void returnTest(String name) {
        try {
            System.out.println(name);
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            System.out.println("interrupt " + name);
        }
        System.out.println(name + " close");
    }


    private void test5() {
        JK<String, Integer, Boolean> jk = new JK<>();
        Class<JK> jkClass = JK.class;
        try {
            Field field = jkClass.getDeclaredField("list");
            Type type = field.getGenericType();
            parseType(type);

//            TypeVariable<Class<JK>>[] typeVariables = jkClass.getTypeParameters();
//            for (TypeVariable typeVariable : typeVariables) {
//                for (TypeVariable ty : typeVariable.getGenericDeclaration().getTypeParameters()) {
//                    System.out.println("++  :  " + ty.getName());
//                }
//
//                System.out.println(typeVariable.getBounds().length);
//                System.out.println(typeVariable.getName());
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void accessFileDemo() {
        try {
            RandomAccessFile readFile = new RandomAccessFile("src/com/jkingone/stream", "rw");
            RandomAccessFile writeFile = new RandomAccessFile("src/com/jkingone/test", "rw");

            FileChannel writeChannel = writeFile.getChannel();
            FileChannel readChannel = readFile.getChannel();

            MappedByteBuffer readByteBuffer = readChannel.map(FileChannel.MapMode.READ_WRITE, 0, 1024);
            MappedByteBuffer writeByteBuffer = writeChannel.map(FileChannel.MapMode.READ_WRITE, 0, 1024);

            writeByteBuffer.put(readByteBuffer);

            writeChannel.close();
            readChannel.close();

            writeFile.close();
            readFile.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void fileChannelDemo() {
        try {
            RandomAccessFile readFile = new RandomAccessFile("src/com/jkingone/stream", "rw");
            RandomAccessFile writeFile = new RandomAccessFile("src/com/jkingone/test", "rw");
            FileChannel readChannel = readFile.getChannel();
            FileChannel writeChannel = writeFile.getChannel();

            readChannel.transferTo(0, readChannel.size(), writeChannel);

            writeChannel.close();
            readChannel.close();

            writeFile.close();
            readFile.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void parseType(Type type) throws IllegalAccessException, InstantiationException {
        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            System.out.println("parameterizedType " + parameterizedType);
            Type[] types = parameterizedType.getActualTypeArguments();
            for (Type ty : types) {
                System.out.println(ty);
            }
            System.out.println(parameterizedType.getOwnerType());
            Type rawType = parameterizedType.getRawType();
            if (rawType instanceof Class<?>) {
                Class clazz = (Class) rawType;
                System.out.println(clazz.newInstance());
            }
            System.out.println(parameterizedType.getRawType());
        } else if (type instanceof WildcardType) {
            WildcardType wildcardType = (WildcardType) type;
            System.out.println("wildcardType");
            for (Type ty : wildcardType.getLowerBounds()) {
                System.out.println(ty);
            }
            for (Type ty : wildcardType.getUpperBounds()) {
                System.out.println(ty);
            }
        } else if (type instanceof GenericArrayType) {
            GenericArrayType genericArrayType = (GenericArrayType) type;
            System.out.println("genericArrayType");
            System.out.println(genericArrayType.getGenericComponentType());
        } else if (type instanceof TypeVariable) {
            System.out.println("TypeVariable");
        } else {
            System.out.println("Type : " + type);
        }
    }

    private static class JK<T, S, Z> {
        private List<S> list;

        private Map<List<String>, Integer> map;

        public List<String> getList() {
            return null;
        }

        public T get() {
            return null;
        }
    }

    public static void test4() {
        ExecutorService service = Executors.newSingleThreadExecutor();
        FutureTask<String> futureTask = new FutureTask<>(() -> "call");
        service.submit(futureTask);
        try {
            System.out.println(futureTask.get());
            service.shutdown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }

    public static void test3() {
        ExecutorService service = Executors.newSingleThreadExecutor();
        Future<String> future = service.submit(() -> "call");
        for (int i = 0; i < 5; i++) {
            new Thread(() -> {
                try {
                    String result = future.get();
                    if (result != null) {
                        System.out.println(result);
                    }

                } catch (InterruptedException | ExecutionException e) {
                    System.out.println(e);
                }
            }).start();
        }

    }

    public static void test2() {
        ExecutorService service = Executors.newSingleThreadExecutor();
        ResultTask<String> future = new ResultTask<>(() -> "call");
        service.submit(future);
        for (int i = 0; i < 50; i++) {
            new Thread(() -> {
                try {
                    future.get();
                } catch (InterruptedException | ExecutionException e) {
                    System.out.println(e);
                }
            }).start();
        }
    }

    public static int test() {
        try {
            System.out.println("try");
            return 0;
//            throw new IllegalArgumentException("try");
        } catch (Exception e) {
            System.out.println(e);
            return 1;
        } finally {
            System.out.println("finally");
            return 1;
        }
    }
}
