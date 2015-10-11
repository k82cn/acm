Description
=================================
The goal of this project is to implement a simple URL-checking service for authenticated users.
Network traffic has two parts: authentication and URL-queries. Authentication part is done in TCP-based protocol and queries using UDP-packets.

Features
=================================
1. TCP/UDP protocol for data exchange
2. Use AUTH1 to do authentication
3. Server handles multiple client concurrently

Source code
=================================
client.cpp/client.hpp  The source code of client
server.cpp/server.hpp  The source code of server
netutil.cpp/netutil.hpp The source code of socket class, such as TCP and UDP.

How to make
=================================
Update Makefile:

NETTLE=the_path_of_nettle

MODE=DEBUG
or
MODE=RELEASE

run commend to build binaries of client & server
$ make // make both client and server

or

$ make client // make client only

or

$ make server // make server only

How to run
=================================

The user database are:
    Jack 12345
    Alicia 4567
    Rose abcde
    
start server by:
./server --port 28080 --auth1

run client by:
$ ./client --server localhost --port 28080 --user Jack --pwd 12345 --query http://www.baidu.com --auth1

