#include <iostream>

extern "C" {
#include <stdio.h>
#include <stdlib.h>
}

using namespace std;

int main(int argc, char** argv) {

  int pipes[2];

  int rc = ::pipe(pipes);
  if (rc < 0) {
    return -1;
  }

  string data =
    "This data is much larger than BUFFERED_READ_SIZE, which means it will "
    "trigger multiple buffered async reads as a result.........";

  while (data.length() < 3 * 16 * 4096) {
    data.append(data);
  }

  size_t offset = 0;
  
  while (offset < data.length()) {
    ssize_t length =
      ::write(pipes[1], data.data() + offset, data.length() - offset);

    if (length < 0) {
      // TODO(benh): Handle a non-blocking fd? (EAGAIN, EWOULDBLOCK)
      if (errno == EINTR) {
        continue;
      }
      return -1;
    }

    offset += length;
  }

  return 0;
}
