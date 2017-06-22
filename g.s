.intel_syntax noprefix # Use Intel syntax instead of AT&T
.text
name(swap) proc near
push bp
mov bp, sp
sub sp, size
load(R, t)
store(R, x)
load(R, x)
store(R, y)
load(R, y)
store(R, t)
endof(swap): mov sp, bp
pop bp
ret
name(swap) endp
name(bsort) proc near
push bp
mov bp, sp
sub sp, size
load(R, changed)
store(R, 1)
load(ax, 0)
load(dx, 0)
cmp ax, dx
instr label(?)
load(R, changed)
store(R, 0)
load(R, i)
store(R, 0)
load(ax, n)
load(dx, 1)
sub ax, dx
store(ax, $t1)
load(ax, i)
load(dx, $t1)
cmp ax, dx
instr label(?)
load(ax, x)
mov cx, size
imul cx
loadAddr(cx, i)
add ax, cx
store(ax, $t2)
load(ax, i)
load(dx, 1)
add ax, dx
store(ax, $t3)
load(ax, x)
mov cx, size
imul cx
loadAddr(cx, $t3)
add ax, cx
store(ax, $t4)
load(ax, 1)
load(dx, $t4)
cmp ax, dx
instr label(?)
load(ax, x)
mov cx, size
imul cx
loadAddr(cx, i)
add ax, cx
store(ax, $t5)
load(ax, i)
load(dx, 1)
add ax, dx
store(ax, $t6)
load(ax, x)
mov cx, size
imul cx
loadAddr(cx, $t6)
add ax, cx
store(ax, $t7)
load(R, changed)
store(R, 1)
load(ax, i)
load(dx, 1)
add ax, dx
store(ax, $t8)
load(R, i)
store(R, $t8)
endof(bsort): mov sp, bp
pop bp
ret
name(bsort) endp
name(putArray) proc near
push bp
mov bp, sp
sub sp, size
load(R, i)
store(R, 0)
load(ax, i)
load(dx, n)
cmp ax, dx
instr label(?)
load(ax, 0)
load(dx, 0)
cmp ax, dx
instr label(?)
load(ax, x)
mov cx, size
imul cx
loadAddr(cx, i)
add ax, cx
store(ax, $t9)
load(ax, i)
load(dx, 1)
add ax, dx
store(ax, $t10)
load(R, i)
store(R, $t10)
endof(putArray): mov sp, bp
pop bp
ret
name(putArray) endp
name(main) proc near
push bp
mov bp, sp
sub sp, size
load(R, seed)
store(R, 65)
load(R, i)
store(R, 0)
load(ax, i)
load(dx, 16)
cmp ax, dx
instr label(?)
load(ax, seed)
load(cx, 137)
imul cx
store(ax, $t11)
load(ax, $t11)
load(dx, 220)
add ax, dx
store(ax, $t12)
load(ax, $t12)
load(dx, i)
add ax, dx
store(ax, $t13)
load(ax, $t13)
cwd
load(cx, 101)
idiv cx
store(dx, $t14)
load(R, seed)
store(R, $t14)
load(ax, x)
mov cx, size
imul cx
loadAddr(cx, i)
add ax, cx
store(ax, $t15)
load(R, [$t15])
store(R, seed)
load(ax, i)
load(dx, 1)
add ax, dx
store(ax, $t16)
load(R, i)
store(R, $t16)
endof(main): mov sp, bp
pop bp
ret
name(main) endp
.intel_syntax noprefix # Use Intel syntax instead of AT&T
.text
