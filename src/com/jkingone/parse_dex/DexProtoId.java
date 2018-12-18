package com.jkingone.parse_dex;

public class DexProtoId {
    /*struct DexProtoId {
        uint  shortyIdx;          // index into stringIds for shorty descriptor
        uint  returnTypeIdx;      // index into typeIds list for return type
        uint  parametersOff;      // file offset to type_list for parameter types
    };*/
	public static final int BYTE_LEN = 4 + 4 + 4;

	public int shortyIdx;
	public int returnTypeIdx;
	public int parametersOff;

	public String shortyDescriptor;
	public String returnType;
	public DexTypeList parameterList;

    @Override
    public String toString() {
        return "DexProtoId{" +
                "shortyIdx=" + shortyIdx +
                ", returnTypeIdx=" + returnTypeIdx +
                ", parametersOff=" + parametersOff +
                ", shortyDescriptor='" + shortyDescriptor + '\'' +
                ", returnType='" + returnType + '\'' +
                ", dexTypeList=" + parameterList +
                '}';
    }

}