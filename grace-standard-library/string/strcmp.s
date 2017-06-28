	.file	"strcmp.c"
	.text
	.globl	str_cmp
	.type	str_cmp, @function
str_cmp:
.LFB0:
	.cfi_startproc
	pushl	%ebp
	.cfi_def_cfa_offset 8
	.cfi_offset 5, -8
	movl	%esp, %ebp
	.cfi_def_cfa_register 5
	jmp	.L2
.L5:
	movl	8(%ebp), %eax
	movzbl	(%eax), %eax
	testb	%al, %al
	jne	.L3
	movl	$0, %eax
	jmp	.L4
.L3:
	addl	$1, 8(%ebp)
	addl	$1, 12(%ebp)
.L2:
	movl	8(%ebp), %eax
	movzbl	(%eax), %edx
	movl	12(%ebp), %eax
	movzbl	(%eax), %eax
	cmpb	%al, %dl
	je	.L5
	movl	8(%ebp), %eax
	movzbl	(%eax), %eax
	movsbl	%al, %edx
	movl	12(%ebp), %eax
	movzbl	(%eax), %eax
	movsbl	%al, %eax
	subl	%eax, %edx
	movl	%edx, %eax
.L4:
	popl	%ebp
	.cfi_restore 5
	.cfi_def_cfa 4, 4
	ret
	.cfi_endproc
.LFE0:
	.size	str_cmp, .-str_cmp
	.ident	"GCC: (Ubuntu 5.4.0-6ubuntu1~16.04.4) 5.4.0 20160609"
	.section	.note.GNU-stack,"",@progbits
