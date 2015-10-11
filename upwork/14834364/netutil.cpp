/* Server code template using socket API in C++
 * Jyke Savia 2008-2015
 * License http://creativecommons.org/licenses/by-nc/2.0/
 */

// include C library headers as externC:
extern "C" {
#include <sys/types.h>
#include <sys/socket.h> /* socket api */
#include <netinet/in.h> /* inetaddr structs */

#include <string.h> /* memset(), memcopy() */
#include <stdio.h> /* perror() */
#include <unistd.h> /* close() */

#include <stdlib.h>
#include <stdint.h>
#include <time.h>
#include <assert.h>

#include <arpa/inet.h>  // htonl()

#include "nettle/sha.h"

}

#include <cstdlib> // exit()
#include <vector>
#include <string>
#include <iostream>
#include <sstream>

#include "netutil.hpp"

static void panic(std::string message, int exitval)
{
    std::cerr << "error: " << message << std::endl;
    exit(exitval);
}

int UdpMessage::serialize(char* buf)
{
    int len = 0;
    memcpy(buf, &ver, 1);
    len += 1;
    memcpy(buf + len, &dir, 1);
    len += 1;
    memcpy(buf + len, &len, 2);
    len += 2;
    memcpy(buf + len, &sid, 4);
    len += 4;
    memcpy(buf + len, &tid, 4);
    len += 4;

    if (dir == 'S')
    {
        uint16_t urllen = htons(url.length());
        memcpy(buf + len, &urllen, 2);
        len += 2;
        memcpy(buf + len, url.c_str(), url.length());
        len += url.length();
    }
    else if (dir == 'C')
    {
        uint32_t ttn = htonl(timestamp);
        memcpy(buf + len, &ttn, 4);
        len += 4;
        len += 1;
        memcpy(buf + len, &status, 1);
        len += 1;
    }

    uint16_t maclen = htons(mac_sha.length());
    memcpy(buf + len, &maclen, 2);
    len += 2;
    memcpy(buf + len, mac_sha.c_str(), mac_sha.length());
    len += mac_sha.length();

    debug(
        "Udp -- ver: <%c>, dir: <%c>, len: <%d>, sid: <%d>, tid: <%d>, url: <%s>, mac: <%s>, timestamp: <%d>, status: <%d>",
        ver, dir, this->len, sid, tid, url.c_str(), mac_sha.c_str(), timestamp,
        status);

    return len;
}

int UdpMessage::deserialize(const char* buf, int buflen)
{
    int len = 0;
    memcpy(&ver, buf, 1);
    len += 1;
    memcpy(&dir, buf + len, 1);
    len += 1;
    memcpy(&len, buf + len, 2);
    len += 2;
    memcpy(&sid, buf + len, 4);
    len += 4;
    memcpy(&tid, buf + len, 4);
    len += 4;

    if (dir == 'S')
    {
        uint16_t urllen;
        memcpy(&urllen, buf + len, 2);
        urllen = ntohs(urllen);
        len += 2;

        char* urlbuf = (char*) malloc(urllen + 1);
        memcpy(urlbuf, buf + len, urllen);
        url.assign(urlbuf, urlbuf + urllen);
        len += urllen;
        free(urlbuf);

    }
    else if (dir == 'C')
    {
        memcpy(&timestamp, buf + len, 4);
        timestamp = ntohl(timestamp);
        len += 4;
        // ignore Not used byte
        len += 1;
        memcpy(&status, buf + len, 1);
        len += 1;
    }

    uint16_t maclen;
    memcpy(&maclen, buf + len, 2);
    maclen = ntohs(maclen);
    len += 2;

    char* macbuf = (char*) malloc(maclen + 1);
    memcpy(macbuf, buf + len, maclen);
    mac_sha.assign(macbuf, macbuf + maclen);
    len += maclen;

    debug(
        "Udp -- ver: <%c>, dir: <%c>, len: <%d>, sid: <%d>, tid: <%d>, url: <%s>, mac: <%s>, timestamp: <%d>, status: <%d>",
        ver, dir, this->len, sid, tid, url.c_str(), mac_sha.c_str(), timestamp,
        status);

    return len;
}

int UdpSocket::connect(const char* host, int port)
{
    bzero(&peer, sizeof(peer));
    peer.sin_family = AF_INET;

    // string hostname -> ip-address
    {
        struct hostent *hp;
        if ((hp = gethostbyname(host)) == NULL)
        {
            perror("gethostbyname()");
            panic("gethostbyname()", 9);
        }
        bcopy(reinterpret_cast<char *>(&peer.sin_addr),
              reinterpret_cast<char *>(hp->h_addr), hp->h_length);
    }

    peer.sin_port = htons(port);
    return 1;
}

UdpSocket::UdpSocket()
{
    struct sockaddr_in adr_inet;
    int rc, snd_size, rcv_size, optlen;

    //建立socket
    sockfd = socket(AF_INET, SOCK_DGRAM, 0);
    if (sockfd == -1)
    {
        perror("socket()");
        panic("socket()", 1);
    }

    adr_inet.sin_family = AF_INET;
    adr_inet.sin_port = 0;
    adr_inet.sin_addr.s_addr = htonl(INADDR_ANY);
    bzero(&(adr_inet.sin_zero), 8);

    rc = ::bind(sockfd, (struct sockaddr *) &adr_inet, sizeof(adr_inet));
    if (rc == -1)
    {
        perror("bind()");
        panic("bind()", 1);
    }

    socklen_t n = sizeof(adr_inet);
    memset(&adr_inet, 0, n);
    rc = getsockname(sockfd, (struct sockaddr*) &adr_inet, &n);
    port = ntohs(adr_inet.sin_port);

    if (rc < 0)
    {
        perror("get socket name");
    }

    debug("UDP socket: <%d>", sockfd);

    snd_size = 65535;
    optlen = sizeof(snd_size);
    rc = setsockopt(sockfd, SOL_SOCKET, SO_SNDBUF, &snd_size, optlen);
    if(rc < 0)
    {
        perror("set send buf");
        panic("set send buf", rc);
    }

    debug("UDP socket: <%d>", sockfd);

    rcv_size = 65535;
    optlen = sizeof(rcv_size);
    rc = setsockopt(sockfd, SOL_SOCKET, SO_RCVBUF, (char *)&rcv_size, optlen);
    if(rc < 0)
    {
        perror("set receive buf");
        panic("set receive buf", rc);
    }
    debug("UDP socket: <%d>", sockfd);

}

UdpSocket::~UdpSocket()
{
    debug("close udp socket <%d>", sockfd);
    close(sockfd);
}

int UdpSocket::get_port()
{
    debug("UDP socket: <%d>", sockfd);

    return port;
}

int UdpSocket::send(const void* buf, int len)
{
    debug("UDP socket: <%d>", sockfd);

    int rc = sendto(sockfd, buf, len, 0, (struct sockaddr *) &peer, sizeof(peer));
    if (rc < 0)
    {
        perror("send udp");
    }
    return rc;
}
int UdpSocket::recv(void* buf, int len)
{
    debug("UDP socket: <%d>", sockfd);

    socklen_t peer_len = -1;
    int rc = recvfrom(sockfd, buf, len, 0, (struct sockaddr *) &peer, &peer_len);
    if (rc < 0)
    {
        perror("recv udp");
    }
    return rc;
}

ServerSocket::ServerSocket(int listen_port)
{
// -- initialize a TCP/IP socket
    sockfd = socket(AF_INET, SOCK_STREAM, 0);
    if (sockfd < 0)
    {
        perror("socket()");
        panic("socket()", 1);
    }

// -- listening socket struct
    sockaddr_in server_addr;
    memset(reinterpret_cast<char *>(&server_addr), 0, sizeof(server_addr));
    server_addr.sin_family = AF_INET;
    server_addr.sin_addr.s_addr = htonl(INADDR_ANY);
    server_addr.sin_port = htons(listen_port);
    if (bind(sockfd, reinterpret_cast<struct sockaddr *>(&server_addr),
             sizeof(server_addr)) < 0)
    {
        perror("bind()");
        panic("bind()", 2);
    }

// -- start accepting incoming connections
    listen(sockfd, 5);
//std::clog << args[0] << " listening on port: " << listen_port << std::endl;
}

int ServerSocket::accept()
{
    sockaddr_in client_addr;
    socklen_t client_len = sizeof(client_addr);
    int newsockfd = ::accept(sockfd, reinterpret_cast<sockaddr*>(&client_addr),
                             &client_len);
    if (newsockfd < 0)
    {
        perror("accept()");
        panic("accept()", 7);
    }
    return newsockfd;
}

ServerSocket::~ServerSocket()
{
    close(sockfd);  // close the listening socket
}

Connection::Connection(const char* connect_host, int connect_port)
{
// -- create a TCP/IP socket
    sockfd = socket(AF_INET, SOCK_STREAM, 0);
    if (sockfd < 0)
    {
        perror("socket()");
        panic("socket()", 1);
    }

// -- init destination address struct
    sockaddr_in connect_addr;
    memset(reinterpret_cast<char *>(&connect_addr), 0, sizeof(connect_addr));
    connect_addr.sin_family = AF_INET;
    connect_addr.sin_port = htons(connect_port);

// string hostname -> ip-address
    {
        struct hostent *hp;
        if ((hp = gethostbyname(connect_host)) == NULL)
        {
            perror("gethostbyname()");
            panic("gethostbyname()", 9);
        }
        bcopy(reinterpret_cast<char *>(&connect_addr.sin_addr),
              reinterpret_cast<char *>(hp->h_addr), hp->h_length);
    }

// make the connection to the server
    if (connect(sockfd, reinterpret_cast<struct sockaddr *>(&connect_addr),
                sizeof(connect_addr)) < 0)
    {
        perror("connect()");
        panic("connect()", 8);
    }
}

Connection::Connection(int skt)
{
    this->sockfd = skt;
}

Connection::~Connection()
{
    close(sockfd);  // close connection
}

uint16_t Connection::recv_uint16()
{
    uint16_t h;

    ::recv(sockfd, &h, 2, 0);

    return ntohs(h);
}

int Connection::send(void* buf, int len)
{
    return ::send(sockfd, buf, len, 0);
}

int Connection::recv(void* buf, int len)
{
    return ::recv(sockfd, buf, len, 0);
}

static const char* STEP_LABEL[] = { "A0", "A1", "A2", "A3" };

uint16_t Command::length()
{
    uint16_t totalLen = 0;

    switch (step)
    {
    case 0:
    {
        totalLen = 3 + user.length();
        break;
    }
    case 1:
    {
        totalLen = 6;
        break;
    }
    case 2:
    {
        totalLen = 66;
        break;
    }
    case 3:
    {
        totalLen = 7;
        break;
    }
    default:
        ::panic("unknown auth step", step);
        break;
    }

    return totalLen;
}

int Command::serialize(char* buf)
{

    int totalLen = -1;
    memcpy(buf, STEP_LABEL[step], 2);

    switch (step)
    {
    case 0:
    {
        char userLen = user.length();
        memcpy(buf + 2, &userLen, 1);
        memcpy(buf + 2 + 1, user.c_str(), userLen);
        totalLen = 3 + user.length();
        debug("CMD -- step: <%s>, user: <%s>, total length: <%d>",
              STEP_LABEL[step], user.c_str(), totalLen);
        break;
    }
    case 1:
    {
        uint32_t data = htonl(rn);

        memcpy(buf + 2, &data, 4);
        totalLen = 6;
        debug("CMD -- step: <%s>, random number: <%ul>, total length: <%d>",
              STEP_LABEL[step], rn, totalLen);
        break;
    }
    case 2:
    {
        update_sha();
        memcpy(buf + 2, sha, 64);
        totalLen = 66;
        debug("CMD -- step: <%s>, sha: <%s>, total length: <%d>",
              STEP_LABEL[step], sha, totalLen);

        break;
    }
    case 3:
    {
        uint32_t data = htonl(sid);
        memcpy(buf + 2, &status, 1);
        memcpy(buf + 2 + 1, &data, 4);
        totalLen = 7;
        debug("CMD -- step: <%s>, status: <%d>, SID: <%d>, total length: <%d>",
              STEP_LABEL[step], status, sid, totalLen);
        break;
    }
    default:
        ::panic("unknown auth step", step);
        break;
    }

    return totalLen;
}

int Command::deserialize(const char* buf)
{

    int totalLen = -1;
    {
        // get step;
        char stepStr[3] = { 0 };
        memcpy(stepStr, buf, 2);
        step = stepStr[1] - '0';
    }

    switch (step)
    {
    case 0:
    {
        char username[128] = { 0 };
        char userLen;
        memcpy(&userLen, buf + 2, 1);
        memcpy(username, buf + 2 + 1, userLen);
        user = username;
        totalLen = 3 + userLen;
        debug("CMD -- step: <%s>, user: <%s>, total length: <%d>",
              STEP_LABEL[step], username, totalLen);
        break;
    }
    case 1:
    {
        memcpy(&rn, buf + 2, 4);
        rn = ntohl(rn);
        totalLen = 6;
        debug("CMD -- step: <%s>, random number: <%ul>, total length: <%d>",
              STEP_LABEL[step], rn, totalLen);
        break;
    }
    case 2:
    {
        memcpy(sha, buf + 2, 64);
        totalLen = 66;
        debug("CMD -- step: <%s>, sha: <%s>, total length: <%d>",
              STEP_LABEL[step], sha, totalLen);

        break;
    }
    case 3:
        memcpy(&status, buf + 2, 1);
        memcpy(&sid, buf + 2 + 1, 4);
        sid = ntohl(sid);
        totalLen = 7;
        debug("CMD -- step: <%s>, status: <%d>, SID: <%d>, total length: <%d>",
              STEP_LABEL[step], status, sid, totalLen);
        break;
    default:
        ::panic("unknown auth step", step);
        break;
    }

    return totalLen;
}

uint32_t Command::get_random_number()
{
    if (mode == '1')
    {
        uint32_t i;
        FILE* rawdata = NULL;

        rawdata = fopen("/dev/urandom", "r");
        if (rawdata == NULL)
        {
            perror("/dev/urandom");
            exit(1);
        }

        if (fread(&i, 1, 4, rawdata) != 4)
        {
            perror("urandom fread()");
            exit(1);
        }
        fclose(rawdata);
        return i;
    }
    return 123456;
}

/* max size for a password in operations */
#define PASSWORD_LEN 20

/* max buffer size (password + 4 byte random number)  */
#define DATA_MAX PASSWORD_LEN + 4

/* sha256-digest size in ascii-hex format */
#define DIGEST_ASCII (SHA256_DIGEST_SIZE*2+1)

void get_sha256(char* sha, std::string& pwd, uint32_t rn)
{
    /* the binary result */
    unsigned char digest[SHA256_DIGEST_SIZE];
    /* SHA256 using Nettle library */
    {
        struct sha256_ctx sha256_data; /* data structure to collect bytes */
        sha256_init(&sha256_data); /* init */
        int pwd_len = pwd.length();
//        /* add password bytes */
        sha256_update(&sha256_data, pwd_len,
                      reinterpret_cast<uint8_t*>(const_cast<char*>(pwd.c_str())));
//        /* add 4 bytes of the random number */
        sha256_update(&sha256_data, 4,
                      reinterpret_cast<uint8_t*>(const_cast<uint32_t*>(&rn)));
//        /* calculate SHA256 digest from the data */
        sha256_digest(&sha256_data, SHA256_DIGEST_SIZE, digest);
    }
    /* muutetaan binääri digest ascii-hex muotoon */
    {
        for (int di = 0; di < SHA256_DIGEST_SIZE; ++di)
        {
            sprintf(sha + di * 2, "%02x", digest[di]);
        }
    }
}

void Command::update_sha()
{
    if (mode == '1')
    {

        /* the binary result */
        get_sha256(sha, pwd, rn);
        return;
    }
    memset(sha, 'f', 64);
}

uint16_t TcpMessage::length()
{
    return 2 + 11 + 1 + cmd.length();
}

int TcpMessage::serialize(char* buf)
{

    memcpy(buf + 2, PRO_LABEL, 11);
    memcpy(buf + 2 + 11, &(this->mode), 1);

    int cmdLen = this->cmd.serialize(buf + 2 + 11 + 1);

    len = PRO_TCP_HEADER_LEN + cmdLen;
    uint16_t hl = htons(len);
    memcpy(buf, &hl, 2);

    debug("TCP -- len: <%d>, mode: <%c>", len, mode);

    return this->len;
}

int TcpMessage::deserialize(const char* buf, int len)
{
//    uint16_t nl;
//    memcpy(&nl, buf, 2);
//    len = ntohs(nl);
    memcpy(&mode, buf + 2 + 11, 1);

    this->cmd.deserialize(buf + 2 + 11 + 1);

    debug("TCP -- len: <%d>, mode: <%c>", len, mode);

    return 1;
}

