package com.jkingone.parse_dex;

public class DexMapItem {

	/*struct DexMapItem {
		ushort type;       // type code (see kDexType* above)
		ushort unused;
		uint size;         // count of items of the indicated type
		uint offset;       // file offset to the start of data
	};*/


	public static final int BYTE_LEN = 2 + 2 + 4 + 4;
	public short type;
	public short unused;
	public int size;
	public int offset;

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
