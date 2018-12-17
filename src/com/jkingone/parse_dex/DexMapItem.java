package com.jkingone.parse_dex;

public class DexMapItem {
	
	/**
	 * struct DexMapItem {
	 u2 type;              // type code (see kDexType* above)
	 u2 unused;
	 u4 size;              // count of items of the indicated type
	 u4 offset;            // file offset to the start of data
     };
	 */

	public static final int SIZE = 2 + 2 + 4 + 4;
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
