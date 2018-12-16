package com.jkingone.parse_dex_2;

import com.jkingone.parse_dex.Utils;

public class EncodedField {

	/**
	 * data encoded_field
		{
			uleb128 filed_idx_diff; // index into filed_ids for ID of this filed
			uleb128 accessFlags; // access flags like public, static etc.
		}
	 */
	public byte[] filed_idx_diff;
	public byte[] access_flags;

	@Override
	public String toString(){
		return "field_idx_diff:"+ Utils.bytesToHexString(filed_idx_diff)
                + ",accessFlags:"+Utils.bytesToHexString(filed_idx_diff);
	}

}
