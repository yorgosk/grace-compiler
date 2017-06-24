	.file	"strlen.c"
	.intel_syntax noprefix
	.text
	.globl	str_len
	.type	str_len, @function
str_len:
.LFB0:
	.cfi_startproc
	push	rbp
	.cfi_def_cfa_offset 16
	.cfi_offset 6, -16
	mov	rbp, rsp
	.cfi_def_cfa_register 6
	mov	QWORD PTR [rbp-24], rdi
	mov	DWORD PTR [rbp-8], 0
	mov	DWORD PTR [rbp-4], 0
	jmp	.L2
.L3:
	add	DWORD PTR [rbp-8], 1
	add	DWORD PTR [rbp-4], 1
.L2:
	mov	eax, DWORD PTR [rbp-4]
	movsx	rdx, eax
	mov	rax, QWORD PTR [rbp-24]
	add	rax, rdx
	movzx	eax, BYTE PTR [rax]
	test	al, al
	jne	.L3
	mov	eax, DWORD PTR [rbp-8]
	pop	rbp
	.cfi_def_cfa 7, 8
	ret
	.cfi_endproc
.LFE0:
	.size	str_len, .-str_len
	.ident	"GCC: (Ubuntu 5.4.0-6ubuntu1~16.04.4) 5.4.0 20160609"
	.section	.note.GNU-stack,"",@progbits
