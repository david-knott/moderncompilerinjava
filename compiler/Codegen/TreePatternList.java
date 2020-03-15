package Codegen;

import Codegen.TreePatternList.Item;
import Tree.EXP;
import Tree.MEM;
import Tree.MOVE;
import Tree.Stm;

class TreePatternList {

    private Item items;

    class Item {
        Item next;
        TreePattern treePattern;
        TreePatternMatchAction action;

        public Item(TreePattern treePattern, TreePatternMatchAction action, Item next) {
            this.treePattern = treePattern;
            this.action = action;
            this.next = next;
        }
    }

    public TreePatternList add(TreePattern treePattern, TreePatternMatchAction action) {
        if(items == null){
            items = new Item(treePattern, action, null);
        } else {
            var last = items;
            for(;last.next != null; last = last.next);
            last.next = new Item(treePattern, action, null);
        }
        return this;
    }

    boolean doMatch(Stm stm){
        for(var item = items; item != null; item = item.next){
            if(item.treePattern.isMatch(stm)){
                item.action.execute(item.treePattern);
                return true;
            }
        }
        return false;
    }

    boolean match(MOVE move) {
        return doMatch(move);
    }

    boolean match(MEM mem) {
        for(var item = items; item != null; item = item.next){
            if(item.treePattern.isMatch(mem)){
                item.action.execute(item.treePattern);
                return true;
            }
        }
        return false;
    }

    boolean match(EXP mem) {
        return doMatch(mem);
    }
}