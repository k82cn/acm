#include "ogl.h"

#include <cstring>
#include <cstdio>
#include <cstdlib>

extern "C"
{
#include <stdarg.h>
#include <execinfo.h>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <errno.h>
#include <sys/time.h>
#include <stdio.h>
#include <stdlib.h>

}

namespace ogl
{

    LoggerPtr logger(Logger::getLogger("ogl.global"));

    lock_t Logger::m_logLock;

    ogl::Property* Logger::m_props = NULL;

    Logger* Logger::getLogger(const std::string& loggerName)
    {
        {
            autolock_t lock(m_logLock);
            if (m_props == NULL)
            {
                m_props = new ogl::Property(CONF_OGL_CONF_FILE, CONF_OGL_HOME);
            }
        }

        return new Logger(loggerName);
    }

    LOG_LEVEL Logger::toLogLevel(std::string strLevel)
    {

        ogl::cstr_to_upper(strLevel);

        if (strLevel == "DEBUG")
        {
            return DEBUG;
        }

        if (strLevel == "INFO")
        {
            return INFO;
        }

        if (strLevel == "ERROR")
        {
            return ERROR;
        }

        if (strLevel == "WARN")
        {
            return WARN;
        }

        return INFO;
    }

    Logger::Logger(const std::string& loggerName) : m_name(loggerName)
    {
        m_logFile = ::fopen(m_props->getCString(CONF_OGL_LOG_FILE), "a+");

        assert(m_logFile);

        std::string logConf(CONF_OGL_LOG_PRE);
        logConf.append(loggerName);

        const char* logLevelStr = m_props->getCString(logConf);

        if (0 == logLevelStr)
        {
            logLevelStr = m_props->getCString(CONF_OGL_LOG_LEVEL);
        }

        if (0 == logLevelStr)
        {
            m_logLevel = INFO;
        }
        else
        {
            m_logLevel = toLogLevel(logLevelStr);
        }

    };

    void Logger::Debug(const char* msg)
    {
        if (m_logLevel <= DEBUG)
        {
            ogl::autolock_t lock(m_logLock);
            ::fprintf(m_logFile, "%s [DEBUG]: %s\n", m_name.c_str(), msg);
            ::fflush(m_logFile);
        }
    }

    void Logger::Warn(const char* msg)
    {
        if (m_logLevel <= WARN)
        {
            ogl::autolock_t lock(m_logLock);
            ::fprintf(m_logFile, "%s [WARN]: %s\n", m_name.c_str(), msg);
            ::fflush(m_logFile);
        }
    }
    void Logger::Info(const char* msg)
    {
        if (m_logLevel <= INFO)
        {
            ogl::autolock_t lock(m_logLock);
            ::fprintf(m_logFile, "%s [INFO]: %s\n", m_name.c_str(), msg);
            ::fflush(m_logFile);
        }
    }
    void Logger::Error(const char* msg)
    {
        if (m_logLevel <= ERROR)
        {
            ogl::autolock_t lock(m_logLock);
            ::fprintf(m_logFile, "%s [ERROR]: %s\n", m_name.c_str(), msg);
            ::fflush(m_logFile);
        }
    }

    void Logger::Assert(bool exp, const char* msg)
    {
        if (!exp)
        {
            ogl::autolock_t lock(m_logLock);
            ::fprintf(m_logFile, "%s [ASSERT]: %s\n", m_name.c_str(), msg);
            ::fflush(m_logFile);

            int nptrs;
            void *buffer[OGL_STACK_MAX_SIZE];

            nptrs = backtrace(buffer, OGL_STACK_MAX_SIZE);

            backtrace_symbols_fd(buffer, nptrs, fileno(m_logFile));
        }
    }

    void Logger::Backtrace(void)
    {
        ogl::autolock_t lock(m_logLock);
        ::fprintf(m_logFile, "%s [Backtrace]:\n", m_name.c_str());
        ::fflush(m_logFile);

        int nptrs;
        void *buffer[OGL_STACK_MAX_SIZE];

        nptrs = ::backtrace(buffer, OGL_STACK_MAX_SIZE);
        ::backtrace_symbols_fd(buffer, nptrs, fileno(m_logFile));
    }

    void Logger::Assert(bool exp)
    {
        if (!exp)
        {
            ogl::autolock_t lock(m_logLock);
            ::fprintf(m_logFile, "%s [ASSERT]: errno(%d): %s\n", m_name.c_str(), errno, strerror(errno));
            ::fflush(m_logFile);

            int nptrs;
            void *buffer[OGL_STACK_MAX_SIZE];

            nptrs = backtrace(buffer, OGL_STACK_MAX_SIZE);

            backtrace_symbols_fd(buffer, nptrs, fileno(m_logFile));
        }
    }

    bool Logger::isLevel(LOG_LEVEL l)
    {
        return m_logLevel <= l;
    }

    AutoTimer::AutoTimer(const char* label) : m_label(label)
    {
        m_start = new timeval();
        gettimeofday((timeval*)m_start, NULL);
    }

    AutoTimer::~AutoTimer()
    {
        m_end = new timeval();
        gettimeofday((timeval*)m_end, NULL);

        long seconds, useconds;
        double mtime;

        seconds = ((timeval*)m_end)->tv_sec - ((timeval*)m_start)->tv_sec;
        useconds = ((timeval*)m_end)->tv_usec - ((timeval*)m_start)->tv_usec;

        mtime = ((seconds) * 1000 + useconds / 1000.0) + 0.5;

        char buffer[BUFSIZ] = {0};

        sprintf(buffer, "%s (%lf).", m_label.c_str(), mtime);

        logger->Info(buffer);

        delete (timeval*) m_start;
        delete (timeval*) m_end;
    }


}
