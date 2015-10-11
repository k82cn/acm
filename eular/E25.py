#!/usr/bin/env python

fn1 = 1;
fn2 = 1;
res = 3;

while True:
    fn = fn1 + fn2;
    if len(str(fn)) >= 1000:
        break;
    fn2 = fn1;
    fn1 = fn;
    res = res + 1;

print res;
