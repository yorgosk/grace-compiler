	.file	"putc.c"
	.intel_syntax noprefix
	.text
	.globl	put_c
	.type	put_c, @function
put_c:
.LFB0:
	.cfi_startproc
	push	rbp
	.cfi_def_cfa_offset 16
	.cfi_offset 6, -16
	mov	rbp, rsp
	.cfi_def_cfa_register 6
	sub	rsp, 16
	mov	eax, edi
	mov	BYTE PTR [rbp-4], al
	movsx	eax, BYTE PTR [rbp-4]
	mov	edi, eax
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
