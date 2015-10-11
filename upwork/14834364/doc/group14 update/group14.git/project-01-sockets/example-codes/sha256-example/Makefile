#
# Jyke Savia 2009-2015
# Copyright: http://creativecommons.org/licenses/by-nc/1.0/fi/
#
# extra flags for the compiler
CCFLAGS = -Wall -Wextra -pedantic 
#
#
#ifeq ($(shell uname), Linux)
CCBIN=g++
#else # 
#$(error use lintulalinux host [korppi])
#endif

NETTLE=/Users/dma/codes/oDesk/14834364/nettle/
CFLAGS+=  -I$(NETTLE)/include
LDFLAGS+= -L$(NETTLE)/lib -Wl,-rpath,$(NETTLE)/lib


CC= $(CCBIN) $(CFLAGS)
LD= gcc $(LDFLAGS)

# -------------------------------------------------------------------------

.PHONY: default dist clean distclean

default: sha256example

sha256example: sha256example.o
	$(LD) $< -o $@ -lnettle

sha256example.o: sha256example.cc
	$(CC) -c $< -o $@

dist:	distclean
	(cd ..; zip -r sha256example.zip sha256example/ )

clean: 
	-rm -f *.o *~ 

distclean: clean
	-rm -f sha256example

