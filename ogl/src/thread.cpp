#include "thread.h"

extern "C"
{
#include <pthread.h>
}

namespace ogl
{
    queue_t::queue_t()
    {
        m_queue = list_create();
    }

    void* queue_t::getq()
    {
        void* res = NULL;

        m_event.acquire();

        while (list_is_empty(m_queue))
        {
            m_event.wait();
        }

        res =  list_pop(m_queue);

        m_event.release();

        return res;
    }

    int queue_t::putq(void* data)
    {
        m_event.acquire();
        list_append(m_queue, data);

        m_event.notifyAll();
        m_event.release();
        return 1;
    }

    bool queue_t::empty()
    {
        m_event.acquire();
        bool r = list_is_empty(m_queue);
        m_event.release();
        return r;
    }

    Thread::Thread(worker_t worker, void* data)
    {
        m_worker = worker;
        m_data = data;
    }

    Thread::~Thread()
    {
        // do nothing
    }

    int Thread::start()
    {
        return pthread_create(&m_tid, 0, m_worker, m_data);
    }

    int Thread::stop()
    {
        return 0;
    }

    int Thread::wait()
    {
        return pthread_join(m_tid, 0);
    }

    int Thread::yield()
    {

        return -1;
        // return pthread_yield();
        //
    }

};
