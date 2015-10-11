extern "C" {
#include <signal.h>
#include <unistd.h>
#include <getopt.h>
#include <stdio.h>
#include <pthread.h>
}

#include <iostream>
#include <cstdlib>
#include <map>

#include "netutil.hpp"

#define MAX_RUNTIME 60*60  /* one hour in seconds */

static std::map<std::string, std::string> users;

static void watchdog(int signro)
{
    exit(signro); /* process will exit when the timer signal arrives */
}

/**
 * --port T where number T defines the port number where the server listens to connections (both TCP and UDP)
 * ONE of these parametes must be specified:
 *    --debug server is running in debug mode (only the DEBUG parts of the protocol are implemented)
 *    --normal server is running in normal operating mode (AUTH1 parts implemented)
 *    --authN server implements extra functionality, where number N is chosen by the project team
 */

static int port = -1;
static int mode = -1;

static int usage(char* name)
{
    std::cout << "Usage: " << name << " --port port --debug|normal|auth1"
              << std::endl;
    exit(1);
}

static void parseArgs(int argc, char** argv)
{

    if (argc != 4)
    {
        usage(argv[0]);
    }

    while (1)
    {
        int c, option_index;
        static struct option long_options[] = { { "port", required_argument, 0,
                0
            }, { "debug", no_argument, 0, 0 }, { "normal",
                required_argument, 0, 0
            }, { "auth1",
                no_argument, 0, 0
            }, { 0, 0, 0, 0 }
        };

        c = getopt_long(argc, argv, "p:dna", long_options, &option_index);
        if (c == -1)
            break;

        if (strcmp(long_options[option_index].name, "port") == 0)
        {
            port = atoi(optarg);
        }
        else if (strcmp(long_options[option_index].name, "debug") == 0)
        {
            if (mode != -1)
                usage(argv[0]);
            mode = PRO_DEBUG;
        }
        else if (strcmp(long_options[option_index].name, "normal") == 0)
        {
            if (mode != -1)
                usage(argv[0]);
            mode = PRO_NORMAL;
        }
        else if (strcmp(long_options[option_index].name, "auth1") == 0)
        {
            if (mode != -1)
                usage(argv[0]);
            mode = PRO_AUTH1;
        }
    }

    debug("port: <%d>, mode: <%c>", port, mode);

}

void* client_handler(void* _sockfd)
{
    char* buf = 0;
    int len;
    uint16_t tcplen;
    TcpMessage msg(mode);
    // setup udp socket

    UdpMessage udpMsg;

    debug("Get connection from client, start authentication.");

    int sockfd = reinterpret_cast<long>(_sockfd);

    Connection conn(sockfd);

    // Get A0 command
    {
        debug("Get A0 command from client");
        tcplen = conn.recv_uint16();

        buf = (char*) malloc(tcplen);

        msg.len = tcplen;
        len = conn.recv(buf + 2, tcplen - 2);

        msg.deserialize(buf, tcplen);
        free(buf);

        // the user is not in the user database, close the TCP connection
        if (users.find(msg.cmd.user) == users.end())
        {
            info(
                "The user <%s> is not found in user database, close the connection with client",
                msg.cmd.user.c_str());
            // the de-constructor of conn will close the socket
            return 0;
        }

    }

    {
        // A1 command: A1 32bit-random-number
        debug("Send A1 command to client");

        msg.cmd.step = 1;
        buf = (char*) malloc(msg.length());

        len = msg.serialize(buf);
        conn.send(buf, len);

        free(buf);
        // A1 command -- end
    }

    {
        debug("Get A2 command from client");
        tcplen = conn.recv_uint16();
        msg.len = tcplen;

        buf = (char*) malloc(tcplen);
        len = conn.recv(buf + 2, tcplen - 2);

        msg.deserialize(buf, len);
        free(buf);
    }

    UdpSocket udpSkt;

    {
        debug("Send A3 command to client");

        msg.cmd.step = 3;
        msg.cmd.status = 0;

        buf = (char*) malloc(msg.length());

        if (mode == '1')
        {
            std::string passwd = users[msg.cmd.user];

            char sha[128];

            get_sha256(sha, passwd, msg.cmd.rn);
            if (strncmp(sha, msg.cmd.sha, 64) == 0)
            {
                msg.cmd.status = 0;
            }
            else
            {
                msg.cmd.status = 1;
            }

        }

        // if auth successfully, setup UDP for query
        if (msg.cmd.status == 0)
        {
            msg.cmd.sid = udpSkt.get_port(); // should be udp port
            info("User Authentication done for <%s>.", msg.cmd.user.c_str());
        }

        len = msg.serialize(buf);
        conn.send(buf, len);
        free(buf);
    }

    {
        debug("Get UDP query from client");
        char udpbuf[PRO_UDP_MAX_LEN] = { 0 };
        len = udpSkt.recv(udpbuf, PRO_UDP_MAX_LEN);
        udpMsg.deserialize(udpbuf, len);
    }

    {
        debug("Send UDP response to client");
        udpMsg.dir = 'C';
        udpMsg.timestamp = 1;
        udpMsg.status = 1;

        char udpbuf[PRO_UDP_MAX_LEN] = { 0 };
        len = udpMsg.serialize(udpbuf);
        errno = 0;
        udpSkt.send(udpbuf, len);
    }

    printf("\n");
}

int main(int argc, char** argv)
{
    if (signal(SIGALRM, watchdog) == SIG_ERR)
    {
        exit(2); /* something went wrong in setting signal */
    }

    alarm( MAX_RUNTIME); /* after this time the program will always exit */

    /* This location would contain the rest of the program.
     * In this example we'll just pause the program (to wait for the alarm signal)
     */

    parseArgs(argc, argv);

    users["Jack"] = "12345";
    users["Alicia"] = "4567";
    users["Rose"] = "abcde";

    ServerSocket srvSkt(port);

    int sockfd;

    while ((sockfd = srvSkt.accept()) > 0)
    {
        pthread_t id;
        int ret = pthread_create(&id, NULL, client_handler,
                                 reinterpret_cast<void*>(sockfd));
        if (ret != 0)
        {
            printf("Create pthread error!n");
            exit(1);
        }

        pthread_detach(id);
        // client_handler(reinterpret_cast<void*>(sockfd));
    }
    return 0;
}

