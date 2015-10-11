#include "log.h"
#include "tcp.h"
#include "types.h"

#include "msg.h"

#include <unistd.h>
#include <sys/socket.h>
#include <netinet/in.h>

#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>

#include <netdb.h>


ogl::Logger* ogl::TcpServer::m_logger = ogl::Logger::getLogger("ogl.TcpServer");
ogl::Logger* ogl::TcpConnection::m_logger = ogl::Logger::getLogger("ogl.TcpConnection");

ogl::TcpServer::TcpServer(int& port)
{
    int rc = -1;
    m_socket = socket(AF_INET, SOCK_STREAM, 0);
    m_logger->Assert(m_socket > 0);
    struct sockaddr_in serv_addr;

    memset(&serv_addr, 0, sizeof(serv_addr));

    serv_addr.sin_family = AF_INET;
    serv_addr.sin_addr.s_addr = INADDR_ANY;
    if (port == 0)
    {
        serv_addr.sin_port = 0;
    }
    else
    {
        serv_addr.sin_port = htons(port);
    }

    rc = bind(m_socket, (struct sockaddr*) & serv_addr, sizeof(serv_addr));
    m_logger->Assert(rc >= 0);

    if (0 == port)
    {
        socklen_t n = sizeof(serv_addr);
        memset(&serv_addr, 0, n);
        rc = getsockname(m_socket, (struct sockaddr*) & serv_addr, &n);
        m_logger->Assert(rc == 0);
        port = ntohs(serv_addr.sin_port);
    }

    rc = listen(m_socket, 10);
    m_logger->Assert(rc >= 0);
}

int ogl::TcpServer::close()
{
    return ::close(m_socket);
}

ogl::TcpConnection ogl::TcpServer::accept()
{
    socket_t skt;
    skt = ::accept(m_socket, 0, 0);
    m_logger->Assert(skt > 0);
    return skt;
}

ogl::TcpConnection::TcpConnection(const std::string& host, int port)
{
    m_socket = socket(AF_INET, SOCK_STREAM, 0);
    m_logger->Assert(m_socket > 0);

    struct hostent *server = ::gethostbyname(host.c_str());
    m_logger->Assert(server != 0);

    struct sockaddr_in serv_addr;
    bzero((char *) &serv_addr, sizeof(serv_addr));
    serv_addr.sin_family = AF_INET;
    bcopy((char *)server->h_addr,
          (char *)&serv_addr.sin_addr.s_addr,
          server->h_length);

    serv_addr.sin_port = htons(port);
    int rc = connect(m_socket, (struct sockaddr *) & serv_addr, sizeof(serv_addr));
    m_logger->Assert(rc >= 0);
}

ogl::TcpConnection::TcpConnection(socket_t skt)
{
    m_logger->Assert(skt > 0);
    m_socket = skt;
}

size_t ogl::TcpConnection::send(const ogl::Buffer& buf)
{
    int n = 0;
    n = ::send(m_socket, buf.data(), buf.size(), 0);
    m_logger->Assert(n >= 0 && (size_t)n == buf.size());
    return buf.size();
}

size_t ogl::TcpConnection::send(const char* buf, int size)
{
    int n = 0;
    n = ::send(m_socket, buf, size, 0);
    m_logger->Assert(n >= 0 && n == size);
    return size;
}

size_t ogl::TcpConnection::recv(char* buf, int& size)
{
    int n = 0;
    n = ::recv(m_socket, buf, size, 0);
    m_logger->Assert(n >= 0);
    size = n;
    return size;
}

size_t ogl::TcpConnection::recv(Buffer& buf)
{
    int n = 0;
    n = ::recv(m_socket, buf.data(), buf.capacity(), 0);
    m_logger->Assert(n >= 0);
    buf.size(n);
    return buf.size();
}

int ogl::TcpConnection::close()
{
    return ::close(m_socket);
}

ogl::handle_t ogl::TcpConnection::get_handler()
{
    return m_socket;
}

