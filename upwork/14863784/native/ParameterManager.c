#include <ctype.h>
#include "ParameterManager.h"

#ifdef OGL_DEBUG

#define debug(fmt,...) do {                                         \
        fprintf(stderr,"[ DEBUG ] : [ %s, %d ] ",__FILE__,__LINE__);    \
        fprintf(stderr,fmt,##__VA_ARGS__);                              \
        fprintf(stderr,"\n");                                           \
} while(0)

#else
#define debug(fmt,...)
#endif

/*The function provides ability to create new Parameter List.
int front maintains the index of Parameter Value to be returned next by the PL_next method.*/

ParameterList *PL_create( int size )
{

    ParameterList* pl;
    pl = (ParameterList*)malloc(sizeof(ParameterList));
    pl->size = size;

    /*Managing pointer to next parameter to be retrieved.*/
    pl->front = 0;

    int i;
    for(i = 0; i < MAX_PARAMS; i++)
    {
        pl->params[i] = (char*)malloc(sizeof(char*));

    }

    return pl;

}

/*The function provides ability to iterate over next ParameterValue in ParameterList
int front maintains the index of Parameter Value to be returned next by the PL_next method.*/

char* PL_next(ParameterList* list)
{
    char *returnValue = 0;

    if(list->front > list->size - 1)
        return NULL;
    else
    {
        char *str = list->params[list->front];
        if (!str)
        {
            return NULL;
        }
        list->front++;
        return strdup(str);
    }
    return returnValue;
}

int PL_length(ParameterList* list)
{
    return list->size;
}

/*
*    Creates a new parameter manager by allocating memory to hash table
*
*     PRE: size is a positive integer
*
*    POST: Returns a new parameter manager object initialized to be empty (i.e. managing no parameters)
*     on success, NULL otherwise (memory allocation failure)
*/
ParameterManager* PM_create(int size)
{
    ParameterManager* manager = malloc(sizeof(ParameterManager));
    manager->parameters = createHashTable( size );
    if (manager->parameters == NULL)
    {
        free(manager);
        return NULL;
    }

    return manager;


}

/*
*  Frees up memory used by the Parameter Manager and the hashTable.
*
*    PRE: n/a
*    POST: all memory associated with parameter manager p is freed; returns 1 on success, 0 otherwise
*/
int PM_destroy(ParameterManager* manager)
{
    if(manager == NULL)
        return 0;
    if(manager->parameters != NULL)
    {
        int i = 0;
        for(i = 0; i < manager->parameters->size; i++)
        {
            if(manager->parameters->table[i] != NULL)
                free(manager->parameters->table[i]);
        }
    }

    free(manager->parameters);
    free(manager);

    return 1;
}


/*Internal function which checks to see if the provided str is integer. If it is integer, function saves it's int value.*/

Boolean isInteger(char* str, int* val)
{

    char *originalString = (char*)malloc(sizeof(strlen(str) + 1));
    strcpy(originalString, str);

    if (*str == '-')
        ++str;

    if (!*str)
        return false;

    while (*str)
    {

        if (!isdigit(*str))
            return false;
        else
            ++str;
    }

    sscanf(originalString, "%d", val);
    return true;
}



/*Internal function which checks to see if the provided str is a floating number. If it is , function saves it's float value.*/

Boolean isReal(char* str, double* val)
{

    int decimalPoint = 0;
    int floatingNumbers = 0;
    char *originalString = (char*)malloc(sizeof(strlen(str) + 1));
    strcpy(originalString, str);

    if (*str == '-')
        ++str;

    if (!*str)
        return false;

    while (*str)
    {

        if (isdigit(*str))
        {
            if(decimalPoint)
            {
                floatingNumbers  = true;
            }

        }
        else if (*str == '.')
        {
            if(decimalPoint)
            {
                return false;
            }
            else
            {

                decimalPoint = 1;
            }
        }
        else
            return false;


        ++str;

    }

    if(decimalPoint && !floatingNumbers)
    {
        return false;


    }

    sscanf(originalString, "%lf", val);
    return true;
}



/*Internal function which checks to see if the provided str is a Boolean. If it is , function saves it's Boolean value.*/

Boolean isBoolean(char* str, Boolean* val)
{
    if(strcmp(str, "true") == 0)
    {

        *val = true;
        return true;
    }
    if(strcmp(str, "false") == 0)
    {

        *val = false;
        return true;
    }

    return false;

}


/*    Internal function which checks to see if the provided value is formatted as a valid List. If it is, the function
*  Parses the String and converts it into a ParameterList type object.
*
*/

Boolean isList(char* value, ParameterList** val)
{

    char* csvParams;

    /* Not in valid format */
    if (value[0] != '{' || value[strlen(value) - 1] != '}')
    {
        return false;
    }

    csvParams = (char*)calloc(1, strlen(value) + 1);

    int i = 0;

    for (i = 0; i < strlen(value) - 2; i++)
    {
        csvParams[i] = value[i + 1];
    }

    char* tok;
    int count = 0;
    tok = strtok(value, ",");
    while(tok != NULL)
    {
        count++;
        tok = strtok(NULL, ",");
    }



    ParameterList *pl;
    pl = PL_create(count);

    if(pl == NULL)
    {
        return false;
    }

    count = 0;

    /*get the first token*/

    tok = pl->params[count] = strtok(csvParams, ",");
    count++;
    /*walk through other tokens*/
    while(tok)
    {
        void *ptr = strtok(NULL, ",");
        tok = pl->params[count++] = ptr;
    }
	/*
        for (i = 0; i < count; i++) {
            char *current = pl->params[i];
            if (current == NULL) {
                break;
            }
            current[strlen(current) - 1] = 0;
            pl->params[i] = current + 1;
        } */
    *val = pl;

    return true;
}

/*
*    Extract values for parameters from an input stream
*
*    PRE: file passed is a valid input stream ready for reading that contains the desired parameters
*    POST:    All required parameters, and those optional parameters present,
*        are assigned values that are consumed from the file given,

*        character if not nul ('');
*        returns non-zero on success,
*        0 otherwise (parse error, memory allocation failure)
*/

int PM_parseFrom(ParameterManager* manager, FILE* fp, char comment_char)
{
    char c;
    Boolean keyDetected = false;
    int i = 0;
    Boolean insideQuote = false;

    ParameterValue* parameterValue;

    int n = 1024;
    char* buffer = 0;
    if (fp == NULL)
    {
        return 0;
    }

    buffer = (char*) malloc(n);
    if (NULL == buffer)
    {
        return 0;
    }

    while((n = getline(&buffer, &n, fp)) > 0)
    {
        char keys[MAX_PARAM_LENGTH] = {0};
        char values[MAX_PARAM_LENGTH] = {0};

        char* cur = 0;
        char *lp, *rp, *end = buffer + n - 1;
        // replace \n with 0

        debug("line: <%s>", buffer);

        for (cur = buffer; cur != buffer + n; cur++)
        {
            if (comment_char == *cur)
            {
                *cur = 0;
                end = cur;
                break;
            }
        }

        for (cur = end; cur != buffer; cur--)
        {
            if (';' == *cur)
            {
                *cur = 0;
                end = cur;
                break;
            }
        }

        // find # for comments and ignore the comming character
        debug("ignore comments and ';': <%s>", buffer);

        // ignore empty line
        for (lp = buffer; isspace(*lp) && lp < end; lp++)
            ;

        if (lp == end)
        {
            debug("ignore empty line: <%s>", buffer);
            continue;
        }

        // find =
        cur = strchr(buffer, '=');
        if (NULL == cur)
        {
            return 0;
        }

        // copy keys
        for (lp = buffer; isspace(*lp); lp++)
            ;

        for (rp = cur - 1; isspace(*rp); rp--)
            ;
        if (rp <= lp)
        {
            debug("RP: <%c>, LP: <%c>", *rp, *lp);
            return 0;
        }
        memcpy(keys, lp, rp - lp + 1);
        debug("KEY: <%s>", keys);

        // copy values
        for (lp = cur + 1; isspace(*lp); lp++)
            ;

        for (rp = end - 1; isspace(*rp); rp--)
            ;

        if (rp <= lp)
        {
            debug("RP: <%c>, LP: <%c>", *rp, *lp);
            return 0;
        }

        memcpy(values, lp, rp - lp + 1);
        debug("Values: %s", values);

        // parse name & value
        parameterValue = ((ParameterValue*) getValue(manager->parameters, keys, 0));
        if(parameterValue)
        {
            if(parameterValue->type == STRING_TYPE)
            {
                parameterValue->value.str_val =  (char *) calloc(1, sizeof(char) * (strlen(values) + 1));
                strcpy(parameterValue->value.str_val, values);
                debug("String: KEY <%s>, VALUE <%s>", keys, parameterValue->value.str_val);
            }
            else if(parameterValue->type == INT_TYPE)
            {
                Boolean isValid = isInteger(values, &(parameterValue->value.int_val));
                if(!isValid)
                    return 0;

                debug("String: KEY <%s>, VALUE <%d>", keys, parameterValue->value.int_val);
            }
            else if(parameterValue->type == BOOLEAN_TYPE)
            {
                Boolean isValid = isBoolean(values, &(parameterValue->value.bool_val));
                if(!isValid)
                    return 0;
            }
            else if(parameterValue->type == REAL_TYPE)
            {
                Boolean isValid = isReal(values, &(parameterValue->value.real_val));

                if(!isValid)
                    return 0;
            }
            else if(parameterValue->type == LIST_TYPE)
            {
                Boolean isValid = isList(values, &(parameterValue->value.list_val));

                if(!isValid)
                    return 0;
            }

            parameterValue->initialized = 1;
        }
        else
        {
            debug("Failed to get ParamaterValue from manager by key <%s>", keys);
        }

    }


/*
    while ((c = (char) fgetc(fp)) != EOF)
    {
        //Ignore comments
        while(c == comment_char)
        {
            while (c != '\n' && c != EOF)
            {

                c = fgetc(fp);
            }

            while(c == '\n' && c != EOF)
            {
                c = fgetc(fp);
            }

        }

        if(c == EOF)
        {
            break;
        }

        //Ignore all white space.
        if (!isspace(c) || insideQuote)
        {
            if (c == '"')
            {
                insideQuote = !insideQuote;
            }
            if (c == '=')
            {
                if (!keyDetected && i > 0)
                {
                    keys[i] = '\0';
                    keyDetected = true;
                    i = 0;
                }
                // Equals before key
                else
                {
                    return 0;
                }
            }
            else if (c == ';')
            {
                if(keyDetected && i > 0)
                {
                    values[i] = '\0';

                    parameterValue = ((ParameterValue*) getValue(manager->parameters, keys, 0));
                    if(parameterValue)
                    {
                        if(parameterValue->type == STRING_TYPE)
                        {
                            parameterValue->value.str_val =  (char *) calloc(1, sizeof(char) * (strlen(values) + 1));
                            strcpy(parameterValue->value.str_val, values);
                        }
                        else if(parameterValue->type == INT_TYPE)
                        {
                            Boolean isValid = isInteger(values, &(parameterValue->value.int_val));
                            if(!isValid)
                                return 0;
                        }
                        else if(parameterValue->type == BOOLEAN_TYPE)
                        {
                            Boolean isValid = isBoolean(values, &(parameterValue->value.bool_val));
                            if(!isValid)
                                return 0;
                        }
                        else if(parameterValue->type == REAL_TYPE)
                        {
                            Boolean isValid = isReal(values, &(parameterValue->value.real_val));

                            if(!isValid)
                                return 0;
                        }
                        else if(parameterValue->type == LIST_TYPE)
                        {
                            Boolean isValid = isList(values, &(parameterValue->value.list_val));

                            if(!isValid)
                                return 0;
                        }

                        parameterValue->initialized = 1;
                    }
                    keyDetected = false;
                    i = 0;
                }
                else
                {
                    return 0;
                }
            }
            else
            {
                if (i < MAX_PARAM_LENGTH - 1)
                {
                    if(keyDetected)
                    {
                        values[i++] = c;
                    }
                    else
                    {
                        keys[i++] = c;
                    }
                }

                else
                {
                    return 0;
                }
            }
        }

    }


    if(i > 0)
    {
        return 0;
    }
*/

    /*Checks to see if all values have been successfully initialized.*/
    if(manager == NULL)
        return 1;
    if(manager->parameters)
    {
        int i = 0;
        for(i = 0; i < manager->parameters->size; i++)
        {
            if(manager->parameters->table[i] && ( (ParameterValue*)(manager->parameters->table[i]->value[0]) )->required )
            {

                if(!( (ParameterValue*)(manager->parameters->table[i]->value) )->initialized)
                    return 0;

            }

        }
    }
    return 1;
}

/*
*    Register parameter for management.
*
*    PRE: name does not duplicate the name of a parameter already managed
*    POST:    Parameter named name of parameter type type is registered with
*        manager manager as a parameter;
*        if required is zero the parameter will be considered optional,
*        otherwise it will be considered required; returns 1 on success,
*        0 otherwise (duplicate name, memory allocation failure)
*/

int PM_manage(ParameterManager* manager, char* name, ParamType type, int required)
{
    char* keys;
    ParameterValue* value;

    debug("Register parameter <%s>", name);

    if (getValue(manager->parameters, name, 0))
    {
        return 1;
    }
    keys = malloc(sizeof(char) * (strlen(name) + 1));
    strcpy(keys, name);
    if (!keys)
    {
        return 1;
    }
    value = malloc(sizeof(ParameterValue));

    if (value == NULL)
    {
        free(keys);
        return 0;
    }
    value->required = required;
    value->type = type;
    value->initialized = 0;
    setValue( manager->parameters, name, value);


    return 1;
}

/*
Test if a parameter has been assigned a value

*    PRE: name is the name of a parameter currently managed by manager manager
*    POST:    Returns 1 if pname has been assigned a value,
*        0 otherwise (no value, unknown parameter)
*/

int PM_hasValue(ParameterManager* manager, char* name)
{
    ParameterValue* value = (ParameterValue*)getValue(manager->parameters, name, 0);
    if (value != NULL && value->initialized == 1)
    {
        return 1;
    }
    return 0;
}

/*
*    Obtain the value assigned with a parameter of a specified name
*
*    PRE:    the name must refer to a parameter currently managed by this manager
*        and has been assigned a value
*    POST:    Returns the value assigned to a the parameter of the specified name;
*        result is undefined if it has not been assigned a value or is unknown
*/

union param_value PM_getValue(ParameterManager* manager, char *name)
{
    ParameterValue* value =  (ParameterValue*)getValue(manager->parameters, name, 0);
    union param_value returnValue;

    if (value && value->initialized == 1)
    {
        returnValue = value->value;
    }

    return returnValue;
}
