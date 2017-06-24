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
	pushq	%rbp
	.cfi_def_cfa_offset 16
	.cfi_offset 6, -16
	movq	%rsp, %rbp
	.cfi_def_cfa_register 6
	subq	$16, %rsp
	movl	%edi, -4(%rbp)
	movq	%rsi, -16(%rbp)
	movq	stdin(%rip), %rdx
	movl	-4(%rbp), %eax
	movl	%eax, %esi
	movl	$.LC0, %edi
	call	fgets
	nop
	leave
	.cfi_def_cfa 7, 8
	ret
	.cfi_endproc
.LFE0:
	.size	get_s, .-get_s
	.ident	"GCC: (Ubuntu 5.4.0-6ubuntu1~16.04.4) 5.4.0 20160609"
	.section	.note.GNU-stack,"",@progbits
