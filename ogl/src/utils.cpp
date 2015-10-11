#include "ogl.h"

#include <string>
#include <cctype>
#include <cstdlib>

namespace ogl
{
    std::string gethostname()
    {
        Buffer buf;

        ::gethostname(buf.data(), buf.capacity());

        buf.size(strlen(buf.data()) + 1);

        return buf.data();
    }

    void cstr_release(char*& str)
    {
        if (str)
        {
            ::free(str);
            str = 0;
        }
    }

    int cstr_to_upper(std::string& str)
    {
        size_t i;

        for (i = 0; i < str.length(); i++)
        {
            str[i] = ::toupper(str[i]);
        }

        return i;
    }


    char* cstr_append_int(const char* str, size_t len, int num)
    {
        char* res = (char*)::malloc(len + 16);
        int n = snprintf(res, len + 16, "%s/%d", str, num);
        if (n < 0)
        {
            return 0;
        }
        res[n] = 0;

        return res;
    }

    void trim(std::string& str)
    {
        std::string whitespaces (" \t\f\v\n\r");

        std::size_t found = str.find_last_not_of(whitespaces);
        if (found != std::string::npos)
            str.erase(found + 1);
        else
        {
            str.clear();
            return;
        }

        found = str.find_first_not_of(whitespaces);
        if (found != std::string::npos)
        {
            str.erase(0, found);
        }
    }

    event_t::event_t()
    {
        pthread_mutexattr_t attr;
        pthread_mutexattr_init( &attr );
        pthread_mutexattr_settype( &attr, PTHREAD_MUTEX_RECURSIVE );

        pthread_mutex_init( &m_mutex, &attr );

        pthread_mutexattr_destroy( &attr );
        pthread_mutex_init(&m_mutex, NULL);

        pthread_cond_init(&m_cond, NULL);
    }

    event_t::event_t(const ogl::event_t& event)
    {
        m_mutex = event.m_mutex;
        m_cond = event.m_cond;
    }

    event_t::~event_t()
    {
        pthread_mutex_destroy(&m_mutex);
        pthread_cond_destroy(&m_cond);
    }

    void event_t::acquire()
    {
        pthread_mutex_lock(&m_mutex);
    }

    void event_t::wait()
    {
        pthread_cond_wait(&m_cond, &m_mutex);
    }

    void event_t::notify()
    {
        pthread_cond_signal(&m_cond);
    }

    void event_t::notifyAll()
    {
        pthread_cond_broadcast(&m_cond);
    }

    void event_t::release()
    {
        pthread_mutex_unlock(&m_mutex);
    }

    lock_t::lock_t()
    {
        pthread_mutexattr_t attr;
        pthread_mutexattr_init( &attr );
        pthread_mutexattr_settype( &attr, PTHREAD_MUTEX_RECURSIVE );

        pthread_mutex_init( &m_mutex, &attr );

        pthread_mutexattr_destroy( &attr );
        pthread_mutex_init(&m_mutex, NULL);
    }

    lock_t::lock_t(const ogl::lock_t& mutex)
    {
        m_mutex = mutex.m_mutex;
    }

    lock_t::~lock_t()
    {
        pthread_mutex_destroy(&m_mutex);
    }

    void lock_t::acquire()
    {
        pthread_mutex_lock(&m_mutex);
    }

    void lock_t::release()
    {
        pthread_mutex_unlock(&m_mutex);
    }
}


