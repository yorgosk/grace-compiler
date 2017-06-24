	.file	"abs.c"
	.intel_syntax noprefix
	.text
	.globl	abs
	.type	abs, @function
abs:
.LFB0:
	.cfi_startproc
	push	rbp
	.cfi_def_cfa_offset 16
	.cfi_offset 6, -16
	mov	rbp, rsp
	.cfi_def_cfa_register 6
	mov	DWORD PTR [rbp-4], edi
	cmp	DWORD PTR [rbp-4], 0
	js	.L2
	mov	eax, DWORD PTR [rbp-4]
	jmp	.L3
.L2:
	mov	eax, DWORD PTR [rbp-4]
	neg	eax
.L3:
	pop	rbp
	.cfi_def_cfa 7, 8
	ret
	.cfi_endproc
.LFE0:
	.size	abs, .-abs
	.ident	"GCC: (Ubuntu 5.4.0-6ubuntu1~16.04.4) 5.4.0 20160609"
	.section	.note.GNU-stack,"",@progbits
