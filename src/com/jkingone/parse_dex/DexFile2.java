package com.jkingone.parse_dex;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DexFile2 {

    public static void main(String[] args) {

        byte[] srcByte = null;
        FileInputStream fis = null;
        ByteArrayOutputStream bos = null;
        try {
            fis = new FileInputStream("DexDemo.dex");
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

        DexFile2 dexFile = new DexFile2();

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

//        System.out.println("<======================Parse DexMapList:======================>");
//        dexFile.parseDexMapList(srcByte);
//        System.out.println("");
//
//        System.out.println("<======================Parse Class Data:======================>");
//        dexFile.parseClassData(srcByte);
//
//        System.out.println("<======================Parse Code Content:======================>");
//        dexFile.parseCode(srcByte);

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

    private List<DexStringId> dexStringIds = new ArrayList<>();
    private List<DexTypeId> dexTypeIds = new ArrayList<>();
    private List<DexProtoId> dexProtoIds = new ArrayList<>();
    private List<DexFieldId> dexFieldIds = new ArrayList<>();
    private List<DexMethodId> dexMethodIds = new ArrayList<>();
    private List<DexClassDef> dexClassDefs = new ArrayList<>();

    private List<ClassDataItem> dataItemList = new ArrayList<>();

    private List<CodeItem> directMethodCodeItemList = new ArrayList<>();
    private List<CodeItem> virtualMethodCodeItemList = new ArrayList<>();

    // 这里的map用来存储code数据，因为一个ClassCode都是以class_idx为单位的，所以这里的key就是classname来存储
    private HashMap<String, DexClassDef> classDataMap = new HashMap<>();


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

        System.out.println("DexHeader : " + dexHeader);

    }

    //====================
    // 解析DexStringId
    //====================

    public void parseDexStringId(byte[] srcByte) {
        for (int i = 0; i < dexHeader.stringIdsSize; i++) {
            int offset = dexHeader.stringIdsOff + i * DexStringId.BYTE_LEN;
            byte[] bytes = Utils.copyByte(srcByte, offset, DexStringId.BYTE_LEN);
            DexStringId item = new DexStringId();
            item.stringDataOff = Utils.byteToInt(bytes);
            // 第一字节是长度
            String result;
            try {
                byte size = srcByte[item.stringDataOff];
                byte[] strByte = Utils.copyByte(srcByte, item.stringDataOff + 1, size);
                result = new String(strByte, "UTF-8");
            } catch (Exception e) {
                // do nothing...
                result = "string is error : " + e;
            }
            item.stringData = result;
            dexStringIds.add(item);
            System.out.println("DexStringId : " + item);
        }
    }

    //====================
    // 解析DexTypeId
    //====================

    public void parseDexTypeId(byte[] srcByte) {
        for (int i = 0; i < dexHeader.typeIdsSize; i++) {
            DexTypeId item = new DexTypeId();
            byte[] bytes = Utils.copyByte(srcByte, dexHeader.typeIdsOff + i * DexTypeId.BYTE_LEN, DexTypeId.BYTE_LEN);
            item.descriptorIdx = Utils.byteToInt(bytes);
            item.typeDescriptor = dexStringIds.get(item.descriptorIdx).stringData;
            dexTypeIds.add(item);
            System.out.println("DexTypeId : " + item);
        }
    }

    //====================
    // 解析DexProtoId
    //====================

    public void parseDexProtoId(byte[] srcByte) {
        for (int i = 0; i < dexHeader.protoIdsSize; i++) {
            DexProtoId item = new DexProtoId();
            byte[] bytes = Utils.copyByte(srcByte, dexHeader.protoIdsOff + i * DexProtoId.BYTE_LEN, DexProtoId.BYTE_LEN);
            item.shortyIdx = Utils.byteToInt(Utils.copyByte(bytes, 0, 4));
            item.shortyDescriptor = dexStringIds.get(item.shortyIdx).stringData;
            item.returnTypeIdx = Utils.byteToInt(Utils.copyByte(bytes, 4, 4));
            item.returnType = dexTypeIds.get(item.returnTypeIdx).typeDescriptor;
            item.parametersOff = Utils.byteToInt(Utils.copyByte(bytes, 8, 4));
            // 方法没有参数，值就是0
            item.parameterList = parseDexTypeList(srcByte, item.parametersOff);
            dexProtoIds.add(item);
            System.out.println("DexProtoId : " + item);
        }
    }

    //====================
    // 解析DexFieldId
    //====================

    public void parseDexFieldId(byte[] srcByte) {
        for (int i = 0; i < dexHeader.fieldIdsSize; i++) {
            byte[] bytes = Utils.copyByte(srcByte, dexHeader.fieldIdsOff + i * DexFieldId.BYTE_LEN, DexFieldId.BYTE_LEN);
            DexFieldId item = new DexFieldId();
            item.classIdx = Utils.byteToShort(Utils.copyByte(bytes, 0, 2));
            item.fieldDefiningClass = dexTypeIds.get(item.typeIdx).typeDescriptor;
            item.typeIdx = Utils.byteToShort(Utils.copyByte(bytes, 2, 2));
            item.fieldType = dexTypeIds.get(item.typeIdx).typeDescriptor;
            item.nameIdx = Utils.byteToInt(Utils.copyByte(bytes, 4, 4));
            item.fieldName = dexStringIds.get(item.nameIdx).stringData;
            dexFieldIds.add(item);
            System.out.println("DexFieldId : " + item);
        }
    }

    //====================
    // 解析DexMethodId
    //====================

    public void parseDexMethodId(byte[] srcByte) {
        for (int i = 0; i < dexHeader.methodIdsSize; i++) {
            byte[] bytes = Utils.copyByte(srcByte, dexHeader.methodIdsOff + i * DexMethodId.BYTE_LEN, DexMethodId.BYTE_LEN);
            DexMethodId item = new DexMethodId();
            item.classIdx = Utils.byteToShort(Utils.copyByte(bytes, 0, 2));
            item.methodDefiningClass = dexTypeIds.get(item.classIdx).typeDescriptor;
            item.protoIdx = Utils.byteToShort(Utils.copyByte(bytes, 2, 2));
            item.nameIdx = Utils.byteToInt(Utils.copyByte(bytes, 4, 4));
            item.methodName = dexStringIds.get(item.nameIdx).stringData;
            dexMethodIds.add(item);

            DexProtoId dexProtoId = dexProtoIds.get(item.protoIdx);
            String returnType = dexTypeIds.get(dexProtoId.returnTypeIdx).typeDescriptor;
            String shorty = dexStringIds.get(dexProtoId.shortyIdx).stringData;
            if (dexProtoId.parameterList != null) {
                StringBuilder parameters = new StringBuilder();
                parameters.append(returnType).append("(");
                for (DexTypeItem typeItem : dexProtoId.parameterList.dexTypeItems) {
                    parameters.append(typeItem.typeDescriptor).append(",");
                }
                parameters.append(")").append(shorty);
                item.methodPrototype = parameters.toString();
            }
            System.out.println("DexMethodId : " + item);
        }
    }

    //====================
    // 解析DexClassDef
    //====================

    public void parseDexClassDef(byte[] srcByte) {
        for (int i = 0; i < dexHeader.classDefsSize; i++) {
            byte[] bytes = Utils.copyByte(srcByte, dexHeader.classDefsOff + i * DexClassDef.BYTE_LEN, DexClassDef.BYTE_LEN);
            DexClassDef classDef = new DexClassDef();
            classDef.classIdx = Utils.byteToInt(Utils.copyByte(bytes, 0, 4));
            classDef.className = dexTypeIds.get(classDef.classIdx).typeDescriptor;
            classDef.accessFlags = Utils.byteToInt(Utils.copyByte(bytes, 4, 4));
            classDef.superclassIdx = Utils.byteToInt(Utils.copyByte(bytes, 8, 4));
            classDef.superClassName = dexTypeIds.get(classDef.superclassIdx).typeDescriptor;
            // 这里如果class没有interfaces的话，这里就为0
            classDef.interfacesOff = Utils.byteToInt(Utils.copyByte(bytes, 12, 4));
            classDef.interfaceData = parseDexTypeList(srcByte, classDef.interfacesOff);
            // 如果此项信息缺失，值为0xFFFFFF
            classDef.sourceFileIdx = Utils.byteToInt(Utils.copyByte(bytes, 16, 4));
            classDef.sourceFileName = dexStringIds.get(classDef.sourceFileIdx).stringData;
            classDef.annotationsOff = Utils.byteToInt(Utils.copyByte(bytes, 20, 4));
            classDef.classDataOff = Utils.byteToInt(Utils.copyByte(bytes, 24, 4));
            classDef.staticValueOff = Utils.byteToInt(Utils.copyByte(bytes, 28, 4));
            dexClassDefs.add(classDef);

            System.out.println("DexClassDef : " + classDef);
            classDataMap.put(String.valueOf(classDef.classIdx), classDef);
        }
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


    private DexTypeList parseDexTypeList(byte[] srcByte, int offset) {
        if (offset != 0) {
            DexTypeList dexTypeList = new DexTypeList();
            // 解析size和size大小的List中的内容
            dexTypeList.size = Utils.byteToInt(Utils.copyByte(srcByte, offset, DexTypeList.PARA_BYTE_LEN));
            offset += DexTypeList.PARA_BYTE_LEN;
            for (int j = 0; j < dexTypeList.size; j++) {
                DexTypeItem dexTypeItem = new DexTypeItem();
                byte[] typeByte = Utils.copyByte(srcByte,
                        offset + DexTypeItem.BYTE_LEN * j, DexTypeItem.BYTE_LEN);
                Short typeId = Utils.byteToShort(typeByte);
                dexTypeItem.typeIdx = typeId;
                dexTypeItem.typeDescriptor = dexTypeIds.get(typeId).typeDescriptor;
                dexTypeList.dexTypeItems.add(dexTypeItem);
            }
            return dexTypeList;
        }
        return null;
    }
}

