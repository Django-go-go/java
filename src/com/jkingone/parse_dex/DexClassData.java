package com.jkingone.parse_dex;

import java.util.ArrayList;
import java.util.List;

public class DexClassData {
	/*struct DexClassData {
		DexClassDataHeader header;
		DexField*          staticFields;
		DexField*          instanceFields;
		DexMethod*         directMethods;
		DexMethod*         virtualMethods;
	};*/
	public DexClassDataHeader header = new DexClassDataHeader();
	public List<DexField> staticFields = new ArrayList<>();
	public List<DexField> instanceFields = new ArrayList<>();
	public List<DexMethod> directMethods = new ArrayList<>();
	public List<DexMethod> virtualMethods = new ArrayList<>();

	@Override
	public String toString() {
		return "DexClassData{" +
				"header=" + header +
				", staticFields=" + staticFields +
				", instanceFields=" + instanceFields +
				", directMethods=" + directMethods +
				", virtualMethods=" + virtualMethods +
				'}';
	}

	public class DexClassDataHeader {
		/*struct DexClassDataHeader {
			uleb128 staticFieldsSize;
			uleb128 instanceFieldsSize;
			uleb128 directMethodsSize;
			uleb128 virtualMethodsSize;
		};*/
		public int staticFieldsSize;
		public int instanceFieldsSize;
		public int directMethodsSize;
		public int virtualMethodsSize;

		@Override
		public String toString() {
			return "DexClassDataHeader{" +
					"staticFieldsSize=" + staticFieldsSize +
					", instanceFieldsSize=" + instanceFieldsSize +
					", directMethodsSize=" + directMethodsSize +
					", virtualMethodsSize=" + virtualMethodsSize +
					'}';
		}
	}
}
