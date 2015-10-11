#!/usr/local/bin/python

def gcd(num1,num2):
    if num2 == 0:
        return num1
    else:
        return gcd(num2,num1%num2)

def lcm(num1,num2):
    tmp = gcd(num1,num2)
    return num1*num2/tmp

r=1

for i in range(1, 20):
    r = lcm(r, i)

print r
