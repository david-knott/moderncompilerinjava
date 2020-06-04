.text
.global tigermain
tigermain:
movq $L1, %rdi
call print
.data
L1:  
.long 0x2
.ascii "hi"

