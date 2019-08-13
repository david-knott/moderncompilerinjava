class Tree {
    Tree left;
    String key;
    Tree right;

    Tree(Tree l, String k, Tree r) {
        left = l;
        key = k;
        right = r;
    }

    public boolean contains(String k){
        //does current tree have key ?
        if(k.equals(key))
            return true;
        //if tree has a right node, check it for the key
        if(right != null && right.contains(k))
            return true;
        //if the tree has a left node, check if for the key
        if(left != null && left.contains(k))
            return true;
        //no key.
        return false;
    }

    public String toString(){
        String s = "[Tree ";
        s+= " key=" + key;
        s+= "]";
        return s;
    }
}

class Ex1_1 {
    
    public static Tree insert(String key, Tree t) {
        if (t == null)
            return new Tree(null, key, null);
        else if (key.compareTo(t.key) < 0)
            return new Tree(insert(key, t.left), t.key, t.right);
        else if (key.compareTo(t.key) > 0)
            return new Tree(t.left, t.key, insert(key, t.right));
        else
            return new Tree(t.left, key, t.right);
    }

    public static void main(String[] args) {
        var t1 = new Tree(null, "e", null);
        var t2 = insert("a", t1);
        var t3 = insert("b", t2);
        var t4 = insert("c", t3);
        var t5 = insert("f", t4);
        var t6 = insert("g", t5);
        var t7 = insert("h", t6);
        
        System.out.println(t7.contains("x"));
        System.out.println(t7);
    }
}