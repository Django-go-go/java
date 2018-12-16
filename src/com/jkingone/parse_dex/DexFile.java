package com.jkingone.parse_dex;

import com.jkingone.parse_dex.data.*;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DexFile {

    public static void main(String[] args) {

        byte[] srcByte = null;
        FileInputStream fis = null;
        ByteArrayOutputStream bos = null;
        try {
            fis = new FileInputStream("classes.dex");
            bos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len;
            while ((len = fis.read(buffer)) != -1) {
                bos.write(buffer, 0, len);
            }
            srcByte = bos.toByteArray();
        } catch (Exception e) {
            System.err.println("read res file error:" + e);
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
                if (bos != null) {
                    bos.close();
                }
            } catch (Exception e) {
                System.err.println("close file error:" + e);
            }
        }

        if (srcByte == null) {
            System.out.println("get src error...");
            return;
        }

        DexFile dexFile = new DexFile();

        System.out.println("<======================ParseHeader:======================>");
        dexFile.parseDexHeader(srcByte);
        System.out.println("");

        System.out.println("<======================Parse StringIds:======================>");
        dexFile.parseDexStringId(srcByte);
        System.out.println("");

        System.out.println("<======================Parse TypeIds:======================>");
        dexFile.parseDexTypeId(srcByte);
        System.out.println("");

        System.out.println("<======================Parse ProtoIds:======================>");
        dexFile.parseDexProtoId(srcByte);
        System.out.println("");

        System.out.println("<======================Parse FieldIds:======================>");
        dexFile.parseDexFieldId(srcByte);
        System.out.println("");

        System.out.println("<======================Parse MethodIds:======================>");
        dexFile.parseDexMethodId(srcByte);
        System.out.println("");

        System.out.println("<======================Parse ClassDef:======================>");
        dexFile.parseDexClassDef(srcByte);
        System.out.println("");

        System.out.println("<======================Parse DexMapList:======================>");
        dexFile.parseDexMapList(srcByte);
        System.out.println("");

    }

    /**
     struct DexFile {
      DexHeader*    pHeader;
      DexStringId*  pStringIds;
      DexTypeId*    pTypeIds;
      DexFieldId*   pFieldIds;
      DexMethodId*  pMethodIds;
      DexProtoId*   pProtoIds;
      DexClassDef*  pClassDefs;
     };
     */

    private DexHeader dexHeader = new DexHeader();

    private List<DexStringId> dexStringIdsList = new ArrayList<>();
    private List<DexTypeId> dexTypeIdsList = new ArrayList<>();
    private List<DexProtoId> dexProtoIdsList = new ArrayList<>();
    private List<DexFieldId> dexFieldIdsList = new ArrayList<>();
    private List<DexMethodId> dexMethodIdsList = new ArrayList<>();
    private List<DexClassDef> dexClassDefsList = new ArrayList<>();

    private List<String> stringList = new ArrayList<>();


    //====================
    // 解析DexHeader
    //====================

    public void parseDexHeader(byte[] byteSrc) {
        dexHeader.magic = Utils.copyByte(byteSrc, 0, 8);

        byte[] checksumByte = Utils.copyByte(byteSrc, 8, 4);
        dexHeader.checksum = Utils.byteToInt(checksumByte);

        dexHeader.signature = Utils.copyByte(byteSrc, 12, 20);

        byte[] fileSizeByte = Utils.copyByte(byteSrc, 32, 4);
        dexHeader.fileSize = Utils.byteToInt(fileSizeByte);

        byte[] headerSizeByte = Utils.copyByte(byteSrc, 36, 4);
        dexHeader.headerSize = Utils.byteToInt(headerSizeByte);

        byte[] endianTagByte = Utils.copyByte(byteSrc, 40, 4);
        dexHeader.endianTag = Utils.byteToInt(endianTagByte);

        byte[] linkSizeByte = Utils.copyByte(byteSrc, 44, 4);
        dexHeader.linkSize = Utils.byteToInt(linkSizeByte);

        byte[] linkOffByte = Utils.copyByte(byteSrc, 48, 4);
        dexHeader.linkOff = Utils.byteToInt(linkOffByte);

        byte[] mapOffByte = Utils.copyByte(byteSrc, 52, 4);
        dexHeader.mapOff = Utils.byteToInt(mapOffByte);

        byte[] stringIdsSizeByte = Utils.copyByte(byteSrc, 56, 4);
        dexHeader.stringIdsSize = Utils.byteToInt(stringIdsSizeByte);

        byte[] stringIdsOffByte = Utils.copyByte(byteSrc, 60, 4);
        dexHeader.stringIdsOff = Utils.byteToInt(stringIdsOffByte);

        byte[] typeIdsSizeByte = Utils.copyByte(byteSrc, 64, 4);
        dexHeader.typeIdsSize = Utils.byteToInt(typeIdsSizeByte);

        byte[] typeIdsOffByte = Utils.copyByte(byteSrc, 68, 4);
        dexHeader.typeIdsOff = Utils.byteToInt(typeIdsOffByte);

        byte[] protoIdsSizeByte = Utils.copyByte(byteSrc, 72, 4);
        dexHeader.protoIdsSize = Utils.byteToInt(protoIdsSizeByte);

        byte[] protoIdsOffByte = Utils.copyByte(byteSrc, 76, 4);
        dexHeader.protoIdsOff = Utils.byteToInt(protoIdsOffByte);

        byte[] fieldIdsSizeByte = Utils.copyByte(byteSrc, 80, 4);
        dexHeader.fieldIdsSize = Utils.byteToInt(fieldIdsSizeByte);

        byte[] fieldIdsOffByte = Utils.copyByte(byteSrc, 84, 4);
        dexHeader.fieldIdsOff = Utils.byteToInt(fieldIdsOffByte);

        byte[] methodIdsSizeByte = Utils.copyByte(byteSrc, 88, 4);
        dexHeader.methodIdsSize = Utils.byteToInt(methodIdsSizeByte);

        byte[] methodIdsOffByte = Utils.copyByte(byteSrc, 92, 4);
        dexHeader.methodIdsOff = Utils.byteToInt(methodIdsOffByte);

        byte[] classDefsSizeByte = Utils.copyByte(byteSrc, 96, 4);
        dexHeader.classDefsSize = Utils.byteToInt(classDefsSizeByte);

        byte[] classDefsOffByte = Utils.copyByte(byteSrc, 100, 4);
        dexHeader.classDefsOff = Utils.byteToInt(classDefsOffByte);

        byte[] dataSizeByte = Utils.copyByte(byteSrc, 104, 4);
        dexHeader.dataSize = Utils.byteToInt(dataSizeByte);

        byte[] dataOffByte = Utils.copyByte(byteSrc, 108, 4);
        dexHeader.dataOff = Utils.byteToInt(dataOffByte);

        System.out.println("DexHeader:" + dexHeader);

    }

    //====================
    // 解析DexStringId
    //====================

    public void parseDexStringId(byte[] srcByte) {
        for (int i = 0; i < dexHeader.stringIdsSize; i++) {
            int offset = dexHeader.stringIdsOff + i * DexStringId.SIZE;
            byte[] bytes = Utils.copyByte(srcByte, offset, DexStringId.SIZE);
            DexStringId item = new DexStringId();
            item.stringDataOff = Utils.byteToInt(bytes);

            // 第一字节是长度
            String str = getString(srcByte, item.stringDataOff);
//            System.out.println("len = " + srcByte[item.stringDataOff]);
//            System.out.println("string : " + str);

            if (str.contains("permission")) {
                System.out.println(str);
            }
            stringList.add(str);
            dexStringIdsList.add(item);
        }
        System.out.println("string size : " + dexStringIdsList.size());
    }

    private String getString(byte[] srcByte, int startOff) {
        String result = "string is null!!!";
        try {
            byte size = srcByte[startOff];
            byte[] strByte = Utils.copyByte(srcByte, startOff + 1, size);
            result = new String(strByte, "UTF-8");
        } catch (Exception e) {
            // do nothing...
        }
        return result;
    }

    //====================
    // 解析DexTypeId
    //====================

    public void parseDexTypeId(byte[] srcByte) {
        for (int i = 0; i < dexHeader.typeIdsSize; i++) {
            DexTypeId item = new DexTypeId();
            byte[] bytes = Utils.copyByte(srcByte, dexHeader.typeIdsOff + i * DexTypeId.SIZE, DexTypeId.SIZE);
            item.descriptorIdx = Utils.byteToInt(bytes);
            dexTypeIdsList.add(item);

            // 这里的descriptorIdx就是解析之后的字符串中的索引值
//            System.out.println("dexType string: " + stringList.get(item.descriptorIdx));
        }
    }

    //====================
    // 解析DexProtoId
    //====================

    public void parseDexProtoId(byte[] srcByte) {
        for (int i = 0; i < dexHeader.protoIdsSize; i++) {
            byte[] bytes = Utils.copyByte(srcByte, dexHeader.protoIdsOff + i * DexProtoId.SIZE, DexProtoId.SIZE);
            DexProtoId item = parseProtoIdsItem(bytes);
//            System.out.println("shorty descriptor : " + stringList.get(item.shortyIdx));
//            System.out.println("return type : " + stringList.get(item.returnTypeIdx));
            // 有的方法没有参数，这个值就是0
            if (item.parametersOff != 0) {
                item = parseParameterTypeList(srcByte, item.parametersOff, item);
            }
//            System.out.println("\n");
            dexProtoIdsList.add(item);
        }
    }

    private DexProtoId parseProtoIdsItem(byte[] srcByte) {
        DexProtoId item = new DexProtoId();
        byte[] shortyIdxByte = Utils.copyByte(srcByte, 0, 4);
        item.shortyIdx = Utils.byteToInt(shortyIdxByte);
        byte[] returnTypeIdxByte = Utils.copyByte(srcByte, 4, 4);
        item.returnTypeIdx = Utils.byteToInt(returnTypeIdxByte);
        byte[] parametersOffByte = Utils.copyByte(srcByte, 8, 4);
        item.parametersOff = Utils.byteToInt(parametersOffByte);
        return item;
    }

    // 解析方法的所有参数类型
    private DexProtoId parseParameterTypeList(byte[] srcByte, int startOff, DexProtoId item) {
        // 解析size和size大小的List中的内容
        byte[] sizeByte = Utils.copyByte(srcByte, startOff, 4);
        int size = Utils.byteToInt(sizeByte);
        List<String> parametersList = new ArrayList<>();
        List<Short> typeList = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            byte[] typeByte = Utils.copyByte(srcByte, startOff + 4 + 2 * i, 2);
            typeList.add(Utils.byteToShort(typeByte));
        }
//        System.out.println("param count : " + size);
        for (Short aTypeList : typeList) {
//            System.out.println("type : " + stringList.get(aTypeList));
            int index = dexTypeIdsList.get(aTypeList).descriptorIdx;
            parametersList.add(stringList.get(index));
        }

        item.parameterCount = size;
        item.parametersList = parametersList;

        return item;
    }

    //====================
    // 解析DexFieldId
    //====================

    public void parseDexFieldId(byte[] srcByte) {
        for (int i = 0; i < dexHeader.fieldIdsSize; i++) {
            byte[] bytes = Utils.copyByte(srcByte, dexHeader.fieldIdsOff + i * DexFieldId.SIZE, DexFieldId.SIZE);

            DexFieldId dexFieldId = parseFieldIdsItem(bytes);
            dexFieldIdsList.add(dexFieldId);

//            int classIndex = dexTypeIdsList.get(dexFieldId.classIdx).descriptorIdx;
//            int typeIndex = dexTypeIdsList.get(dexFieldId.typeIdx).descriptorIdx;
//            System.out.println("defined class : " + stringList.get(classIndex));
//            System.out.println("field name : " + stringList.get(dexFieldId.nameIdx));
//            System.out.println("field type : " + stringList.get(typeIndex));
//            System.out.println("");
        }
    }

    private DexFieldId parseFieldIdsItem(byte[] srcByte) {
        DexFieldId item = new DexFieldId();
        byte[] classIdxByte = Utils.copyByte(srcByte, 0, 2);
        item.classIdx = Utils.byteToShort(classIdxByte);
        byte[] typeIdxByte = Utils.copyByte(srcByte, 2, 2);
        item.typeIdx = Utils.byteToShort(typeIdxByte);
        byte[] nameIdxByte = Utils.copyByte(srcByte, 4, 4);
        item.nameIdx = Utils.byteToInt(nameIdxByte);
        return item;
    }

    //====================
    // 解析DexMethodId
    //====================

    public void parseDexMethodId(byte[] srcByte) {
        for (int i = 0; i < dexHeader.methodIdsSize; i++) {
            byte[] bytes = Utils.copyByte(srcByte, dexHeader.methodIdsOff + i * DexMethodId.SIZE, DexMethodId.SIZE);
            DexMethodId dexMethodId = parseMethodIdsItem(bytes);
            dexMethodIdsList.add(dexMethodId);

            int classIndex = dexTypeIdsList.get(dexMethodId.classIdx).descriptorIdx;

            DexProtoId dexProtoId = dexProtoIdsList.get(dexMethodId.protoIdx);
            int returnIndex = dexProtoId.returnTypeIdx;
            String returnTypeStr = stringList.get(dexTypeIdsList.get(returnIndex).descriptorIdx);
            int shortIndex = dexProtoId.shortyIdx;
            String shortStr = stringList.get(shortIndex);
            List<String> paramList = dexProtoId.parametersList;

            StringBuilder parameters = new StringBuilder();
            parameters.append(returnTypeStr).append("(");
            for (String str : paramList) {
                parameters.append(str).append(",");
            }
            parameters.append(")").append(shortStr);


            String str = stringList.get(dexMethodId.nameIdx);
            if ("requestPermissions".equals(str)) {
                System.out.println("define class : " + stringList.get(classIndex));
                System.out.println("method name : " + stringList.get(dexMethodId.nameIdx));
                System.out.println("method prototype : " + parameters);
                System.out.println("");
            }
        }
    }

    private DexMethodId parseMethodIdsItem(byte[] srcByte) {
        DexMethodId item = new DexMethodId();
        byte[] classIdxByte = Utils.copyByte(srcByte, 0, 2);
        item.classIdx = Utils.byteToShort(classIdxByte);
        byte[] protoIdxByte = Utils.copyByte(srcByte, 2, 2);
        item.protoIdx = Utils.byteToShort(protoIdxByte);
        byte[] nameIdxByte = Utils.copyByte(srcByte, 4, 4);
        item.nameIdx = Utils.byteToInt(nameIdxByte);
        return item;
    }

    //====================
    // 解析DexClassDef
    //====================

    public void parseDexClassDef(byte[] srcByte) {
        for (int i = 0; i < dexHeader.classDefsSize; i++) {
            byte[] bytes = Utils.copyByte(srcByte, dexHeader.classDefsOff + i * DexClassDef.SIZE, DexClassDef.SIZE);
            DexClassDef dexClassDef = parseClassDefItem(bytes);
            dexClassDefsList.add(dexClassDef);

//            DexTypeId typeItem = dexTypeIdsList.get(dexClassDef.classIdx);
//            System.out.println("class type : " + stringList.get(typeItem.descriptorIdx));
//
//            DexTypeId superTypeItem = dexTypeIdsList.get(dexClassDef.superclassIdx);
//            System.out.println("superclass type : " + stringList.get(superTypeItem.descriptorIdx));
//
//            String sourceFile = stringList.get(dexClassDef.sourceFileIdx);
//            System.out.println("sourceFile name : " + sourceFile);
        }
    }

    private DexClassDef parseClassDefItem(byte[] srcByte) {
        DexClassDef item = new DexClassDef();

        byte[] classIdxByte = Utils.copyByte(srcByte, 0, 4);
        item.classIdx = Utils.byteToInt(classIdxByte);

        byte[] accessFlagsByte = Utils.copyByte(srcByte, 4, 4);
        item.accessFlags = Utils.byteToInt(accessFlagsByte);

        byte[] superClassIdxByte = Utils.copyByte(srcByte, 8, 4);
        item.superclassIdx = Utils.byteToInt(superClassIdxByte);

        // 这里如果class没有interfaces的话，这里就为0
        byte[] interfacesOffByte = Utils.copyByte(srcByte, 12, 4);
        item.interfacesOff = Utils.byteToInt(interfacesOffByte);

        // 如果此项信息缺失，值为0xFFFFFF
        byte[] sourceFileIdxByte = Utils.copyByte(srcByte, 16, 4);
        item.sourceFileIdx = Utils.byteToInt(sourceFileIdxByte);

        byte[] annotationsOffByte = Utils.copyByte(srcByte, 20, 4);
        item.annotationsOff = Utils.byteToInt(annotationsOffByte);

        byte[] classDataOffByte = Utils.copyByte(srcByte, 24, 4);
        item.classDataOff = Utils.byteToInt(classDataOffByte);

        byte[] staticValueOffByte = Utils.copyByte(srcByte, 28, 4);
        item.staticValueOff = Utils.byteToInt(staticValueOffByte);
        return item;
    }

    //====================
    // 解析DexMapList
    //====================

    public void parseDexMapList(byte[] srcByte) {
        DexMapList dexMapList = new DexMapList();
        byte[] sizeByte = Utils.copyByte(srcByte, dexHeader.mapOff, 4);
        int size = Utils.byteToInt(sizeByte);
        for (int i = 0; i < size; i++) {
            byte[] bytes = Utils.copyByte(srcByte, dexHeader.mapOff + 4 + i * DexMapItem.SIZE, DexMapItem.SIZE);
            dexMapList.dexMapItems.add(parseMapItem(bytes));
        }
    }

    private DexMapItem parseMapItem(byte[] srcByte) {
        DexMapItem item = new DexMapItem();
        byte[] typeByte = Utils.copyByte(srcByte, 0, 2);
        item.type = Utils.byteToShort(typeByte);
        byte[] unusedByte = Utils.copyByte(srcByte, 2, 2);
        item.unused = Utils.byteToShort(unusedByte);
        byte[] sizeByte = Utils.copyByte(srcByte, 4, 4);
        item.size = Utils.byteToInt(sizeByte);
        byte[] offsetByte = Utils.copyByte(srcByte, 8, 4);
        item.offset = Utils.byteToInt(offsetByte);
        return item;
    }

}
