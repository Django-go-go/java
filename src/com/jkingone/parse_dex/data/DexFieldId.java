package com.jkingone.parse_dex.data;

public class DexFieldId {
	
	/**
	 * data DexFieldId
		{
		ushort classIdx; // index into typeIds list for defining class
		ushort typeIdx; // index into typeIds for field type
		uint nameIdx; // index into stringIds for field name
		}
	 */

	public static final int SIZE = 2 + 2 + 4;

	public short classIdx;
	public short typeIdx;
	public int nameIdx;

	@Override
	public String toString() {
		return "DexFieldId{" +
				"classIdx=" + classIdx +
				", typeIdx=" + typeIdx +
				", nameIdx=" + nameIdx +
				'}';
	}

}
