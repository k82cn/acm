#include <iostream>
using std::cout;
using std::hex;
using std::endl;
#include <cassert>
extern "C" {
#include <netinet/in.h> 
#include <string.h> /* memset() */
#include <stdint.h>
}

#define MIGHTCRASH 1

// convinience function to print out a char buffer in hex
void DumpData(const unsigned char* p, const unsigned int len)
{
  for(unsigned int i = 0 ; i<len; i++) {
    cout << hex << static_cast<unsigned short int>(p[i]) << ":";
  }
  cout << endl;
}

int main()
{
  const unsigned int len = 12;
  unsigned char buf[len];
  // fill our buffer with values 0x88
  for(unsigned int i = 0 ; i<len; i++) {
    buf[i] = 0x88;
  }

  // --- THIS IS NOT CORRECT: ---
  uint32_t* p1 = reinterpret_cast<uint32_t*>(&buf[0]);
  // accessing 'p1' works only if the first byte of the buffer is
  // aligned correctly (not a guaranteed situation)
#if MIGHTCRASH
  *p1 = htonl(0xDEADBEEF);
  DumpData(buf,len);
#endif

  uint32_t* p2 = reinterpret_cast<uint32_t*>(&buf[1]);
  // this probably will crash the program, since the second item
  // in the buffer probably is not in integer aligned memory address
  // ei ole sellaisessa muistiosoitteessa, johon voidaan sijoittaa
  // (please note that the previous assignment could crash also)
#if MIGHTCRASH
  *p2 = htonl(0xDEADC0DE);
  DumpData(buf,len);
#endif

  // --- THIS IS THE CORRECT IMPLEMENTATION: ---
  // The solution is to copy the raw data into a correctly allocated
  // variable:

  // a contains our value in network byteorder:
  uint32_t a = htonl(0x0BADF00D); 
  // now we transfer the _bytes_ of variable a into the char-buffer:
  memcpy(reinterpret_cast<char *>(&buf[1]), 
     reinterpret_cast<char *>(&a), 
     sizeof(uint32_t));

  uint16_t b = htons(0xBEEB);
  memcpy(reinterpret_cast<char *>(&buf[5]), 
    reinterpret_cast<char *>(&b), 
    sizeof(uint16_t));

  DumpData(buf,len);

  // when collecting data from the char-buffer, we first initialize
  // a correct size variable and transfer _bytes_ to it:
  uint16_t raw = 0;
  memcpy(reinterpret_cast<char *>(&raw), 
     reinterpret_cast<char *>(&buf[4]), 
    sizeof(uint16_t));

  // 'raw' contains the bytes, but they can still be in wrong byte-order:
  uint16_t port = ntohs(raw);

  cout << hex << port << endl;
  return 0;
}
