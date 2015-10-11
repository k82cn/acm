#ifndef __OGL_THREAD_H__
#define __OGL_THREAD_H__


#include <list>

#include "types.h"
#include "utils.h"
#include "mem.h"

namespace ogl
{
    class queue_t
    {
        public:
            queue_t();
            void* getq();
            int putq(void* data);
            bool empty();

        private:
            node_t* m_queue;
            event_t m_event;
    };

    class Thread
    {
        public:

            typedef void* (*worker_t)(void* data);

            Thread(worker_t worker, void* data = 0);

            virtual ~Thread();

            virtual int start();

            virtual int stop();

            virtual int wait();

            virtual int yield();

        private:
            thread_t m_tid;
            worker_t m_worker;
            void* m_data;
    };
}

#endif

