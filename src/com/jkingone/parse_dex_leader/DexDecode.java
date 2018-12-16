package com.jkingone.parse_dex_leader;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xulc on 2018/9/8.
 *
 data DexFile
 {
 DexHeader           Header;
 DexStringId          StringIds[stringIdsSize];
 DexTypeId           TypeIds[typeIdsSize];
 DexProtoId           ProtoIds[protoIdsSize];
 DexFieldId            FieldIds[fieldIdsSize];
 DexMethodId       MethodIds[methodIdsSize];
 DexClassDef         ClassDefs[classDefsSize];
 DexData                Data[];
 DexLink                 LinkData;
 };

 typedef data _DexHeader
 {
 u1	magic[8];						// dex 版本标识，"dex.035"
 u4	checksum;						// adler32 检验
 u1	signature[kSHA1DigestLen];		// SHA-1 哈希值，20个字节
 u4	fileSize;						// 整个 dex 文件大小
 u4	headerSize;						// DexHeader 结构大小，0x70
 u4	endianTag;						// 字节序标记，小端 "0x12345678"，大端"0x78563412"
 u4	linkSize;						// 链接段大小
 u4	linkOff;						// 链接段偏移
 u4	mapOff;							// DexMapList 的偏移
 u4	stringIdsSize;					// DexStringId 的个数
 u4	stringIdsOff;					// DexStringId 的偏移		字符串
 u4	typeIdsSize;					// DexTypeId 的个数
 u4	typeIdsOff;					// DexTypeId 的偏移			类型
 u4	protoIdsSize;					// DexProtoId 的个数
 u4	protoIdsOff;					// DexProtoId 的偏移			声明
 u4	fieldIdsSize;					// DexFieldId 的个数
 u4	fieldIdsOff;					// DexFieldId 的偏移			字段
 u4	methodIdsSize;					// DexMethodId 的个数
 u4	methodIdsOff;					// DexMethodId 的偏移		方法
 u4	classDefsSize;					// DexClassDef 的个数
 u4	classDefsOff;					// DexClassDef 的偏移		类
 u4	dataSize;						// 数据段的大小
 u4	dataOff;						// 数据段的偏移
 }DexHeader, *PDexHeader;

 data     DexMapItem
 {
 u2     type;                    // kDexType 开头的类型
 u2     unused;               // 未使用，用于对齐
 u4     size;                     // 指定类型的个数
 u4     offset;                 // 指定类型数据的文件偏移
 };

 */

public class DexDecode {

    private static final String TAG = "DexDecode";

    private static final String FILE_NAME = "Hello.dex";

    public static void main(String[] args) {
        DexDecode decode = new DexDecode();
        decode.decode(FILE_NAME);
    }

    private void decode(String fileName) {
        File file = new File(fileName);
        if (!file.exists()) {
            System.out.print("file not exist!");
            return;
        }

//        toChar(file);
        byte[] bytes = toBytes(file);
//        print("bytes", bytesToHexString(bytes));

        System.out.println(parseDexHeader(bytes));

//        String magic = decodeMagic(bytes);
//        print("magic", magic);
//
//        String checkSum = decodeChecksum(bytes);
//        print("checkSum", checkSum);
//
//        int fileSize = decodeFileSize(bytes);
//        print("fileSize", String.valueOf(fileSize));
//
//        int headerSize = decodeHeaderSize(bytes);
//        print("headerSize", String.valueOf(headerSize));
//
//        int stringIdsSize = decodeStringIdsSize(bytes);
//        print("stringIdsSize", String.valueOf(stringIdsSize));
//
//        List<String> strings = decodeAllStrings(bytes);
//        for (String string : strings) {
//            print("string", string);
//        }
//
//        List<DexMapItem> dexMapItemList = decodeMapList(bytes);
//        System.out.println(dexMapItemList);
    }

    private List<DexMapItem> decodeMapList(byte[] bytes) {
        List<DexMapItem> mapItems = new ArrayList<>();
        final int mapOffAddr = decodeMapOff(bytes);
        final int mapItemSize = decodeAddr(bytes, mapOffAddr, 0x4);
        final int mapItemType = decodeAddr(bytes, (mapOffAddr + 0x4), 0x4);
        print("mapItemType", String.valueOf(mapItemType));
        return mapItems;
    }

    private static class DexMapItem {
        int type;
        int unused;
        int size;
        int offset;

        @Override
        public String toString() {
            return "DexMapItem{" +
                    "type=" + type +
                    ", unused=" + unused +
                    ", size=" + size +
                    ", offset=" + offset +
                    '}';
        }
    }

    public static final int kSHA1DigestLen = 20;

    private static class DexHeader {
        String magic;						// dex 版本标识，"dex.035"
        String checksum;						// adler32 检验
        String signature;		// SHA-1 哈希值，20个字节
        int	fileSize;						// 整个 dex 文件大小
        int	headerSize;						// DexHeader 结构大小，0x70
        String endianTag;						// 字节序标记，小端 "0x12345678"，大端"0x78563412"
        int	linkSize;						// 链接段大小
        int	linkOff;						// 链接段偏移
        int	mapOff;							// DexMapList 的偏移
        int	stringIdsSize;					// DexStringId 的个数
        int	stringIdsOff;					// DexStringId 的偏移		字符串
        int	typeIdsSize;					// DexTypeId 的个数
        int	typeIdsOff;					// DexTypeId 的偏移			类型
        int	protoIdsSize;					// DexProtoId 的个数
        int	protoIdsOff;					// DexProtoId 的偏移			声明
        int	fieldIdsSize;					// DexFieldId 的个数
        int	fieldIdsOff;					// DexFieldId 的偏移			字段
        int	methodIdsSize;					// DexMethodId 的个数
        int	methodIdsOff;					// DexMethodId 的偏移		方法
        int	classDefsSize;					// DexClassDef 的个数
        int	classDefsOff;					// DexClassDef 的偏移		类
        int	dataSize;						// 数据段的大小
        int	dataOff;						// 数据段的偏移

        @Override
        public String toString() {
            return "DexHeader{" +
                    "magic='" + magic + '\'' +
                    ", checksum='" + checksum + '\'' +
                    ", signature='" + signature + '\'' +
                    ", fileSize=" + fileSize +
                    ", headerSize=" + headerSize +
                    ", endianTag='" + endianTag + '\'' +
                    ", linkSize=" + linkSize +
                    ", linkOff=" + linkOff +
                    ", mapOff=" + mapOff +
                    ", stringIdsSize=" + stringIdsSize +
                    ", stringIdsOff=" + stringIdsOff +
                    ", typeIdsSize=" + typeIdsSize +
                    ", typeIdsOff=" + typeIdsOff +
                    ", protoIdsSize=" + protoIdsSize +
                    ", protoIdsOff=" + protoIdsOff +
                    ", fieldIdsSize=" + fieldIdsSize +
                    ", fieldIdsOff=" + fieldIdsOff +
                    ", methodIdsSize=" + methodIdsSize +
                    ", methodIdsOff=" + methodIdsOff +
                    ", classDefsSize=" + classDefsSize +
                    ", classDefsOff=" + classDefsOff +
                    ", dataSize=" + dataSize +
                    ", dataOff=" + dataOff +
                    '}';
        }
    }

    private DexHeader parseDexHeader(byte[] bytes) {
        DexHeader dexHeader = new DexHeader();
        int offset = 0x0;
        int len = 0x8;
        dexHeader.magic = new String(bytes, offset, len);
        offset += len;
        len = 0x4;
        dexHeader.checksum = new String(bytes, offset, len);
        return dexHeader;
    }

    private int decodeMapOff(byte[] bytes) {
        final int offset = 0x34;
        final int length = 0x4;
        byte[] b = new byte[length];
        System.arraycopy(bytes, offset, b, 0, length);
        b = reverse(b);
        return bytesToInt(b);
    }

    private String decodeMagic(byte[] bytes) {
        final int offset = 0x0;
        final int length = 0x8;
        byte[] bMagic = new byte[length];
        System.arraycopy(bytes, offset, bMagic, 0, length);
        return new String(bMagic);
    }

    private String decodeChecksum(byte[] bytes) {
        final int offset = 0x8;
        final int length = 0x4;
        byte[] bSum = new byte[length];
        System.arraycopy(bytes, offset, bSum, 0, length);
        return new String(bSum);
    }

    private int decodeFileSize(byte[] bytes) {
        final int offset = 0x20;
        final int length = 0x4;
        byte[] bSize = new byte[length];
        System.arraycopy(bytes, offset, bSize, 0, length);
        bSize = reverse(bSize);
        return bytesToInt(bSize);
    }

    private int decodeHeaderSize(byte[] bytes) {
        final int offset = 0x24;
        final int length = 0x4;
        byte[] hSize = new byte[length];
        System.arraycopy(bytes, offset, hSize, 0, length);
        hSize = reverse(hSize);
        return bytesToInt(hSize);
    }

    private int decodeStringIdsSize(byte[] bytes) {
        final int offset = 0x38;
        final int length = 0x4;
        byte[] b = new byte[length];
        System.arraycopy(bytes, offset, b, 0, length);
        b = reverse(b);
        return bytesToInt(b);
    }

    private List<String> decodeAllStrings(byte[] bytes) {
        final int stringIdsSize = decodeStringIdsSize(bytes);
        final int stringIdsOff = decodeStringsIdsOff(bytes); // 记录字符串起始地址

        final int length = 0x4;

        List<String> strings = new ArrayList<>();

        int addr = stringIdsOff;
        for (int i = 0; i < stringIdsSize; i++) {
            int stringAddr = decodeAddr(bytes, addr, length); // 读取字符串地址
            String string = decodeString(bytes, stringAddr);
            strings.add(string);
            addr += length;
        }
        return strings;
    }

    private int decodeAddr(final byte[] bytes, final int addr, final int length) {
        byte[] b = new byte[length];
        System.arraycopy(bytes, addr, b, 0, length);
        b = reverse(b);
        return bytesToInt(b);
    }

    private String decodeString(final byte[] bytes, final int addr) {
        int size = byteToInt(bytes[addr]) + 1; // 先读取第一个字节，用于确定当前字符串的长度
        byte[] b = new byte[size];
        System.arraycopy(bytes, addr, b, 0, size);
        return new String(b);
    }

    private int decodeStringsIdsOff(byte[] bytes) {
        final int offset = 0x3C;
        final int length = 0x4;
        byte[] b = new byte[length];
        System.arraycopy(bytes, offset, b, 0, length);
        b = reverse(b);
        return bytesToInt(b);
    }

    private byte[] reverse(byte[] src) {
        byte[] dst = new byte[src.length];
        for (int i = 0; i < dst.length; i++) {
            dst[i] = src[src.length - 1 - i];
        }
        return dst;
    }

    private void toChar(File file) {
        char[] chars = new char[1024];
        List<Character> list = new ArrayList<>();
        int len = 0;
        try {
            FileReader fileReader = new FileReader(file);
            while ((len = fileReader.read(chars)) != -1) {
//                for (int i = 0; i < len; i++) {
//                    list.add(chars[i]);
//                }
                System.out.println(String.valueOf(chars));
            }
            fileReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private byte[] toBytes(File file) {

        InputStream is = null;
        byte[] bytes = null;
        try {
            is = new FileInputStream(file);
            bytes= new byte[is.available()];
            is.read(bytes);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bytes;
    }

    private String bytesToHexString(byte[] src){
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    private int bytesToInt(byte[] bytes){
        int result = 0;
        if(bytes.length == 4){
            int a = (bytes[0] & 0xff) << 24;//说明二
            int b = (bytes[1] & 0xff) << 16;
            int c = (bytes[2] & 0xff) << 8;
            int d = (bytes[3] & 0xff);
            result = a | b | c | d;
        }
        return result;
    }

    private int byteToInt(byte b) {
        return b & 0xFF;
    }

    private void print(String prefix, String content) {
        System.out.print(TAG.concat(" | ").concat(prefix.concat(" : ").concat(content)));
        System.out.print("\n");
    }
}
