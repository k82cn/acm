#include "ogl.h"

extern "C"
{
#include <sys/epoll.h>
}

namespace ogl
{
    Reactor::Reactor()
    {
        m_eventSet = ::epoll_create(1);
        m_stop = false;
    }

    int Reactor::register_handler(Handler* h, int mode )
    {
        struct epoll_event* event = (struct epoll_event*) malloc(sizeof(struct epoll_event));

        event->data.ptr = handler;
        event->events = EPOLLIN | EPOLLET;

        return epoll_ctl (m_eventSet, EPOLL_CTL_ADD, h->get_handler(), event);
    }

    int Reactor::remove_handler(Handler* h)
    {
        int hr = epoll_ctl (m_eventSet, EPOLL_CTL_DEL, h->get_handler(), NULL);

        if (0 == hr)
        {
            free(h);
        }

        return hr;
    }

    void Reactor::run_event_loop()
    {
        int n, i;
        struct epoll_event *events;
        events = calloc (MAXEVENTS, sizeof event);

        n = epoll_wait (m_eventSet, events, MAXEVENTS, -1);
        for (i = 0; i < n; i++)
        {
            // handle input
            if (event[i].events & EPOLLIN)
            {
                event[i].ptr->handle_input();
                continue;
            }

            // handle output
            if (event[i].events & EPOLLOUT)
            {
                event[i].ptr->handle_output();
                continue;
            }
        }
    }

    int Reactor::is_loop_done()
    {
        return m_stop;
    }

    int Reactor::stop_event_loop()
    {
        m_stop = true;
    }

}
