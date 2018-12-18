package com.jkingone.parse_dex;

import java.util.ArrayList;
import java.util.List;

public class DexMapList {

	/*struct DexMapList {
		uint  size;       // #of entries in list
		DexMapItem list[1];     // entries
	};*/
	public static final int BYTE_LEN = 4;
	public int size;
	public List<DexMapItem> dexMapItems = new ArrayList<>();

	@Override
	public String toString() {
		return "DexMapList{" +
				"size=" + size +
				", dexMapItems=" + dexMapItems +
				'}';
	}
}
