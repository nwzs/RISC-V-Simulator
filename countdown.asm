# countdown.asm
# Counts down from 5 to 0 using a loop
# Expected result: x1 = 0 (loop ran 5 times)

ADDI x1, x0, 5    # x1 = 5  (counter)
ADDI x2, x0, 1    # x2 = 1  (decrement amount)
SUB  x1, x1, x2   # x1 = x1 - 1
BNE  x1, x0, -8   # if x1 != 0, jump back to SUB
