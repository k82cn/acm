#ifndef _OGL_BUFFER_H_
#define _OGL_BUFFER_H_

#include <cstring>
#include <cstdio>

#include <memory>

#include "msg.h"
#include "log.h"

namespace ogl
{
    class Data
    {
        private:
            char* m_data;
            size_t m_size;
            size_t m_capacity;
            long m_refcnt;

            static LoggerPtr m_logger;

            Data(const Data& m_d);
            Data& operator=(const Data& m_d);

        public:

            void _inc_ref_cnt();

            void _dec_ref_cnt();

            char* data() const
            {
                return m_data;
            }

            size_t size() const
            {
                return m_size;
            }

            size_t capacity() const
            {
                return m_capacity;
            }

            void size(size_t s);

            size_t idle() const
            {
                return m_capacity - m_size;
            }

            Data(const char* msg, size_t len);

            Data(int size = BUFSIZ);

            ~Data();
    };

    typedef std::shared_ptr<ogl::Data> DataPtr;

    class Buffer
    {
        private:
            Data* m_data;
            Buffer* m_next;

            long m_refcnt;

            static LoggerPtr m_logger;

        protected:
            void _inc_ref_cnt();

            void _dec_ref_cnt();

        public:

            Buffer( const char* msg, int len);

            Buffer(int size = BUFSIZ);

            Buffer(const Buffer& buf);

            Buffer& operator=(const Buffer& buf);

            Buffer* next() const
            {
                return m_next;
            }

            void append(Buffer* next);

            void append(const char* msg, size_t len);

            char* data() const
            {
                return m_data->data();
            }

            size_t size() const
            {
                return m_data->size();
            }

            size_t capacity() const
            {
                return m_data->capacity();
            }

            void size(size_t s);

            ~Buffer();
    };

    typedef std::shared_ptr<ogl::Buffer> BufferPtr;

}

#endif
