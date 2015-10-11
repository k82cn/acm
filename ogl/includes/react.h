#ifndef __OGL_REACT_H__
#define __OGL_REACT_H__

#include "types.h"

namespace ogl
{
    class Handler
    {
        public:
            virtual int handle_input() = 0;
            virtual int handle_output() = 0;
            virtual int handle_close() = 0;

            void set_handler(handle_t t)
            {
                m_handler = t;
            };

            handle_t get_handler()
            {
                return m_handler;
            };

        private:
            handle_t m_handler;
    };

    class Reactor
    {
        public:

            static const int read = 1;
            static const int write = 2;

            int register_handler(Handler* h, int mode );
            int remove_handler(Handler* h);

            void run_event_loop();
            int is_loop_done();
            int stop_event_loop();

        private:
            handle_t m_eventSet;
            volatile bool m_stop;
    };
}

#endif
