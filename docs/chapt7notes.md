### Sequence Expression

(e1, e2, e3, e4, e5, e6 ) where sequence evaluates to e6

  eseq(
    seq(
       e1,
       seq(
          e2,
          seq(
             e3,
             seq(
                e4,
                e5
             )
          )
       )
    ),
    e6
  )

for e1 -> e6 

seq s1 = new seq(e1, null)

seq s2 = new seq(e2, null)
s1.right = s2

seq s3 = new seq(e2, null)
s2.right = s3

etc

this doesn't keep a record of the first item

var r = null, p = null, c  = null
for each ei
 p = c
 if not last(ei)
   c = new seq(ei, null)
 else
    p = c
 if p != null
   p.right = c
   
perhaps we should use a do while loop
var c = new seq(e0)
var p = null
var e = e0
do
{
  p = c
  var last = e->tail == null;
  if not last
    c = new seq(s, null)
  else
    p = c
  if p != null
    p.right = c
  e = e->tail;
}
while(e != null)
