#ifndef __OGL_UTILS_H__
#define __OGL_UTILS_H__

#include <string>
#include "types.h"

namespace ogl
{
    void trim(std::string& str);

    std::string gethostname();

    char* cstr_append_int(const char* str, size_t len, int num);
    void cstr_release(char*& str);
    int cstr_to_upper(std::string& str);

    class event_t
    {
        public:
            event_t();

            event_t(const ogl::event_t& event);

            ~event_t();

            void acquire();

            void wait();

            void notify();

            void notifyAll();

            void release();

        private:
            mutex_t  m_mutex;
            cond_t m_cond;
    };


    class lock_t
    {
        public:
            lock_t();

            lock_t(const ogl::lock_t& mutex);

            ~lock_t();

            void acquire();

            void release();

        private:
            mutex_t  m_mutex;
    };

    class autolock_t
    {
        public:
            autolock_t(ogl::lock_t& cs) : m_cs(cs)
            {
                m_cs.acquire();
            }

            ~autolock_t()
            {
                m_cs.release();
            }

        private:
            ogl::lock_t& m_cs;
    };

}

#endif
