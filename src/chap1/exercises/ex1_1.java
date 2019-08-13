import javax.lang.model.util.ElementScanner6;

class Tree {
    Tree left;
    String key;
    Tree right;

    Tree(Tree l, String k, Tree r) {
        left = l;
        key = k;
        right = r;
    }

    public String toString(){
        String s = "[Tree ";

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
        var t1 = new Tree(null, "a", null);
        var t2 = insert("b", t1);
        var t3 = insert("c", t2);
        var t4 = insert("d", t3);
        System.out.println(t4);
    }
}