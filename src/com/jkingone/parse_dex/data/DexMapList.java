package com.jkingone.parse_dex.data;

import java.util.ArrayList;
import java.util.List;

public class DexMapList {
	
	/**
	 * struct DexMapList {
	 u4  size;               // #of entries in list
	 DexMapItem list[1];     // entries
     };
	 */
	
	public int size;
	public List<DexMapItem> dexMapItems = new ArrayList<>();

}
