	.file	"strcpy.c"
	.intel_syntax noprefix
	.text
	.globl	str_cpy
	.type	str_cpy, @function
str_cpy:
.LFB0:
	.cfi_startproc
	push	rbp
	.cfi_def_cfa_offset 16
	.cfi_offset 6, -16
	mov	rbp, rsp
	.cfi_def_cfa_register 6
	mov	QWORD PTR [rbp-8], rdi
	mov	QWORD PTR [rbp-16], rsi
	nop
.L2:
	mov	rax, QWORD PTR [rbp-8]
	lea	rdx, [rax+1]
	mov	QWORD PTR [rbp-8], rdx
	mov	rdx, QWORD PTR [rbp-16]
	lea	rcx, [rdx+1]
	mov	QWORD PTR [rbp-16], rcx
	movzx	edx, BYTE PTR [rdx]
	mov	BYTE PTR [rax], dl
	movzx	eax, BYTE PTR [rax]
	test	al, al
	jne	.L2
	nop
	pop	rbp
	.cfi_def_cfa 7, 8
	ret
	.cfi_endproc
.LFE0:
	.size	str_cpy, .-str_cpy
	.ident	"GCC: (Ubuntu 5.4.0-6ubuntu1~16.04.4) 5.4.0 20160609"
	.section	.note.GNU-stack,"",@progbits
