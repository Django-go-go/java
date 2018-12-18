package com.jkingone.parse_dex;

public class DexMethod {
	/*struct DexMethod {
		uleb128 methodIdx;    // index to a method_id_item
		uleb128 accessFlags;
		uleb128 codeOff;      // file offset to a code_item
	};*/
	
	public int methodIdx;
	public int accessFlags;
	public int codeOff;
	public DexMethodId dexMethodId;
	public DexCode dexCode;

	@Override
	public String toString() {
		return "DexMethod{" +
				"methodIdx=" + methodIdx +
				", accessFlags=" + accessFlags +
				", codeOff=" + codeOff +
				", dexMethodId=" + dexMethodId +
				", dexCode=" + dexCode +
				'}';
	}
}
