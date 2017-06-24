	.file	"putc.c"
	.text
	.globl	put_c
	.type	put_c, @function
put_c:
.LFB0:
	.cfi_startproc
	pushq	%rbp
	.cfi_def_cfa_offset 16
	.cfi_offset 6, -16
	movq	%rsp, %rbp
	.cfi_def_cfa_register 6
	subq	$16, %rsp
	movl	%edi, %eax
	movb	%al, -4(%rbp)
	movsbl	-4(%rbp), %eax
	movl	%eax, %edi
	call	putchar
	nop
	leave
	.cfi_def_cfa 7, 8
	ret
	.cfi_endproc
.LFE0:
	.size	put_c, .-put_c
	.ident	"GCC: (Ubuntu 5.4.0-6ubuntu1~16.04.4) 5.4.0 20160609"
	.section	.note.GNU-stack,"",@progbits
