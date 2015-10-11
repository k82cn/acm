#include "process.h"
#include "buffer.h"

#include "types.h"
#include "msg.h"

pid_t ogl::spawn(const char* cmd, char*const* args)
{
    pid_t rc = vfork();
    if (rc == 0)
    {
        logger->Assert(execvp(cmd, args) > 0);
    }

    logger->Assert(rc > 0);

    return rc;
}
