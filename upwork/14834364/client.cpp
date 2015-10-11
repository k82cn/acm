#include <sys/types.h>
#include <sys/socket.h> /* socket api */
#include <netinet/in.h> /* inetaddr structs */
#include <netdb.h> /* gethostbyname() */

#include <string.h> /* memset(), memcopy() */
#include <stdio.h> /* perror() */
#include <unistd.h> /* close() */
#include <signal.h>
#include <stdlib.h> // exit()

#include <getopt.h>

#include <vector>
#include <string>
#include <iostream>
#include <sstream>
#include <string>

#include "netutil.hpp"

#define MAX_RUNTIME 60*60  /* one hour in seconds */

/**
 * --server N where 'N' is the server to connect to, either in "dotted decimal" format ("130.230.4.2") or in nameserver resolvable text string (e.g. "mustavaris.cs.tut.fi")
 * --port T port number of the service in the server, where the connection attempt is made.
 * --user U where 'U' is the username string (1-64 characters)
 * --pwd P where 'P' is the password for the user (1-20 characters)
 * --query URL where 'URL' is the request string for the server
 * ONE of these parameters must be specified:
 *    --debug make the connection in debug mode (only the DEBUG parts of the protocol are implemented)
 *    --normal make the connection in normal operating mode (AUTH1 parts implemented)
 *    --authN connection with extra functionality, where number N is chosen by the project team
 */

static std::string host;
static int port = -1;
static std::string user;
static std::string passwd;
static std::string url;
static int mode = -1;

static int usage(char* name)
{
    std::cout << "Usage: " << name
              << " --server N --port T --user U --pwd P --query URL --debug|normal|auth1"
              << std::endl;
    exit(1);
}

static void parseArgs(int argc, char** argv)
{

    if (argc != 12)
    {
        usage(argv[0]);
    }

    while (1)
    {
        int c, option_index;
        static struct option long_options[] = { { "port", required_argument, 0,
                0
            }, { "server", required_argument, 0, 0 }, { "user",
                required_argument, 0, 0
            }, { "pwd", required_argument, 0, 0 }, {
                "query", required_argument, 0, 0
            },
            { "debug", no_argument, 0, 0 }, {
                "normal",
                required_argument, 0, 0
            }, { "auth1",
                no_argument, 0, 0
            }, { 0, 0, 0, 0 }
        };

        c = getopt_long(argc, argv, "p:s:u:w:q:dna", long_options,
                        &option_index);
        if (c == -1)
            break;

        if (strcmp(long_options[option_index].name, "server") == 0)
        {
            host = optarg;
        }
        else if (strcmp(long_options[option_index].name, "query") == 0)
        {
            url = optarg;
        }
        else if (strcmp(long_options[option_index].name, "pwd") == 0)
        {
            passwd = optarg;
        }
        else if (strcmp(long_options[option_index].name, "user") == 0)
        {
            user = optarg;
        }
        else if (strcmp(long_options[option_index].name, "port") == 0)
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

    debug(
        "server: <%s>, user: <%s>, pwd: <%s>, url: <%s>, port: <%d>, mode: <%c>",
        host.c_str(), user.c_str(), passwd.c_str(), url.c_str(), port,
        mode);
}

static void watchdog(int signro)
{
    exit(signro); /* process will exit when the timer signal arrives */
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

    char* buf = 0;
    int len;

    parseArgs(argc, argv);

    Connection conn(host.c_str(), port);

    //
    TcpMessage msg(mode);

    // A0 command: A0 user_name_len user_name
    {
        debug("Send A0 command to server");
        msg.cmd.step = 0;
        msg.cmd.user = user;

        len = msg.length();
        buf = (char*) malloc(len);
        len = msg.serialize(buf);

        conn.send(buf, len);
        free(buf);
        // A0 command -- end
    }

    {
        debug("Get A1 command from server");
        int tcplen = conn.recv_uint16();
        buf = (char*) malloc(tcplen);
        len = conn.recv(buf + 2, tcplen - 2);

        if (len < 0)
        {
            info ("Failed to get feedback from server");
            exit(0);
        }
        msg.deserialize(buf, len);
    }

    {
        debug("Send A2 command to server");
        msg.cmd.step = 2;
        msg.cmd.pwd = passwd;

        len = msg.length();
        buf = (char*) malloc(len);

        len = msg.serialize(buf);

        conn.send(buf, len);
        free(buf);
    }

    {
        debug("Get A3 command from server");
        int tcplen = conn.recv_uint16();
        buf = (char*) malloc(tcplen);

        len = conn.recv(buf + 2, tcplen - 2);
        msg.deserialize(buf, len);
        free(buf);
    }

    if (0 == msg.cmd.status)
    {
        UdpSocket udpSkt;
        UdpMessage udpMsg;

        udpSkt.connect(host.c_str(), msg.cmd.sid);

        udpMsg.ver = mode;
        udpMsg.dir = 'S';
        udpMsg.sid = msg.cmd.sid;
        udpMsg.tid = 0;
        udpMsg.url = url;
        udpMsg.mac_sha.assign(msg.cmd.sha, msg.cmd.sha + 64);

        {
            debug("Send UDP query to server");
            char udpbuf[PRO_UDP_MAX_LEN] = {0};
            len = udpMsg.serialize(udpbuf);
            udpSkt.send(udpbuf, len);
        }

        {
            debug("Get UDP response from server");
            char udpbuf[PRO_UDP_MAX_LEN] = {0};
            len = udpSkt.recv(udpbuf, PRO_UDP_MAX_LEN);
            udpMsg.deserialize(buf, len);
        }

    }
    else
    {
        std::cout << "Failed to auth with server." << std::endl;
    }

    return 0;
}

