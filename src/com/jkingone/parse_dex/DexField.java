package com.jkingone.parse_dex;

public class DexField {
	/*struct DexField {
		uleb128 fieldIdx;    // index to a field_id_item
		uleb128 accessFlags;
	};*/
	public int filedIdx;
	public int accessFlags;
	public DexFieldId dexFieldId;

	@Override
	public String toString() {
		return "DexField{" +
				"filedIdx=" + filedIdx +
				", accessFlags=" + accessFlags +
				", dexFieldId=" + dexFieldId +
				'}';
	}
}
