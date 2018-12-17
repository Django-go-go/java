package com.jkingone.parse_dex;

public class DexStringId {

	/*struct DexStringId {
		u4 stringDataOff;  // file offset to string_data_item
	};*/
	
	public int stringDataOff;

	@Override
	public String toString() {
		return "DexStringId{" +
				"stringDataOff=" + stringDataOff +
				", stringData='" + stringData + '\'' +
				'}';
	}

	public String stringData;
	public static final int BYTE_LEN = 4;

}
