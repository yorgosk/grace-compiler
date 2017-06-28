	.file	"gets.c"
	.section	.rodata
.LC0:
	.string	"%s"
	.text
	.globl	get_s
	.type	get_s, @function
get_s:
.LFB0:
	.cfi_startproc
	pushl	%ebp
	.cfi_def_cfa_offset 8
	.cfi_offset 5, -8
	movl	%esp, %ebp
	.cfi_def_cfa_register 5
	subl	$8, %esp
	movl	stdin, %eax
	subl	$4, %esp
	pushl	%eax
	pushl	8(%ebp)
	pushl	$.LC0
	call	fgets
	addl	$16, %esp
	nop
	leave
	.cfi_restore 5
	.cfi_def_cfa 4, 4
	ret
	.cfi_endproc
.LFE0:
	.size	get_s, .-get_s
	.ident	"GCC: (Ubuntu 5.4.0-6ubuntu1~16.04.4) 5.4.0 20160609"
	.section	.note.GNU-stack,"",@progbits
