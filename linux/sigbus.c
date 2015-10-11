/*
 * From wiki: http://en.wikipedia.org/wiki/Bus_error
 * $ gcc -ansi sigbus.c -o sigbus
 * $ ./sigbus 
 * Bus error
 * $ gdb ./sigbus
 * (gdb) r
 * Program received signal [[SIGBUS]], Bus error.
 * 0x080483ba in main ()
 * (gdb) x/i $pc
 * 0x80483ba <main+54>:    mov    DWORD PTR [eax],0x2a
 * (gdb) p/x $eax
 * $1 = 0x804a009
 * (gdb) p/t $eax & (sizeof(int) - 1)
 * $2 = 1
 */

#include <stdlib.h>
 
int main(int argc, char **argv) {
    int *iptr;
    char *cptr;
 
#if defined(__GNUC__)
# if defined(__i386__)
    /* Enable Alignment Checking on x86 */
    __asm__("pushf\norl $0x40000,(%esp)\npopf");
# elif defined(__x86_64__) 
     /* Enable Alignment Checking on x86_64 */
    __asm__("pushf\norl $0x40000,(%rsp)\npopf");
# endif
#endif
 
    /* malloc() always provides aligned memory */
    cptr = (char *) malloc(sizeof(int) + 1);
 
    /* Increment the pointer by one, making it misaligned */
    iptr = (int *) ++cptr;
 
    /* Dereference it as an int pointer, causing an unaligned access */
    *iptr = 42;
 
    return 0;
}
