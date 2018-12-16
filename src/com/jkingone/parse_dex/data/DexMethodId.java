package com.jkingone.parse_dex.data;

public class DexMethodId {
	
	/**
	 * data DexMethodId
		{
		ushort classIdx; // index into typeIds list for defining class
		ushort protoIdx; // index into protoIds for method prototype
		uint nameIdx; // index into stringIds for method name
		}
	 */

	public static final int SIZE = 2 + 2 + 4;

	public short classIdx;
	public short protoIdx;
	public int nameIdx;

	@Override
	public String toString() {
		return "DexMethodId{" +
				"classIdx=" + classIdx +
				", protoIdx=" + protoIdx +
				", nameIdx=" + nameIdx +
				'}';
	}

}
