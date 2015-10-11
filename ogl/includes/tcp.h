#ifndef _OGL_TCP_H_
#define _OGL_TCP_H_

#include "buffer.h"
#include "types.h"
#include "log.h"

#include <map>
#include <string>

namespace ogl
{


    class SocketOptions
    {
            std::map<long, long> m_options;

        public:
            long option(long key, long value)
            {

                long oldValue = m_options[key];
                m_options[key] = value;
                return oldValue;
            };

            long option(long key)
            {
                return m_options[key];
            };

            std::map<long, long>::iterator begin()
            {
                return m_options.begin();
            };

            std::map<long, long>::iterator end()
            {
                return m_options.end();
            };
    };

    /**
     * A simple TcpConnection
     *
     */
    class TcpConnection
    {
        private:
            socket_t m_socket;
            static Logger* m_logger;
        public:
            TcpConnection(const std::string& host, int port);
            TcpConnection(const std::string& host, int port, const SocketOptions& options) {};
            TcpConnection(ogl::socket_t skt);
            size_t send(const Buffer& buf);
            size_t send(const char* buf, int size);
            size_t recv(Buffer& buf);
            size_t recv(char* buf, int& size);

            handle_t get_handler();

            int close(void);
    };

    /**
     * A simple TcpServer
     *
     */
    class TcpServer
    {
        private:
            socket_t m_socket;
            static Logger* m_logger;
        public:
            TcpServer(int& port);
            TcpServer(int& port, const SocketOptions& options) {};
            TcpConnection accept();
            int close();
    };
}

#endif
