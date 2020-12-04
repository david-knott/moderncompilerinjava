.global tigermain
.data
L0:
	.long  0x4
	.ascii "\t\t\0"
.data
L1:
	.long  0x1
	.ascii "A\0"
.text
tigermain:
pushq %rbp
movq %rsp, %rbp
subq $0, %rsp
# start main
# start 
L3:
movq $L0, %rdi # default name
call print
movq $L1, %rdi # default name
call print
jmp L2
L2:
# sink 
# end main
movq %rbp, %rsp
popq %rbp
retq
