/*
 * [dma@bp860-10 ~]$ g++ -rdynamic t.cpp -o t
 * [dma@bp860-10 ~]$ ./t | c++filt
 * ./t(myfunc3()+0x1c)[0x4008c4]
 * ./t(myfunc()+0x9)[0x4008f9]
 * ./t(main+0x14)[0x400910] /lib64/tls/libc.so.6(__libc_start_main+0xdb)[0x3f37c1c40b]
 * ./t(__gxx_personality_v0+0x3a)[0x40081a]
 * backtrace() returned 5 addresses
 */

#include <execinfo.h>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>

void myfunc3(void)
{
    int j, nptrs;
    #define SIZE 100
    void *buffer[100];
    char **strings;

    nptrs = backtrace(buffer, SIZE);
    printf("backtrace() returned %d addresses\n", nptrs);

    backtrace_symbols_fd(buffer, nptrs, STDOUT_FILENO);
}

void myfunc(void)
{
    myfunc3();
}

int main(int argc, char *argv[])
{
    myfunc();
    return 0;
}
