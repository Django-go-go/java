package com.jkingone.parse_dex;

public class DexTypeItem {
    /*struct DexTypeItem {
        ushort  typeIdx;  // index into typeIds
    };*/

    public Short typeIdx;
    public String typeDescriptor;

    public static final int BYTE_LEN = 2;

    @Override
    public String toString() {
        return "DexTypeItem{" +
                "typeIdx=" + typeIdx +
                ", typeDescriptor='" + typeDescriptor + '\'' +
                '}';
    }
}
