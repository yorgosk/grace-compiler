.intel_syntax noprefix # Use Intel syntax instead of AT&T
.text
endof(swap): mov sp, bp
pop bp
ret
name(swap) endp
load(ax, 0)
load(dx, 0)
cmp ax, dx
instr label(?)
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
load(ax, i)
load(dx, 1)
add ax, dx
store(ax, $t8)
endof(bsort): mov sp, bp
pop bp
ret
name(bsort) endp
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
endof(putArray): mov sp, bp
pop bp
ret
name(putArray) endp
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
load(ax, x)
mov cx, size
imul cx
loadAddr(cx, i)
add ax, cx
store(ax, $t15)
load(ax, i)
load(dx, 1)
add ax, dx
store(ax, $t16)
endof(main): mov sp, bp
pop bp
ret
name(main) endp
.intel_syntax noprefix # Use Intel syntax instead of AT&T
.text
@1:
name(_swap_1) proc near
push bp
mov bp, sp
sub sp, 8
@2:
mov R, t
mov size ptr [bp+offset], R
@3:
mov R, x
mov si, word ptr [bp+offset]
mov size ptr [si], R
@4:
mov R, y
mov si, word ptr [bp+offset]
mov size ptr [si], R
@6:
name(_bsort_6) proc near
push bp
mov bp, sp
sub sp, 8
@7:
mov R, changed
mov size ptr [bp+offset], R
@10:
mov R, changed
mov size ptr [bp+offset], R
@11:
mov R, i
mov size ptr [bp+offset], R
@26:
mov R, changed
mov size ptr [bp+offset], R
@29:
mov R, i
mov size ptr [bp+offset], R
@33:
name(_putArray_33) proc near
push bp
mov bp, sp
sub sp, 8
@36:
mov R, i
mov size ptr [bp+offset], R
@48:
mov R, i
mov size ptr [bp+offset], R
@53:
name(_main_53) proc near
push bp
mov bp, sp
sub sp, 8
@54:
mov R, seed
mov size ptr [bp+offset], R
@55:
mov R, i
mov size ptr [bp+offset], R
@62:
mov R, seed
mov size ptr [bp+offset], R
@64:
mov R, [$t15]
mov size ptr [bp+offset], R
@66:
mov R, i
mov size ptr [bp+offset], R
