	.file	"strcmp.c"
	.intel_syntax noprefix
	.text
	.globl	str_cmp
	.type	str_cmp, @function
str_cmp:
.LFB0:
	.cfi_startproc
	push	rbp
	.cfi_def_cfa_offset 16
	.cfi_offset 6, -16
	mov	rbp, rsp
	.cfi_def_cfa_register 6
	mov	QWORD PTR [rbp-8], rdi
	mov	QWORD PTR [rbp-16], rsi
	jmp	.L2
.L5:
	mov	rax, QWORD PTR [rbp-8]
	movzx	eax, BYTE PTR [rax]
	test	al, al
	jne	.L3
	mov	eax, 0
	jmp	.L4
.L3:
	add	QWORD PTR [rbp-8], 1
	add	QWORD PTR [rbp-16], 1
.L2:
	mov	rax, QWORD PTR [rbp-8]
	movzx	edx, BYTE PTR [rax]
	mov	rax, QWORD PTR [rbp-16]
	movzx	eax, BYTE PTR [rax]
	cmp	dl, al
	je	.L5
	mov	rax, QWORD PTR [rbp-8]
	movzx	eax, BYTE PTR [rax]
	movsx	edx, al
	mov	rax, QWORD PTR [rbp-16]
	movzx	eax, BYTE PTR [rax]
	movsx	eax, al
	sub	edx, eax
	mov	eax, edx
.L4:
	pop	rbp
	.cfi_def_cfa 7, 8
	ret
	.cfi_endproc
.LFE0:
	.size	str_cmp, .-str_cmp
	.ident	"GCC: (Ubuntu 5.4.0-6ubuntu1~16.04.4) 5.4.0 20160609"
	.section	.note.GNU-stack,"",@progbits
