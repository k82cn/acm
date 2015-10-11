#include "buffer.h"

ogl::LoggerPtr ogl::Data::m_logger(ogl::Logger::getLogger("ogl.data"));
ogl::LoggerPtr ogl::Buffer::m_logger(ogl::Logger::getLogger("ogl.buffer"));


ogl::Data::Data(const Data& m_d) {}
ogl::Data& ogl::Data::operator=(const Data & m_d)
{
    return *this;
}


void ogl::Data::_inc_ref_cnt()
{
    m_refcnt++;
};

void ogl::Data::_dec_ref_cnt()
{
    m_refcnt--;
    if (m_refcnt == 0)
    {
        delete this;
    }
};

void ogl::Data::size(size_t s)
{
    m_logger->Assert(s <= m_capacity, ERR_INVALID_SIZE_BIG);

    m_size = s;
}


ogl::Data::Data(const char* msg, size_t len)
{
    m_logger->Assert(msg != 0, ERR_NULL_BUFFER);
    m_size = len;
    m_capacity = len * 2;

    m_data = new char[m_capacity];
    memcpy(m_data, msg, m_size);

    m_refcnt = 1;
}

ogl::Data::Data(int size)
{
    m_logger->Assert(size > 0, ERR_INVALID_SIZE_NEG);
    m_data = new char[size];
    m_size = 0;
    m_data[m_size] = 0;
    m_capacity = size;

    m_refcnt = 1;
}

ogl::Data::~Data()
{
    delete[] m_data;
}

void ogl::Buffer::_inc_ref_cnt()
{
    m_refcnt++;
};

void ogl::Buffer::_dec_ref_cnt()
{
    m_refcnt--;
    if (m_refcnt == 0)
    {
        delete this;
    }
};

ogl::Buffer::Buffer(const char* msg, int len)
{
    m_data = new Data(msg, len);
    m_next = 0;
}

ogl::Buffer::Buffer(int size)
{
    m_data = new Data(size);
    m_next = 0;
}

ogl::Buffer::Buffer(const Buffer& buf)
{
    m_data = buf.m_data;
    m_next = buf.m_next;

    if (m_data)
    {
        m_data->_inc_ref_cnt();
    }
    if (m_next)
    {
        m_next->_inc_ref_cnt();
    }
}

ogl::Buffer& ogl::Buffer::operator=(const Buffer & buf)
{
    m_data = buf.m_data;
    m_next = buf.m_next;

    if (m_data)
    {
        m_data->_inc_ref_cnt();
    }

    if (m_next)
    {
        m_next->_inc_ref_cnt();
    }

    return *this;
}

void ogl::Buffer::append(Buffer* next)
{
    m_logger->Assert(next != 0, ERR_NULL_BUFFER);
    m_next = next;
};

void ogl::Buffer::append(const char* msg, size_t len)
{
    m_logger->Assert(msg != 0, ERR_NULL_BUFFER);
    size_t idle = m_data->idle();

    if (idle > len)
    {
        memcpy(data() + size(), msg, len);
        m_data->size(m_data->size() + len);
    }
    else
    {
        memcpy(data() + size(), msg, idle);
        m_data->size(m_data->capacity());

        m_next = new Buffer(m_data->capacity());
        m_next->_inc_ref_cnt();

        m_next->append(msg + idle, len - idle);
    }
}

void ogl::Buffer::size(size_t s)
{
    m_logger->Assert(s <= capacity(), ERR_INVALID_SIZE);
    m_data->size(s);
}


ogl::Buffer::~Buffer()
{
    m_logger->Assert(m_data != 0, ERR_INVALID_STATUS);

    if (m_data)
    {
        m_data->_dec_ref_cnt();
    }
    if (m_next)
    {
        m_next->_dec_ref_cnt();
    }
}

