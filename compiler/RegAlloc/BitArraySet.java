package RegAlloc;

import Temp.TempList;

public class BitArraySet {

    private static final int ALL_ONES = 0xFFFFFFFF;
    private static final int WORD_SIZE = 32;
    private int bits[] = null;
    private int capacity;

    public BitArraySet(int size) {
        capacity = size;
        bits = new int[size / WORD_SIZE + (size % WORD_SIZE == 0 ? 0 : 1)];
    }

    BitArraySet(int capacity, int[] bits) {
        this.capacity = capacity;
        this.bits = bits;
    }

    public BitArraySet(TempList tempList, int capacity) {
        this(capacity);
        for (var tp = tempList; tp != null; tp = tp.tail) {
            var temp = tp.head;
            var pos = temp.hashCode();
            setBit(pos, true);
        }
    }

    public boolean getBit(int pos) {
        return (bits[pos / WORD_SIZE] & (1 << (pos % WORD_SIZE))) != 0;
    }

    public void setBit(int pos, boolean b) {
        int word = bits[pos / WORD_SIZE];
        int posBit = 1 << (pos % WORD_SIZE);
        if (b) {
            word |= posBit;
        } else {
            word &= (ALL_ONES - posBit);
        }
        bits[pos / WORD_SIZE] = word;
    }

    public BitArraySet union(BitArraySet bitArraySet) {
        int[] union = new int[this.bits.length];
        for(var i = 0; i < union.length; i++){
            union[i] = this.bits[i] | bitArraySet.bits[i];
        }
        return new BitArraySet(capacity, union);
    }

    public BitArraySet difference(BitArraySet bitArraySet) {
        int[] diff = new int[this.bits.length];
        for(var i = 0; i < diff.length; i++){
            diff[i] = this.bits[i] & ~bitArraySet.bits[i];
        }
        return new BitArraySet(capacity, diff);
    }

    public BitArraySet intersection(BitArraySet bitArraySet) {
        int[] inter = new int[this.bits.length];
        for(var i = 0; i < inter.length; i++){
            inter[i] = this.bits[i] & bitArraySet.bits[i];
        }
        return new BitArraySet(capacity, inter);
    }

    public boolean equals(BitArraySet other) {
        for(int i = 0; i < bits.length; i++){
       //     System.out.println(bits[i] + " " + other.bits[i]);
            if(bits[i] != other.bits[i]){
                return false;
            }
        }
        return true; 
    }

    public String toString(){
        String s = "";
        for(int i = 0; i < capacity; i++){
            s+= this.getBit(i) ? i : "_";
            if(i < capacity - 1)
                s+= ",";
        }
        return s;
    }
}