package com.jkingone.parse_dex;

public class DexMethodId {
	
	/*struct DexMethodId {
		  ushort classIdx; // index into typeIds list for defining class
		  ushort protoIdx; // index into protoIds for method prototype
		  uint nameIdx; // index into stringIds for method name
	}*/

	public static final int BYTE_LEN = 2 + 2 + 4;

	public short classIdx;
	public short protoIdx;
	public int nameIdx;

	public String methodDefiningClass;
	public String methodPrototype;
	public String methodName;

	@Override
	public String toString() {
		return "DexMethodId{" +
				"classIdx=" + classIdx +
				", protoIdx=" + protoIdx +
				", nameIdx=" + nameIdx +
				", methodDefiningClass='" + methodDefiningClass + '\'' +
				", methodPrototype='" + methodPrototype + '\'' +
				", methodName='" + methodName + '\'' +
				'}';
	}

}
