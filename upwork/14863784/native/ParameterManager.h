#ifndef PARAMETERMANAGER_H
#define PARAMETERMANAGER_H


#include <stdlib.h>
#include <string.h>
//#include <execinfo.h>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include "HashTable.h"


#define MAX_PARAM_LENGTH 4000
#define MAX_PARAMS 1000

#define debug(fmt,...) do {                                         \
    fprintf(stderr,"[ DEBUG ] : [ %s, %d ] ",__FILE__,__LINE__);    \
    fprintf(stderr,fmt,##__VA_ARGS__);                              \
    fprintf(stderr,"\n");                                           \
} while(0)

/*Enumeration of all parameter types*/

typedef enum
{
    INT_TYPE, REAL_TYPE, BOOLEAN_TYPE, STRING_TYPE, LIST_TYPE
} ParamType;



/*Struct representing Parameter List. It maintains each table as a char* and maintains a pointer to the next element
 * that should be returned by PL_next function. */

struct ParameterListType
{

    int size;
    int front;
    char *params[MAX_PARAMS];

};
typedef struct ParameterListType ParameterList;


/*Union representing Parameter.*/

union param_value
{
    int int_val;
    double real_val;
    Boolean bool_val;
    char* str_val;
    ParameterList *list_val;

};


/*Struct representing ParameterValue.*/

typedef struct ParameterValueType
{

    int initialized;
    int required;
    ParamType type;
    union param_value value;

} ParameterValue;


/*Parameter manager only has one attribute, parameters hashTable.*/

typedef struct ParameterManagerType
{

    hashTable* parameters;

} ParameterManager;


/*Functions provided by Parameter Manager utility.*/

ParameterManager* PM_create(int size);
int PM_destroy(ParameterManager* this);
int PM_hasInitalizedAllRequired();
int PM_parseFrom(ParameterManager* this, FILE* f, char comment_char);
int PM_manage(ParameterManager* this, char* name, ParamType type, int required);
int PM_hasValue(ParameterManager* this, char* name);
char* PL_next(ParameterList* list);
int PL_length(ParameterList* list);
union param_value PM_getValue(ParameterManager* this, char *name);

#endif



