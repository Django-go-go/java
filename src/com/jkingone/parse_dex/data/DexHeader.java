package com.jkingone.parse_dex.data;

import com.jkingone.parse_dex.Utils;

public class DexHeader {

	/**
	 * data header_item
		{
		ubyte[8] magic; // dex 版本标识
		uint checksum; // adler32 检验
		ubyte[20] signature; // SHA-1 哈希值，20个字节
		uint fileSize; // 整个 dex 文件大小
		uint headerSize; // DexHeader 结构大小，0x70
		unit endianTag; // 字节序标记，小端 "0x12345678"，大端"0x78563412"
		uint linkSize; // 链接段大小
		uint linkOff; // 链接段偏移
		uint mapOff; // DexMapList 的偏移
		uint stringIdsSize;
		uint stringIdsOff;
		uint typeIdsSize;
		uint typeIdsOff;
		uint protoIdsSize;
		uint protoIdsOff;
		uint methodIdsSize;
		uint methodIdsOff;
		uint classDefsSize;
		uint classDefsOff;
		uint dataSize;
		uint dataOff;
		}
	 */
	public byte[] magic = new byte[8];
	public int checksum;
	public byte[] signature = new byte[20];
	public int fileSize;
	public int headerSize;
	public int endianTag;
	public int linkSize;
	public int linkOff;
	public int mapOff;
	public int stringIdsSize;
	public int stringIdsOff;
	public int typeIdsSize;
	public int typeIdsOff;
	public int protoIdsSize;
	public int protoIdsOff;
	public int fieldIdsSize;
	public int fieldIdsOff;
	public int methodIdsSize;
	public int methodIdsOff;
	public int classDefsSize;
	public int classDefsOff;
	public int dataSize;
	public int dataOff;

	@Override
	public String toString(){
        return "magic: " + Utils.bytesToHexString(magic) + "\n"
                + "checksum: " + checksum + "\n"
                + "signature: " + Utils.bytesToHexString(signature) + "\n"
                + "fileSize: " + fileSize + "\n"
                + "headerSize:" + headerSize + "\n"
                + "endianTag: " + endianTag + "\n"
                + "linkSize: " + linkSize + "\n"
                + "linkOff: " + linkOff + " ==> " + Utils.bytesToHexString(Utils.int2ToByte(linkOff)) + "\n"
                + "mapOff: " + mapOff + " ==> " + Utils.bytesToHexString(Utils.int2ToByte(mapOff)) + "\n"
                + "stringIdsSize: " + stringIdsSize + "\n"
                + "stringIdsOff: " + stringIdsOff + " ==> " + Utils.bytesToHexString(Utils.int2ToByte(stringIdsOff)) + "\n"
                + "typeIdsSize: " + typeIdsSize + "\n"
                + "typeIdsOff: " + typeIdsOff + " ==> " + Utils.bytesToHexString(Utils.int2ToByte(typeIdsOff)) + "\n"
                + "protoIdsSize: " + protoIdsSize + "\n"
                + "protoIdsOff: " + protoIdsOff + " ==> " + Utils.bytesToHexString(Utils.int2ToByte(protoIdsOff)) + "\n"
                + "fieldIdsSize: " + fieldIdsSize + "\n"
                + "fieldIdsOff: " + fieldIdsOff + " ==> " + Utils.bytesToHexString(Utils.int2ToByte(fieldIdsOff)) + "\n"
                + "methodIdsSize: " + methodIdsSize + "\n"
                + "methodIdsOff: " + methodIdsOff + " ==> " + Utils.bytesToHexString(Utils.int2ToByte(methodIdsOff)) + "\n"
                + "classDefsSize: " + classDefsSize + "\n"
                + "classDefsOff: " + classDefsOff + " ==> " + Utils.bytesToHexString(Utils.int2ToByte(classDefsOff)) + "\n"
                + "dataSize: " + dataSize + "\n"
                + "dataOff: " + dataOff + " ==> " + Utils.bytesToHexString(Utils.int2ToByte(dataOff));
	}

}
