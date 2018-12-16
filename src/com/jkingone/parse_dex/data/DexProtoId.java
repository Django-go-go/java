package com.jkingone.parse_dex.data;

import java.util.ArrayList;
import java.util.List;

public class DexProtoId {

	/**
	 * struct DexProtoId {
	 uint  shortyIdx;          // index into stringIds for shorty descriptor
	 uint  returnTypeIdx;      // index into typeIds list for return type
	 uint  parametersOff;      // file offset to type_list for parameter types
     };
	 */

	public static final int SIZE = 4 + 4 + 4;

	public int shortyIdx;
	public int returnTypeIdx;
	public int parametersOff;

	// 这个不是公共字段，而是为了存储方法原型中的参数类型名和参数个数
	public List<String> parametersList = new ArrayList<>();
	public int parameterCount;

	@Override
	public String toString() {
		return "DexProtoId{" +
				"shortyIdx=" + shortyIdx +
				", returnTypeIdx=" + returnTypeIdx +
				", parametersOff=" + parametersOff +
				", parametersList=" + parametersList +
				", parameterCount=" + parameterCount +
				'}';
	}

}