#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <arpa/inet.h>
#include <sys/types.h>
#include <netinet/in.h>
#include <sys/socket.h>
#include <iostream>
#include <fstream>
#include <cstring>
#include <sstream>
// #include <mach/mach.h>
#include <stdint.h>

using std::endl;
using std::cout;
using std::ifstream;
using std::string;
using std::getline;
using std::cin;
using std::istringstream;
using std::strcpy;
using std::hex;

#define MAXRCVLEN 500
#define PORTNUM 2300

const char* getOffsetToken(char* chr){
    int n = 0;
    const char* token[4] = {};
    
    token[0] = strtok(chr, " ");
    
    if (token[0])     {
        for (n = 1; n < 4; n++)
        {
            token[n] = strtok(0, " ");
            if (!token[n]) break;
        }
    }
    return token[1];
}

int main(int argc, char *argv[])
{
//    mach_port_t task;
//    kern_return_t err;
//     pointer_t buf;
    uint32_t sz,process,coins;
//     vm_address_t vmpatch1;
    long vmpatch1;
    
    char buffer[MAXRCVLEN + 1]; /* +1 so we can add null terminator */
    int len, mysocket;
    struct sockaddr_in dest;
    
    mysocket = socket(AF_INET, SOCK_STREAM, 0);
    
    memset(&dest, 0, sizeof(dest));                /* zero the struct */
    dest.sin_family = AF_INET;
    dest.sin_addr.s_addr = inet_addr("127.0.0.1"); /* set destination IP number - localhost, 127.0.0.1*/
    //dest.sin_addr.s_addr = htonl(INADDR_LOOPBACK); /* set destination IP number - localhost, 127.0.0.1*/
    dest.sin_port = htons(PORTNUM);                /* set destination port number */
    
    connect(mysocket, (struct sockaddr *)&dest, sizeof(struct sockaddr));
    
    len = recv(mysocket, buffer, MAXRCVLEN, 0);
    
    /* We have to null terminate the received data ourselves */
    buffer[len] = '\0';
    char* bt = strdup(buffer);
    
    cout<<buffer<<endl;
    cout << "1" << endl;
    cout <<getOffsetToken(buffer)<<endl;
    cout << "2" << endl;
    vmpatch1=atol(getOffsetToken(bt));
    cout << "3" << endl;
    cout << vmpatch1<<endl;
    free(bt);
    
    close(mysocket);
    return EXIT_SUCCESS;
}
