package com.jkingone.parse_dex;

public class DexTypeId {

	/*struct DexTypeId {
		uint descriptorIdx; // index into stringIds list for type descriptor
	}*/

	public static final int BYTE_LEN = 4;

	public int descriptorIdx;
	public String typeDescriptor;

	@Override
	public String toString() {
		return "DexTypeId{" +
				"descriptorIdx=" + descriptorIdx +
				", typeDescriptor='" + typeDescriptor + '\'' +
				'}';
	}
}
