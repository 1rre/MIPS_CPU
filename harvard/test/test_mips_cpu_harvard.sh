#!/bin/bash

mkdir -p .test
gcc test/test.c -o .test/test.bin
.test/test.bin $@