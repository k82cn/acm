#ifndef __OGL_LOG_H__
#define __OGL_LOG_H__

#include <string>
#include <cstdio>
#include <cassert>

#include <memory>

#include "utils.h"
#include "types.h"

namespace ogl
{
    class Property;

    class Logger
    {
        private:
            const std::string m_name;
            FILE* m_logFile;
            LOG_LEVEL m_logLevel;

            Logger(const std::string& loggerName);

            static lock_t m_logLock;

            static ogl::Property* m_props;

            static LOG_LEVEL toLogLevel(std::string str);

        public:
            static Logger* getLogger(const std::string& loggerName);

            bool isLevel(LOG_LEVEL l);

            void Debug(const char* msg);
            void Info(const char* msg);
            void Warn(const char* msg);
            void Error(const char* msg);
            void Assert(bool exp, const char* msg);
            void Assert(bool exp);
            void Backtrace(void);
    };

    typedef std::shared_ptr<ogl::Logger> LoggerPtr;

    extern LoggerPtr logger;

    class AutoTimer
    {
        public:
            AutoTimer(const char* );
            ~AutoTimer();
        private:
            std::string m_label;
            void* m_start;
            void* m_end;
    };

}

#define __OGL_LOG_MSG(fmt, ...)                      \
    int __n = sprintf(__buf, fmt, ##__VA_ARGS__); \
    __buf[__n] = 0;                                  \
    assert(__buf[BUFSIZ - 1] == 0);                  \
 
#define OGL_LOG_ASSERT(exp, fmt, ...) do {            \
        char __buf[BUFSIZ] = {0};                \
        __OGL_LOG_MSG(fmt, ##__VA_ARGS__);        \
        m_logger->Assert(exp, __buf);            \
    } while(0)

#define OGL_LOG_DEBUG(fmt, ...) do {            \
        if (m_logger->isLevel(ogl::DEBUG)) {char __buf[BUFSIZ] = {0};                \
        __OGL_LOG_MSG(fmt, ##__VA_ARGS__);        \
        m_logger->Debug(__buf);  }          \
    } while(0)

#define OGL_LOG_WARN(fmt, ...) do {                \
        if (m_logger->isLevel(ogl::WARN)) {char __buf[BUFSIZ] = {0};                \
        __OGL_LOG_MSG(fmt, ##__VA_ARGS__);        \
        m_logger->Warn(__buf); }            \
    } while(0)

#define OGL_LOG_INFO(fmt, ...) do {                \
        if (m_logger->isLevel(ogl::INFO)) {char __buf[BUFSIZ] = {0};                \
        __OGL_LOG_MSG(fmt, ##__VA_ARGS__);        \
        m_logger->Info(__buf); }            \
    } while(0)

#define OGL_LOG_ERROR(fmt, ...) do {            \
        if (m_logger->isLevel(ogl::ERROR)) {char __buf[BUFSIZ] = {0};                \
        __OGL_LOG_MSG(fmt, ##__VA_ARGS__);        \
        m_logger->Error(__buf); }           \
    } while(0)

#endif
