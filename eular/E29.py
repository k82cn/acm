#!/usr/bin/env python

res = {};

for i in range(2, 101):
  for j in range(2, 101):
    t = i ** j;
    res[t] = 1;

print len(res);
