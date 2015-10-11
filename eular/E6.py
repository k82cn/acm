#!/usr/local/bin/python

s1=0
s2=0

for i in range(1, 101):
    s1 += i*i
    s2 += i

s2 = s2*s2

print s2 - s1
