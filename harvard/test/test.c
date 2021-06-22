#include <stdio.h>
#include <stdlib.h>

int main(int _cnt, char** args) {
  char buf[256];
  for (int i = 1; i < _cnt; i++) {
    sprintf(buf, "echo %s", args[i]);
    system(buf);
  }
} 
