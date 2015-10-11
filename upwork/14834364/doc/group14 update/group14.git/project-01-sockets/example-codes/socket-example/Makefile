# run with 'make' command in commandline
#
# compiler and extra flags for it
CC=g++ -Wall -Wextra -pedantic

# old Lintula used Solaris unix
ifeq ($(shell uname), SunOS)
LIBS=-lsocket -lnsl 
else # assume Linux
# for Linux we don't need any extra libraries (batteries included :)
LIBS=
endif

all: client server

client: client.cc Makefile
	$(CC) -o client client.cc $(LIBS)

server: server.cc Makefile
	$(CC) -o server server.cc $(LIBS)

dist: 
	( cd ..; zip -r socket-example.zip socket-example/ )
clean:
	rm -f client server *~ *.o

