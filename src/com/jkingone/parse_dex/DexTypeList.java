package com.jkingone.parse_dex;

import java.util.ArrayList;
import java.util.List;

public class DexTypeList {
    /*struct DexTypeList {
        uint size;  // #of entries in list
        DexTypeItem list[1]; // entries
    }*/

    public static final int BYTE_LEN = 4;

    public int size;
    public List<DexTypeItem> dexTypeItems = new ArrayList<>();

    @Override
    public String toString() {
        return "DexTypeList{" +
                "size=" + size +
                ", dexTypeItems=" + dexTypeItems +
                '}';
    }

}
