#ifndef HASHTABLE_H
#define HASHTABLE_H
#ifndef BOOLEAN_H
#define BOOLEAN_H

#include <stdlib.h>
#include <stdio.h>
#include <limits.h>
#include <string.h>


struct hashNode
{
    char *key;
    void** value;
    int valueCount;
    struct hashNode *next;
};

typedef struct hashNode tableNode;

/*struct representing a hashTable which would store hashNodes at hash code locations using chaining for collisions.*/

struct hashTableNode
{
    int size;
    struct hashNode **table;
};


typedef struct hashTableNode hashTable;

/*Definition of useful hash table functions which are used in parameter manager.*/

hashTable *createHashTable( int size );
void setValue( hashTable *hashtable, char *key, void *value);
void *getValue( hashTable *hashtable, char *key, int position );

#endif

typedef enum
{
    false, true
} Boolean;


/*For compatibility with stdbool library.*/

typedef Boolean bool;

#endif

