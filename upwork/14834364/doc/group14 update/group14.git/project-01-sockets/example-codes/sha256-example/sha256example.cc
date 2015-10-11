/* 
 * Example code on howto calculate a SHA256 hash using the Nettle library
 * Manual:
 * http://www.lysator.liu.se/~nisse/nettle/nettle.html
 *
 * Jyke Savia 2009-2015
 * License http://creativecommons.org/licenses/by-nc/2.0/
 */

extern "C" {
#include <stdio.h>
#include <stdlib.h>
#include <stdint.h>
#include <time.h>
#include <assert.h>
#include <string.h>   /* strlen(), memcpy() yms. */
#include <arpa/inet.h>  // htonl()

/* SHA256 nettle routines: */
#include <nettle/sha.h>
}

/* ------------------------------------------------------------------------
 * "build" a random number using Linux /dev/urandom special device file
 */
#define INTSIZE 4
uint32_t getrandomint( void )
{
  uint32_t i;
  FILE* rawdata = NULL;

  rawdata = fopen("/dev/urandom", "r");
  if( rawdata == NULL ) { perror("/dev/urandom"); exit(1); }

  if( fread( &i, 1, INTSIZE, rawdata ) != INTSIZE ) {
    perror("urandom fread()"); exit(1);
  }
  fclose( rawdata );

  return i;
}

/* max size for a password in operations */
#define PASSWORD_LEN 20

/* max buffer size (password + 4 byte random number)  */
#define DATA_MAX PASSWORD_LEN + 4

/* sha256-digest size in ascii-hex format */
#define DIGEST_ASCII (SHA256_DIGEST_SIZE*2+1)

/* --------------------------------------------------------------------- */
const char* calculateSHA256( const char* const password, const uint32_t number )
{
  /* data buffer from which the SHA256 will be calculated */
  unsigned char data[DATA_MAX];
  
  /* the binary result */
  unsigned char digest[SHA256_DIGEST_SIZE];

  /* result in ascii-hex format */
  /* because of the static return buffer this routine overwrites the result on
   * each call (not thread safe nor re-entrant)
   */
  static char ascii_hex[ DIGEST_ASCII ];

  /* assumptions for the parameters */
  assert( sizeof(number) == 4 );
  assert( password != NULL );
  int pwd_len = strlen(password);
  assert( pwd_len <= PASSWORD_LEN );

  /* SHA256 using Nettle library */
  { struct sha256_ctx sha256_data;  /* data structure to collect bytes */
    sha256_init( &sha256_data );    /* init */

    /* add password bytes */
    sha256_update( &sha256_data, pwd_len, 
		   reinterpret_cast<uint8_t*>( const_cast<char*>(password)));
    /* add 4 bytes of the random number */
    sha256_update( &sha256_data, 4, 
		   reinterpret_cast<uint8_t*>( const_cast<uint32_t*>(&number)));

    /* calculate SHA256 digest from the data */
    sha256_digest( &sha256_data, SHA256_DIGEST_SIZE, digest );
  }

  /* muutetaan bin‰‰ri digest ascii-hex muotoon */
  { 
  for (int di = 0; di < SHA256_DIGEST_SIZE; ++di)
    sprintf(ascii_hex + di * 2, "%02x", digest[di]);
  }

  /* merkkijonolla on loppunolla */
  ascii_hex[DIGEST_ASCII-1] = '\0';

  return ascii_hex;
}

/* ------------------------------------------------------------------------ */
int main() 
{
  uint32_t random_number = getrandomint();

  /* test outputs.
   * As you see the same passwords generate different hashes, because
   * the random number is part of the hash calculation.
   */
  printf("random number: %u\n", random_number );
  // use number in network byteorder:
  random_number = htonl( random_number );
  printf("elvis SHA256 : %s\n", calculateSHA256( "elvis", random_number ) );
  printf("empty SHA256 : %s\n", calculateSHA256( "", random_number ) );
  printf("nrs   SHA256 : %s\n", calculateSHA256( "1234567890", random_number ));


  return EXIT_SUCCESS;
}
