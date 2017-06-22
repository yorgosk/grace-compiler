.intel_syntax noprefix # Use Intel syntax instead of AT&T
.text
name(prime) proc near
push bp
mov bp, sp
sub sp, size
load(ax, n)
load(dx, 0)
cmp ax, dx
instr label(?)
load(ax, n)
load(dx, 2)
cmp ax, dx
instr label(?)
load(ax, 2)
load(dx, 2)
cmp ax, dx
instr label(?)
load(ax, n)
cwd
load(cx, 2)
idiv cx
store(dx, $t2)
load(ax, 0)
load(dx, 0)
cmp ax, dx
instr label(?)
load(R, i)
store(R, 3)
load(ax, 2)
load(dx, $t3)
cmp ax, dx
instr label(?)
load(ax, n)
cwd
load(cx, i)
idiv cx
store(dx, $t4)
load(ax, 0)
load(dx, 0)
cmp ax, dx
instr label(?)
load(ax, i)
load(dx, 2)
add ax, dx
store(ax, $t5)
load(R, i)
store(R, $t5)
endof(prime): mov sp, bp
pop bp
ret
name(prime) endp
name(main) proc near
push bp
mov bp, sp
sub sp, size
load(R, limit)
store(R, $t6)
load(R, counter)
store(R, 0)
load(ax, 2)
load(dx, 2)
cmp ax, dx
instr label(?)
load(ax, counter)
load(dx, 1)
add ax, dx
store(ax, $t7)
load(R, counter)
store(R, $t7)
load(ax, 3)
load(dx, 3)
cmp ax, dx
instr label(?)
load(ax, counter)
load(dx, 1)
add ax, dx
store(ax, $t9)
load(R, counter)
store(R, $t9)
load(R, number)
store(R, 6)
load(ax, limit)
load(dx, limit)
cmp ax, dx
instr label(?)
load(ax, number)
load(dx, 1)
sub ax, dx
store(ax, $t11)
load(ax, null)
load(dx, 1)
cmp ax, dx
instr label(?)
load(ax, counter)
load(dx, 1)
add ax, dx
store(ax, $t13)
load(R, counter)
store(R, $t13)
load(ax, number)
load(dx, 1)
sub ax, dx
store(ax, $t14)
load(ax, limit)
load(dx, limit)
cmp ax, dx
instr label(?)
load(ax, number)
load(dx, 1)
add ax, dx
store(ax, $t15)
load(ax, null)
load(dx, 1)
cmp ax, dx
instr label(?)
load(ax, counter)
load(dx, 1)
add ax, dx
store(ax, $t17)
load(R, counter)
store(R, $t17)
load(ax, number)
load(dx, 1)
add ax, dx
store(ax, $t18)
load(ax, number)
load(dx, 6)
add ax, dx
store(ax, $t19)
load(R, number)
store(R, $t19)
endof(main): mov sp, bp
pop bp
ret
name(main) endp
.intel_syntax noprefix # Use Intel syntax instead of AT&T
.text
