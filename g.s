.intel_syntax noprefix # Use Intel syntax instead of AT&T
.text
name(reverse) proc near
push bp
mov bp, sp
sub sp, size
load(R, l)
store(R, $t1)
load(R, i)
store(R, 0)
load(ax, i)
load(dx, l)
cmp ax, dx
instr label(?)
load(ax, l)
load(dx, i)
sub ax, dx
store(ax, $t2)
load(ax, $t2)
load(dx, 1)
sub ax, dx
store(ax, $t3)
load(ax, s)
mov cx, size
imul cx
loadAddr(cx, $t3)
add ax, cx
store(ax, $t4)
load(ax, r)
mov cx, size
imul cx
loadAddr(cx, i)
add ax, cx
store(ax, $t5)
load(R, [$t5])
store(R, [$t4])
load(ax, i)
load(dx, 1)
add ax, dx
store(ax, $t6)
load(R, i)
store(R, $t6)
load(ax, r)
mov cx, size
imul cx
loadAddr(cx, i)
add ax, cx
store(ax, $t7)
load(R, [$t7])
store(R, '\0')
endof(reverse): mov sp, bp
pop bp
ret
name(reverse) endp
name(main) proc near
push bp
mov bp, sp
sub sp, size
endof(main): mov sp, bp
pop bp
ret
name(main) endp
.intel_syntax noprefix # Use Intel syntax instead of AT&T
.text
