package com.jkingone.size_of_object;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

import sun.misc.Unsafe;

public class ClassIntrospector {

    public static void main(String[] args) throws IllegalAccessException {
        final ClassIntrospector ci = new ClassIntrospector();

        ObjectInfo res;

        SizeOfObject[] sizeOfObjects = new SizeOfObject[10];
        for (int i = 0; i < sizeOfObjects.length; i++) {
            sizeOfObjects[i] = new SizeOfObject();
        }

        SizeOfObject sizeOfObject = new SizeOfObject();


//        System.out.println("Use Instrumentation ==> " + ObjectShallowSize.sizeOf(sizeOfObjects) + "\n");

        res = ci.introspect(sizeOfObject);
        System.out.println("Use UNSAFE ==> " + res.getDeepSize());
        System.out.println("Use UNSAFE ==> " + res.getSize());
        System.out.println(res);

    }

    /**
     * 测试类
     * 按照引用计算：32 byte
     * 按照实际占用：32 + 56 + [160]
     */
    private static class SizeOfObject {
        int mInt; // 4
        byte mByte; // 1
        char mChar;  // 2
        boolean mBoolean; //1

        /**
         * 引用大小：4字节
         * 如果分配了对象，则需要递归计算对象里数据的大小
         */
        People mPeople;

        /**
         * 按照引用计算为：4字节
         * 按照实际占用为：8(mark) + 4(对象指针) + 4(数组长度占4个字节) + 10(总共的对象数量) * 4(对象的引用)
         *                 + [如果分配了对象：16 * 10(总共的对象数量)] = 56 + [160]
         */
        Object[] mObjectArray = new Object[10];

        SizeOfObject() {
            for (int i = 0; i < mObjectArray.length; i++) {
                mObjectArray[i] = new Object();
            }
            mPeople = new People();
        }
    }

    /**
     * 测试类
     */
    private static class People {
        int age;
        String name;
    }

    private static final Unsafe UNSAFE;

    /** 对象标记大小*/
    public static final int OBJECT_MARK = 8;

    /** 对象指针的大小 */
    public static final int OBJECT_REF_SIZE;
    static {
        try {
            Field field = Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            UNSAFE = (Unsafe) field.get(null);

            // 可以通过Object[]数组得到oop指针究竟是压缩后的4个字节还是未压缩的8个字节
            OBJECT_REF_SIZE = UNSAFE.arrayIndexScale(Object[].class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /** 所有基本类型的大小 */
    private static final Map<Class<?>, Integer> PRIMITIVE_MAP;
    static {
        PRIMITIVE_MAP = new HashMap<>(10);
        PRIMITIVE_MAP.put(byte.class, 1);
        PRIMITIVE_MAP.put(char.class, 2);
        PRIMITIVE_MAP.put(int.class, 4);
        PRIMITIVE_MAP.put(long.class, 8);
        PRIMITIVE_MAP.put(float.class, 4);
        PRIMITIVE_MAP.put(double.class, 8);
        PRIMITIVE_MAP.put(boolean.class, 1);
    }

    /**
     * Get object information for any Java object. Do not pass primitives to
     * this method because they will boxed and the information you will get will
     * be related to a boxed version of your value.
     *
     * @param obj Object to introspect
     * @return Object info
     * @throws IllegalAccessException
     */
    public ObjectInfo introspect(final Object obj) throws IllegalAccessException {
        try {
            return introspect(obj, null);
        } finally { // clean visited cache before returning in order to make
            // this object reusable
            mVisited.clear();
        }
    }

    // we need to keep track of already visited objects in order to support
    // cycles in the object graphs
    private IdentityHashMap<Object, Boolean> mVisited = new IdentityHashMap<>(100);

    private ObjectInfo introspect(final Object obj, final Field fld) throws IllegalAccessException {
        // use Field type only if the field contains null. In this case we will
        // at least know what's expected to be
        // stored in this field. Otherwise, if a field has interface type, we
        // won't see what's really stored in it.
        // Besides, we should be careful about primitives, because they are
        // passed as boxed values in this method
        // (first arg is object) - for them we should still rely on the field
        // type.
        boolean isPrimitive = fld != null && fld.getType().isPrimitive();
        boolean isRecursive = false; // will be set to true if we have already
        // seen this object
        if (!isPrimitive) {
            if (mVisited.containsKey(obj))
                isRecursive = true;
            mVisited.put(obj, true);
        }

        final Class<?> type = (fld == null || (obj != null && !isPrimitive)) ? obj.getClass() : fld.getType();
        int arraySize = 0;
        int baseOffset = 0;
        int indexScale = 0;
        if (type.isArray() && obj != null) {
            baseOffset = UNSAFE.arrayBaseOffset(type);
            indexScale = UNSAFE.arrayIndexScale(type);
            arraySize = baseOffset + indexScale * Array.getLength(obj);
        }

        final ObjectInfo root;
        if (fld == null) {
            root = new ObjectInfo(type.getSimpleName(), type.getCanonicalName(),
                    getContents(obj, type), 0, getShallowSize(type),
                    arraySize, baseOffset, indexScale, false, true);
        } else {
            final int offset = (int) UNSAFE.objectFieldOffset(fld);
            root = new ObjectInfo(fld.getName(), type.getCanonicalName(),
                    getContents(obj, type), offset, getShallowSize(type),
                    arraySize, baseOffset, indexScale, isPrimitive, obj != null);
        }

        if (!isRecursive && obj != null) {
            if (isObjectArray(type)) {
                // introspect object arrays
                final Object[] ar = (Object[]) obj;
                for (final Object item : ar)
                    if (item != null) {
                        root.addChild(introspect(item, null));
                    }
            } else {
                for (final Field field : getAllFields(type)) {
                    if ((field.getModifiers() & Modifier.STATIC) != 0) {
                        continue;
                    }
                    field.setAccessible(true);
                    root.addChild(introspect(field.get(obj), field));
                }
            }
        }

        root.sort(); // sort by offset
        return root;
    }

    // get all fields for this class, including all superclasses fields
    private static List<Field> getAllFields(final Class<?> type) {
        if (type.isPrimitive())
            return Collections.emptyList();
        Class<?> cur = type;
        final List<Field> res = new ArrayList<>(10);
        while (true) {
            Collections.addAll(res, cur.getDeclaredFields());
            if (cur == Object.class)
                break;
            cur = cur.getSuperclass();
        }
        return res;
    }

    // check if it is an array of objects. I suspect there must be a more
    // API-friendly way to make this check.
    private static boolean isObjectArray(final Class<?> type) {
        if (!type.isArray())
            return false;
        if (type == byte[].class || type == boolean[].class
                || type == char[].class || type == short[].class
                || type == int[].class || type == long[].class
                || type == float[].class || type == double[].class)
            return false;
        return true;
    }

    // advanced toString logic
    private static String getContents(final Object val, final Class<?> type) {
        if (val == null)
            return "null";
        if (type.isArray()) {
            if (type == byte[].class)
                return Arrays.toString((byte[]) val);
            else if (type == boolean[].class)
                return Arrays.toString((boolean[]) val);
            else if (type == char[].class)
                return Arrays.toString((char[]) val);
            else if (type == short[].class)
                return Arrays.toString((short[]) val);
            else if (type == int[].class)
                return Arrays.toString((int[]) val);
            else if (type == long[].class)
                return Arrays.toString((long[]) val);
            else if (type == float[].class)
                return Arrays.toString((float[]) val);
            else if (type == double[].class)
                return Arrays.toString((double[]) val);
            else
                return Arrays.toString((Object[]) val);
        }
        return val.toString();
    }

    // obtain a shallow size of a field of given class (primitive or object
    // reference size)
    private static int getShallowSize(final Class<?> type) {
        if (type.isPrimitive()) {
            final Integer res = PRIMITIVE_MAP.get(type);
            return res != null ? res : 0;
        } else {
            return OBJECT_REF_SIZE;
        }
    }
}