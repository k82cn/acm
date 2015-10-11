#ifndef __NET_UTIL_H__
#define __NET_UTIL_H__

extern "C" {
#include <sys/types.h>
#include <sys/socket.h>
#include <stdlib.h>
#include <stdio.h>
#include <unistd.h>
#include <netdb.h>
#include <string.h>
#include <errno.h>

#include <stdio.h>

}

#include <string>

#define PRO_LABEL "DISTRIB2015"
#define PRO_LABEL_LEN 11

#define PRO_DEBUG 'D'
#define PRO_NORMAL 'N'
#define PRO_AUTH1 '1'

#define PRO_TCP_HEADER_LEN 14

#define PRO_UDP_MAX_LEN 2000


#define info(fmt,...) do {                                         \
    fprintf(stderr,"[ INFO ] : [ %s, %d ] ",__FILE__,__LINE__);    \
    fprintf(stderr,fmt,##__VA_ARGS__);                              \
    fprintf(stderr,"\n");                                           \
    fflush(stderr); \
} while(0)

#ifdef DEBUG
#define debug(fmt,...) do {                                         \
    fprintf(stderr,"[ DEBUG ] : [ %s, %d ] ",__FILE__,__LINE__);    \
    fprintf(stderr,fmt,##__VA_ARGS__);                              \
    fprintf(stderr,"\n");                                           \
    fflush(stderr); \
} while(0)
#else
#define debug(fmt,...) do {} while(0)
#endif



struct Command
{
    char status;
    char sha[64];
    char mode;
    uint16_t step;

    uint32_t sid;
    uint32_t rn;
    std::string user;
    std::string pwd;

    Command(char m) :
        mode(m)
    {
        rn = get_random_number();
    }

    uint16_t length();

    uint32_t get_random_number();

    void update_sha();

    int serialize(char* buf);
    int deserialize(const char* buf);

};

struct TcpMessage
{
    uint16_t len;
    char label[11];
    char mode;
    Command cmd;

    uint16_t length();

    TcpMessage(char m) :
        len(-1), mode(m), cmd(m)
    {
        memcpy(label, "DISTRIB2015", 11);
    }

    int serialize(char* buf);
    int deserialize(const char* buf, int len);

};

struct UdpMessage
{
    char ver;
    char dir;
    char status;

    uint16_t len;
    uint32_t sid;
    uint32_t tid;

    uint32_t timestamp;
    std::string url;
    std::string mac_sha;

    int serialize(char* buf);
    int deserialize(const char* buf, int len);

};

class UdpSocket
{
    public:
        UdpSocket();
        ~UdpSocket();

        int connect(const char * host, int port);

        int get_port();

        int send(const void* buf, int len);
        int recv(void* buf, int len);

    private:
        int sockfd;
        int port;
        struct sockaddr_in peer;
};

class Connection
{
    public:
        Connection(int skt);
        Connection(const char* hostname, int port);

        ~Connection();

        uint16_t recv_uint16();
        int send(void* buf, int len);
        int recv(void* buf, int len);

    private:
        int sockfd;
};

class ServerSocket
{
    public:
        /*
         * The constructor of ServerSocket; it will start a server socket at port
         */
        ServerSocket(int port);
        /*
         * The de-constructor of ServerSocket, it will close the socket
         */
        ~ServerSocket();
        // The accept function that waiting client to connect
        int accept();
    private:
        int sockfd;
};

void get_sha256(char* sha, std::string& pwd, uint32_t rn);

#endif
