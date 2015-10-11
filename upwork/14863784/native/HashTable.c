#define RANDOM_HASH_PRIME 5351

#include <stdlib.h>
#include <stdio.h>
#include <limits.h>
#include <string.h>
#include "HashTable.h"


/*Creates a hashTable by allocating memory for it and setting attributes.*/

hashTable *createHashTable( int size )
{

    hashTable *hashtable = malloc( sizeof( hashTable ) );


    if( ( hashtable) == NULL )
    {
        return hashtable;
    }

    hashtable->table = malloc( sizeof(tableNode *) * size );

    if(  hashtable->table == NULL )
    {
        return NULL;
    }

    hashtable->size = size;
    int i = 0;

    for( i = 0; i < size; i++ )
    {
        hashtable->table[i] = NULL;
    }

    return hashtable;
}

/* The following function generates a hash code. The algorithm uses a prime number
 * and bit shifting to increase efficiency. Shifting left by 5 multiplies the hash value by 32. The hash code
 * returned is always between 0 and the size.
 *
 */

int generateHash( hashTable *hashtable, char *key )
{

    unsigned long int hash = RANDOM_HASH_PRIME;
    int i = 0;

    /*Formula uses ASCII value of each character.*/

    while(i < strlen( key ) )
    {
        hash = hash << 5;
        hash += key[ i++ ];

    }

    /*Using mode function ensures the hash code is always between 0 and size of the table.*/

    return hash % hashtable->size;
}

/* The function below is responsible for creating of the node representing key,value pair.
 * Memory first allocated for the tableNode struct which encapsules key/value pair and then for the key/value
 * attribute of the struct. Once memory has been allocated, values are copied over.
 */

tableNode *createTableEntry( char *key, void *value)
{
    tableNode *newpair;

    newpair = malloc( sizeof( tableNode ) );

    if( newpair == NULL )
    {
        return NULL;
    }
    newpair->key = (char *) malloc(strlen(key) + 1);
    newpair->value = (void *) malloc(sizeof(void*));

    strcpy(newpair->key, key);

    /*Value is of type void ** and therefore can store multiple values. This function always stores the value at index 0.*/

    newpair->value[0] = value;
    newpair->valueCount = 1;
    newpair->next = NULL;

    return newpair;
}

/* This function is responsible for inserting values into hashTable. The function uses chaining in cases of collision.
 * Chaining is implemented using linked list concept.
 */

void setValue( hashTable *hashtable, char *key, void *value )
{
    int location = 0;
    tableNode *newpair = NULL;
    tableNode *next = NULL;
    tableNode *last = NULL;

    location = generateHash( hashtable, key );

    next = hashtable->table[ location ];

    /*If multiple exists at same location, retrieve using chaining.*/

    while( next != NULL && next->key != NULL && strcmp( key, next->key ) > 0 )
    {
        last = next;
        next = next->next;
    }

    /*The key/value node doesn't exist. Create new node and insert it into hashtable at location.*/
    if( !(next != NULL && next->key != NULL && strcmp( key, next->key ) == 0 ))
    {

        newpair = createTableEntry( key, value );

        if( next == hashtable->table[ location ] )
        {
            newpair->next = next;
            hashtable->table[ location ] = newpair;

        }
        else if ( next == NULL )
        {
            last->next = newpair;

        }
        else
        {
            newpair->next = next;
            last->next = newpair;
        }


    }
    else
    {

        int i = 0;
        for(i = 0; i < next->valueCount; i++)
        {


        }
        next->value[i] = (void *) malloc(sizeof(void*));
        next->value[i] = value;

    }
}

/*This function is responsible for retrieving the value associated with provided key from the hashTable.*/

void *getValue( hashTable *hashtable, char *key, int position)
{
    int location = 0;
    tableNode *pair;

    location = generateHash( hashtable, key );

    /*In case multiple keys map to same location, iterate over the list to find the one needed.*/
    pair = hashtable->table[ location ];
    while( pair != NULL && pair->key != NULL && strcmp( key, pair->key ) > 0 )
    {
        pair = pair->next;
    }

    /*Return the Value if found, NULL otherwise.*/
    if( pair == NULL || pair->key == NULL || strcmp( key, pair->key ) != 0 )
    {
        return NULL;

    }
    else
    {
        return pair->value[position];
    }

}

