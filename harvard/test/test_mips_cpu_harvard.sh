#!/bin/bash
rm -rf test/build
mkdir -p test/build
gcc test/test.c -o test/build/test.bin
test/build/test.bin $@