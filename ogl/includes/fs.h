#ifndef _OGL_FS_H_
#define _OGL_FS_H_


#include "buffer.h"
#include "types.h"

#include "log.h"

#include <string>
#include <cstdio>
#include <cstdlib>
extern "C"
{
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <unistd.h>
}

namespace ogl
{
    class File
    {
        private:
            handle_t m_handle;
            std::string m_path;
            static Logger* m_log;

        public:

            static const mask_t default_file_mask = 0644;
            static const mask_t default_dir_mask = 0755;

            File();
            File(handle_t handle);
            File(const std::string& path, mode_t mode,  mask_t mask = default_file_mask);
            ~File();

            /**
             * open a file
             * [in] fname: file path
             * [in] mode: Y_READ, Y_WRITE, Y_RDWR
             */

            void open(const std::string& path, mode_t mode, mask_t mask = default_file_mask);
            /**
             * write data to a file
             * [in] fd: file descriptor
             * [in] buff: data
             */

            size_t write(const char* buffer, const size_t& size);
            /**
             * write data to a file
             * [in] fd: file descriptor
             * [in] buff: data
             */

            size_t write(const Buffer& buff);
            /**
             *
             * read data from a file
             * [in] fd: file descriptor
             * [out] buff: data
             */

            size_t read(Buffer& buff);
            /**
             *
             * read data from a file
             * [in] fd: file descriptor
             * [out] buff: data
             */

            size_t read(char* buf, size_t& size);

            /**
             * create a directory
             * [in] path: directory path
             */

            void mkdir(mask_t mask = default_dir_mask);


            /**
             * get the size of file
             *
             */
            size_t size();
    };

}

#endif
