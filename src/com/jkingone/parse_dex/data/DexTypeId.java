package com.jkingone.parse_dex.data;

import com.jkingone.parse_dex.Utils;

public class DexTypeId {
	
	/**
	 * data type_ids_item
		{
		uint descriptorIdx; // index into stringIds list for type descriptor
		}
	 */
	
	public int descriptorIdx;
	public static final int SIZE = 4;

	@Override
	public String toString() {
		return "DexTypeId{" +
				"descriptorIdx : " + descriptorIdx +
				", descriptorIdx bytesToHexString : " + Utils.bytesToHexString(Utils.int2ToByte(descriptorIdx)) +
				'}';
	}

}
