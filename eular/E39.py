#!/usr/bin/env python

res={}

for i in range(1, 1000):
    for j in range(i, 1000):
        for k in range(j, 1000):
            t = i+j+k;
            if t > 1000:
                continue;
            if k*k != i*i + j*j:
                continue;
            if t not in res:
                res[t] = 0;
            res[t] = res[t] + 1;

max_num = 0;

for (k, v) in res.items():
    if v > max_num:
        max_num = v;
        out = k;

print out
