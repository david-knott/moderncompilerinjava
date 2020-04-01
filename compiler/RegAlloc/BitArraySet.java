package RegAlloc;

import Temp.TempList;

public class BitArraySet {

    private static final int ALL_ONES = 0xFFFFFFFF;
    private static final int WORD_SIZE = 32;
    private int bits[] = null;
    private int capacity;

    public BitArraySet(int size) {
        capacity = size;
        //calculates the number of ints we need to store size bits,
        //where each int is 1 word in length
        //eg size = 10
        //we need 1 int for this
        //new int[1] => X,X,X,X,X,X,X,X,X,X.....0  32 bits
        //eg size = 32
        //we need 1 int for this
        //new int[1] => X,X,X,X,X,X,X,X,X,X.....0  32 bits
        //eg size = 33
        //we need 2 ints for this
        //we could use a ceiling function for this, however I suspect
        //this is quicker, get number of whole divisions and add
        //1 if there is a remainder from the division
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
        //get a specific bit using pos
        //first find which int we need to bit shift
        //we can find that from division, returns a word, or 32 bits
        //bitwise and those 32 bits with [0,...1] left shifted by the remainder
        //which is the offset within the int, so shift 0,0,0....1 by the offset
        //this basically moves the 1 in the second int to the position we are
        //interested in. We then & both, which returns 1 if the bit array has a 1
        //or a zero where it doesn't
        return (bits[pos / WORD_SIZE] & (1 << (pos % WORD_SIZE))) != 0;
    }

    public void setBit(int pos, boolean b) {
        //again we find which word we need to bit shift
        int word = bits[pos / WORD_SIZE];
        //create a bit mask by shifting the first bit to the
        //bit position we are interested in. [ 0,0,1,0,0]
        int posBit = 1 << (pos % WORD_SIZE);
        //if we want to set our bit to true
        if (b) {
            //or the bit with the word, 
            //word = word | posBit, if position is 0
            //its changed to 1, if its 1 it stays as 1
            word |= posBit;
        } else {
            //we want to change the bit to zero
            //ALL_ONES - posBit flips the bits in our mask
            //This means the item we are interested in is now 0 
            //in the mask
            //word = word & (ALL_ONES - posBit )
            //if position is 0 it stays as 0 ( 0 & 0 = 0)
            //if position is 1 it changes to 0 ( 0 & 1 = 0)
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
