#include "fs.h"
#include "msg.h"

#include <sys/stat.h>
#include <sys/types.h>
#include <fcntl.h>
#include <stdlib.h>
#include <netdb.h>

using namespace std;

ogl::Logger* ogl::File::m_log = ogl::Logger::getLogger("ogl.File");

ogl::File::File(const string& path, mode_t mode, int mask): m_path(path)
{
    m_handle = ::open(m_path.c_str(), mode, mask);
    m_log->Assert(m_handle >= 0);
}

ogl::File::File(handle_t handle) : m_handle(handle)
{
}

ogl::File::~File()
{
    ::close(m_handle);
}

void ogl::File::open(const string& fname, mode_t mode, int mask)
{
    m_path = fname;
    m_handle = ::open(fname.c_str(), mode, mask);
    m_log->Assert(m_handle >= 0);
}

size_t ogl::File::write(const Buffer& buff)
{
    const ogl::Buffer* bufPtr = &buff;
    int hr = 0;
    int n = -1;

    int offset = 0;

    while (bufPtr != NULL)
    {
        n = ::write(m_handle, bufPtr->data() + offset, bufPtr->size() - offset);
        if (n < 0)
        {
            break;
        }

        hr += n;

        if ((unsigned int)(n + offset) < bufPtr->size())
        {
            // continue to write current buffer
            offset += n;
        }
        else
        {
            // move to next buffer;
            offset = 0;
            bufPtr = bufPtr->next();
        }
    }

    return hr;
}

size_t ogl::File::write(const char* buff, const size_t& size)
{
    int n = ::write(m_handle, buff, size);
    m_log->Assert(n > 0 && (size_t)n == size);

    return size;
}

size_t ogl::File::read(char* buff, size_t& size)
{
    int n = ::read(m_handle, buff, size);
    m_log->Assert(n >= 0);
    size = n;

    return n;
}

size_t ogl::File::read(Buffer& buff)
{
    int n = -1;

    n = ::read(m_handle, buff.data(), buff.capacity());
    if (n <= 0)
    {
        return n;
    }

    buff.size(n);

    return n;
}

void ogl::File::mkdir(int mask)
{
    ::mkdir(m_path.c_str(), mask);
}

size_t ogl::File::size()
{
    struct stat sb;
    if(::fstat(m_handle, &sb) == -1)
    {
        return -1;
    }
    return sb.st_size;
}
