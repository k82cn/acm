/* 
 * example on howto calculate HMAC-SHA256 signature in Lintula environment
 * using the Nettle library, which is installed in directory:
 * /usr/local/lang/nettle-3.0
 * Manual for the nettle can be found:
 * http://www.lysator.liu.se/~nisse/nettle/nettle.html
 *
 * Jyke Savia 2011-2015
 * License http://creativecommons.org/licenses/by-nc/2.0/
 *
 */

extern "C" {
#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <assert.h>
#include <string.h>   /* strlen(), memcpy() yms. */

/* nettle headers */
#include <nettle/hmac.h>
#include <nettle/sha.h>
}

/* sha256-digest size in ascii-hex format (plus string end zero) */
#define DIGEST_ASCII (SHA256_DIGEST_SIZE*2+1)

/* --------------------------------------------------------------------- */
const char* HMACSHA256( const uint8_t* const data, 
			unsigned int data_len,
			const char* const password )
{
  /* calculated result as binary bytes */
  unsigned char digest[SHA256_DIGEST_SIZE];

  /* buffer for the digest in ascii-hex format */
  static char ascii_hex[ DIGEST_ASCII ];


  /* assumptions of parameters */
  assert( data != NULL );
  assert( data_len > 0 );
  assert( password != NULL );
  unsigned int pwd_len = strlen( password );
  assert( pwd_len > 0 );

  /* HMACSHA256 calculation using nettle routines: */
  { struct hmac_sha256_ctx signature;  /* data struct to collect the result */

    /* Initialize the signature key: */
    hmac_sha256_set_key( &signature, pwd_len, (uint8_t*)password ); 
    /* Add data to the calculation: */
    hmac_sha256_update( &signature, data_len, data );
    /* After all data is added, request the binary signature for it: */
    hmac_sha256_digest( &signature, SHA256_DIGEST_SIZE, digest );
  }

  /* convert the binary signature into ascii-hex format: */
  { int di;
  for (di = 0; di < SHA256_DIGEST_SIZE; ++di)
    sprintf(ascii_hex + di * 2, "%02x", digest[di]);
  }

  /* a C-string contains terminating zero: */
  ascii_hex[DIGEST_ASCII-1] = '\0';

  return ascii_hex;
}

/* ------------------------------------------------------------------------ */
void testCase( const char* const data, const char* const password )
{
  unsigned len = strlen( data );
  printf("\nData:\n%s\nSignature:\n%s\n", 
	 data, 
	 HMACSHA256( (uint8_t*)data, len, password ));
}
	 
/* ------------------------------------------------------------------------ */
int main() 
{
  testCase( "elvis", "a password");
  testCase( "elvis", "other password string" );
  testCase( "elvis", NULL );

  return EXIT_SUCCESS;
}
