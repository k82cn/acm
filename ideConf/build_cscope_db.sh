#!/bin/sh

FILES="cpp c proto cc hpp h idl inl java go"

rm -f cscope.files cscope.out

>cscope.files

for i in $FILES
do
  find `pwd` -name "*.$i" >> cscope.files
done

cscope -b

