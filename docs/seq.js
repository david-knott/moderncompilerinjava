function seq(left, right){
    var me = this;
    me.left = left;
    me.right = right;
}

function eseq(left, right){
    var me = this;
    me.left = left;
    me.right = right;
}

function list(item, tail){
    var me = this;
    me.item = item;
    me.tail = tail;
}


//builds a simple sequence tree, using list
//var l = new list('a', new list('b', new list('c', new list('d', new list('e')))));
var l = new list('a', null);
var s  = new seq(l.item, null);
l = l.tail;
var c = s;
var p = null;
var e = l;
while(e != null)
{
    p = c;
    var last = e.tail == null;
    if(!last){
        c = new seq(e.item, null); 
        p.right = c;
    }else{
        p.right = e.item;
    }
    e = e.tail;

}
console.log(JSON.stringify(s, null, 2));


//builds a esequence with sequence tree using list
var l = new list('a', new list('b', new list('c', new list('d', new list('e')))));
//subtree sequence reference
var s  = new seq(l.item, null);
//esequence reference
var es = new eseq(s, null);
l = l.tail;
var c = s;
var e = l;
var p = null;
while(e != null)
{
    p = c;
    if(e.tail == null){
        es.right = e.item;
        break;
    } else if(e.tail.tail  == null) {
        p.right = e.item;
    } else {
        c = new seq(e.item, null); 
        p.right = c;
    }
    e = e.tail;
}
console.log(JSON.stringify(es, null, 2));


//builds a new sequence in same order from an existing one
var l = new list('a', new list('b', new list('c', new list('d', new list('e')))));
var s  = new list(l.item, null);
l = l.tail;
var c = s;
var p = null;
var e = l;
do
{
    p = c;
    c = new list(e.item + '_', null); 
    p.tail= c;
    e = e.tail;

}
while(e != null);
console.log(JSON.stringify(s, null, 2));



//builds a new sequence in reverse order from an existing one
var l = new list('a', new list('b', new list('c', new list('d', new list('e')))));
var e = l;
var c = null;
do
{
    c = new list(e.item + '_', c); 
    e = e.tail;

}
while(e != null);
console.log(JSON.stringify(c, null, 2));

