#
# Jyke Savia 2009-2015
# Copyright: http://creativecommons.org/licenses/by-nc/1.0/fi/
#
# extra flags for the compiler
CCFLAGS = -Wall -Wextra -pedantic 
#
#
ifeq ($(shell uname), Linux)
CCBIN=g++
else # 
$(error use lintulalinux host [korppi])
endif

NETTLE=/usr/local/lang/nettle-3.0
CFLAGS+=  -I$(NETTLE)/include
LDFLAGS+= -L$(NETTLE)/lib64 -Wl,-rpath,$(NETTLE)/lib64



CC= $(CCBIN) $(CFLAGS)
LD= gcc $(LDFLAGS)

# -------------------------------------------------------------------------

.PHONY: default dist clean distclean

default: hmacsha256esimerkki

hmacsha256esimerkki: hmacsha256esimerkki.o
	$(LD) $< -o $@ -lnettle

hmacsha256esimerkki.o: hmacsha256esimerkki.cc
	$(CC) -c $< -o $@

dist:	distclean
	(cd ..; zip -r hmacsha256esimerkki.zip hmacsha256esimerkki/ )

clean: 
	-rm -f *.o *~ 

distclean: clean
	-rm -f hmacsha256esimerkki

