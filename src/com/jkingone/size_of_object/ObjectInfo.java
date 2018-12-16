package com.jkingone.size_of_object;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 *
 * 单个对象 = 8个字节对象头(mark) + 4/8字节对象指针 + 数据区 + padding内存对齐(按照8的倍数对齐)
 * 数组对象 = 8个字节对象头(mark) + 4/8字节对象指针 + 4字节数组长度 + 数据区 + padding内存对齐(按照8的倍数对齐)
 *
 * 对象指针究竟是4字节还是8字节要看是否开启指针压缩
 *
 * HotSpot创建的对象的字段会先按照给定顺序排列,默认的顺序为：
 * 从长到短排列，引用排最后: long/double –> int/float –> short/char –> byte/boolean –> Reference。
 */
public class ObjectInfo {
    /**
     * Field name
     */
    public final String name;
    /**
     * Field type name
     */
    public final String type;
    /**
     * Field data formatted as string
     */
    public final String contents;
    /**
     * Field offset from the start of parent object
     */
    public final int offset;
    /**
     * Memory occupied by this field
     */
    public final int length;
    /**
     * Offset of the first cell in the array
     */
    public final int arrayBase;
    /**
     * Size of a cell in the array
     */
    public final int arrayElementSize;
    /**
     * Memory occupied by underlying array (shallow), if this is array type
     */
    public final int arraySize;

    /**
     * This object fields
     */
    public final List<ObjectInfo> children;

    private boolean isPrimitive;

    private boolean isInit;


    public ObjectInfo(String name, String type, String contents, int offset, int length, int arraySize,
                      int arrayBase, int arrayElementSize, boolean isPrimitive, boolean isInit) {
        this.name = name;
        this.type = type;
        this.contents = contents;
        this.offset = offset;
        this.length = length;
        this.arraySize = arraySize;
        this.arrayBase = arrayBase;
        this.arrayElementSize = arrayElementSize;
        this.isPrimitive = isPrimitive;
        this.isInit = isInit;
        children = new ArrayList<>(1);
    }

    public void addChild(final ObjectInfo info) {
        if (info != null)
            children.add(info);
    }

    /**
     * 这个方法的基本思路是：如果一个对象没new，则只是计算它的指针（引用）的大小；
     *                     如果分配了，则不仅计算指针的大小，还需要计算它指向的一些变量在分配了内存后的总的大小。
     * 注：每次计算完一个对象，需要内存对齐一下。
     */
    public long getDeepSize() {
        return addPaddingSize(arraySize + getDeepSizeInner(arraySize != 0));
    }

    private long getDeepSizeInner(final boolean isArray) {
        long size = 0;
        // 这里主要用来判断是否存在一个无变量的对象，或者这种对象是否被分配了内存
        if (children.isEmpty() && !isPrimitive && !isArray) {
            if ((isInit && offset != 0) || (offset == 0)) {
                return addPaddingSize(ClassIntrospector.OBJECT_REF_SIZE + ClassIntrospector.OBJECT_MARK);
            }
        }
        for (final ObjectInfo child : children)
            size += child.arraySize + child.getDeepSizeInner( child.arraySize != 0 );
        if (!isArray && !children.isEmpty()) {
            int tempSize = children.get( children.size() - 1 ).offset + children.get( children.size() - 1 ).length;
            size += addPaddingSize(tempSize);
        }

        return size;
    }

    /**
     * 这个方法只是单纯的计算对象的指针的大小。
     * 与Instrumentation类相似
     */
    public long getSize() {
        int result = 0;

        if (arraySize > 0) {
            result += arraySize;
        } else {
            int len = children.size();

            if (len > 0) {
                int tempSize = children.get(len - 1).offset + children.get(len - 1).length;
                result += addPaddingSize(tempSize);
            }
        }

        if (result == 0) {
            result = ClassIntrospector.OBJECT_REF_SIZE + ClassIntrospector.OBJECT_MARK;
        }

        return addPaddingSize(result);
    }

    private static final class OffsetComparator implements Comparator<ObjectInfo> {
        @Override
        public int compare(final ObjectInfo o1, final ObjectInfo o2) {
            return o1.offset - o2.offset; //safe because offsets are small non-negative numbers
        }
    }

    //sort all children by their offset
    public void sort() {
        children.sort(new OffsetComparator());
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        toStringHelper(sb, 0);
        return sb.toString();
    }

    private void toStringHelper(final StringBuilder sb, final int depth) {
        depth(sb, depth).append("name=").append(name).append(", type=").append(type)
                .append(", contents=").append(contents).append(", offset=").append(offset)
                .append(", length=").append(length);
        if (arraySize > 0) {
            sb.append(", arrayBase=").append(arrayBase);
            sb.append(", arrayElemSize=").append(arrayElementSize);
            sb.append(", arraySize=").append(arraySize);
        }
        for (final ObjectInfo child : children) {
            sb.append('\n');
            child.toStringHelper(sb, depth + 1);
        }
    }

    private StringBuilder depth(final StringBuilder sb, final int depth) {
        for (int i = 0; i < depth; ++i)
            sb.append("\t");
        return sb;
    }

    private long addPaddingSize(long size) {
        if (size % 8 != 0) {
            return (size / 8 + 1) * 8;
        }
        return size;
    }

}