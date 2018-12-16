package com.jkingone.parse_dex.data;

import com.jkingone.parse_dex.Utils;

public class DexStringId {
	
	/**
	 * struct DexStringId {
	       u4 stringDataOff;      // file offset to string_data_item
       };
	 */
	
	public int stringDataOff;
	public static final int SIZE = 4;
	
	@Override
	public String toString(){
		return Utils.bytesToHexString(Utils.int2ToByte(stringDataOff));
	}

}
