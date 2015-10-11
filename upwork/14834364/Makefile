# run with 'make' command in commandline
#
# compiler and extra flags for it

NETTLE=/Users/dma/nettle

CFLAGS+= -w
LDFLAGS+= -lpthread

CC=g++ -Wall -Wextra -pedantic $(CFLAGS)
LD= gcc $(LDFLAGS)

MODE=DEBUG
#MODE=RELEASE

# old Lintula used Solaris unix
ifeq ($(shell uname), SunOS)
LIBS=-lsocket -lnsl
else # assume Linux
# for Linux we don't need any extra libraries (batteries included :)
LIBS=
endif

all: client server

client: client.cpp netutil.cpp
	$(CC) -o client client.cpp netutil.cpp -g $(LIBS) -D${MODE} ${LDFLAGS} -lnettle

server: server.cpp netutil.cpp
	$(CC) -o server server.cpp netutil.cpp -g $(LIBS) -D${MODE} ${LDFLAGS} -lnettle

dist:
	( cd ..; zip -r socket-example.zip socket-example/ )

clean:
	rm -f client server *~ *.o *.orig

