	.file	"strcat.c"
	.text
	.globl	str_cat
	.type	str_cat, @function
str_cat:
.LFB0:
	.cfi_startproc
	pushl	%ebp
	.cfi_def_cfa_offset 8
	.cfi_offset 5, -8
	movl	%esp, %ebp
	.cfi_def_cfa_register 5
	jmp	.L2
.L3:
	addl	$1, 8(%ebp)
.L2:
	movl	8(%ebp), %eax
	movzbl	(%eax), %eax
	testb	%al, %al
	jne	.L3
	nop
.L4:
	movl	8(%ebp), %eax
	leal	1(%eax), %edx
	movl	%edx, 8(%ebp)
	movl	12(%ebp), %edx
	leal	1(%edx), %ecx
	movl	%ecx, 12(%ebp)
	movzbl	(%edx), %edx
	movb	%dl, (%eax)
	movzbl	(%eax), %eax
	testb	%al, %al
	jne	.L4
	nop
	popl	%ebp
	.cfi_restore 5
	.cfi_def_cfa 4, 4
	ret
	.cfi_endproc
.LFE0:
	.size	str_cat, .-str_cat
	.ident	"GCC: (Ubuntu 5.4.0-6ubuntu1~16.04.4) 5.4.0 20160609"
	.section	.note.GNU-stack,"",@progbits
