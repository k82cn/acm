#ifndef _OGL_PROCESS_H_
#define _OGL_PROCESS_H_

#include <string>

#include "types.h"
#include "log.h"

namespace ogl
{

// create a process
    pid_t spawn(const char* cmd, char* const* args);

// waitfor the process
    int wait(const pid_t& p);
}

#endif
