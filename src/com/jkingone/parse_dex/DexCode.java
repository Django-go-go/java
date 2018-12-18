package com.jkingone.parse_dex;

import java.util.ArrayList;
import java.util.List;

public class DexCode {
	/*struct DexCode {
		ushort registersSize;
		ushort insSize;
		ushort outsSize;
		ushort triesSize;
		uint debugInfoOff;  // file offset to debug info stream
		uint insnssize;
		ushort insns [insnsSize];
		// followed by optional u2 padding
	    // followed by try_item[triesSize]
	    // followed by uleb128 handlersSize
	    // followed by catch_handler_item[handlersSize]
	}*/
	public static final int BYTE_LEN = 2 * 4 + 4 * 2;
	public short registersSize;
	public short insSize;
	public short outsSize;
	public short triesSize;
	public int debugInfoOff;
	public int insnsSize;
	public List<Short> insns = new ArrayList<>();

	@Override
	public String toString() {
		return "DexCode{" +
				"registersSize=" + registersSize +
				", insSize=" + insSize +
				", outsSize=" + outsSize +
				", triesSize=" + triesSize +
				", debugInfoOff=" + debugInfoOff +
				", insnsSize=" + insnsSize +
				", insns=" + insns +
				'}';
	}
	
}
