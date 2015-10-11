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

// usage: "./argv0 port"
int main(int argc, char** argv)
{
  // --- command line args (port number)
  std::vector<std::string> args(argv,argv+argc);
  if( argc != 2 ) { panic("need port argument",3); }
  int listen_port;
  {
    std::istringstream i(args[1]);
    i >> listen_port;
    if( i.fail() ) { panic("port argument is not a number?", 5); }
  }

  // -- initialize a TCP/IP socket
  int sockfd = socket(AF_INET, SOCK_STREAM, 0);
  if( sockfd < 0 ) {
    perror("socket()"); panic("socket()",1);
  }

  // -- listening socket struct 
  sockaddr_in server_addr;
  memset(reinterpret_cast<char *>(&server_addr), 0, sizeof(server_addr));
  server_addr.sin_family      = AF_INET;
  server_addr.sin_addr.s_addr = htonl(INADDR_ANY);
  server_addr.sin_port        = htons(listen_port);
  if( bind(sockfd, reinterpret_cast<struct sockaddr *>(&server_addr),
       sizeof(server_addr)) < 0 ) {
    perror("bind()"); panic("bind()", 2);
  }

  // -- start accepting incoming connections
  listen(sockfd,5);
  std::clog << args[0] << " listening on port: " << listen_port << std::endl;

  // -- accept one incoming connection
  sockaddr_in client_addr;
  socklen_t client_len = sizeof(client_addr);

  int newsockfd = accept(sockfd, reinterpret_cast<sockaddr*>(&client_addr),
             &client_len);
  if( newsockfd < 0 ) { perror("accept()"); panic("accept()",7); }


  // here communication would be possible using 'newsockfd'


  close(newsockfd);  // close client connection

  close(sockfd);  // close the listening socket
}
