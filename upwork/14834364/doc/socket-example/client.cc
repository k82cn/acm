/* Client code template using socket API in C++
 * Jyke Savia 2008-2015
 * License http://creativecommons.org/licenses/by-nc/2.0/
 */

// include C library headers as externC:
extern "C" {
#include <sys/types.h>
#include <sys/socket.h> /* socket api */
#include <netinet/in.h> /* inetaddr structs */
#include <netdb.h> /* gethostbyname() */

#include <string.h> /* memset(), memcopy() */
#include <stdio.h> /* perror() */
#include <unistd.h> /* close() */
}

#include <cstdlib> // exit()
#include <vector>
#include <string>
#include <iostream>
#include <sstream>

void panic(std::string message, int exitval)
{
  std::cerr << "error: " << message << std::endl;
  exit(exitval);
}

// usage: "./argv0 host port"
int main(int argc, char** argv)
{
  // --- parse commandline arguments
  std::vector<std::string> args(argv,argv+argc);
  if( argc != 3 ) { panic("need host and port arguments",3); }
  const char* connect_host = args[1].c_str();
  int connect_port;
  {
    std::istringstream i(args[2]);
    i >> connect_port;
    if( i.fail() ) { panic("port argument is not a number?", 5); }
  }

  // -- create a TCP/IP socket
  int sockfd = socket(AF_INET, SOCK_STREAM, 0);
  if( sockfd < 0 ) {
    perror("socket()"); panic("socket()",1);
  }

  // -- init destination address struct
  sockaddr_in connect_addr;
  memset(reinterpret_cast<char *>(&connect_addr), 0, sizeof(connect_addr));
  connect_addr.sin_family = AF_INET;
  connect_addr.sin_port   = htons(connect_port);

  // string hostname -> ip-address
  {
    struct hostent *hp;
    if ((hp = gethostbyname(connect_host)) == NULL) {
      perror("gethostbyname()"); panic("gethostbyname()",9);
    }
    bcopy(reinterpret_cast<char *>(&connect_addr.sin_addr), 
      reinterpret_cast<char *>(hp->h_addr), 
      hp->h_length);
  }

  // make the connection to the server
  if( connect(sockfd, reinterpret_cast<struct sockaddr *>(&connect_addr),
          sizeof(connect_addr) ) < 0 ) {
    perror("connect()"); panic("connect()",8);
  }

  // here communication would be possible using sockfd

  close(sockfd);  // close connection
}
