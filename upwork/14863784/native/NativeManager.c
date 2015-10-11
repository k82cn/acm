#include <jni.h>
#include <stdio.h>
#include "NativeManager.h"
#include "ParameterManager.h"

ParameterManager* GetJavaNativePointer(JNIEnv* env, jobject obj)
{
    jclass cls = (*env)->GetObjectClass(env, obj);
    jfieldID id = (*env)->GetFieldID(env, cls, "nativeAccessPointer", "J");
    return (ParameterManager*) ((*env)->GetLongField(env, obj, id));
}

void SetJavaNativePointer(JNIEnv* env, jobject obj, ParameterManager* value)
{
    jclass cls = (*env)->GetObjectClass(env, obj);
    jfieldID id = (*env)->GetFieldID(env, cls, "nativeAccessPointer", "J");
    (*env)->SetLongField(env, obj, id, (jlong) value);
}




JNIEXPORT void JNICALL Java_NativeManager_create(JNIEnv *env, jobject obj)
{
    SetJavaNativePointer(env, obj, PM_create(10));
}

char* heapStr(const char* string)
{
    char *s = malloc(sizeof(char) * (strlen(string) + 1));
    strcpy(s, string);
    return s;
}

JNIEXPORT void JNICALL Java_NativeManager_destroy(JNIEnv *env, jobject obj)
{
    ParameterManager* pm = GetJavaNativePointer(env, obj);
    if (pm)
    {
        PM_destroy(pm);
        SetJavaNativePointer(env, obj, NULL);
    }
}

JNIEXPORT void JNICALL Java_NativeManager_manage(JNIEnv *env, jobject obj, jstring parameterName, jbyte parameterType, jboolean required)
{
    ParameterManager* pm = GetJavaNativePointer(env, obj);
    const char* name;
    char* p_name = NULL;
    char type = (char) parameterType;
    ParamType ptype;
    name = (*env)->GetStringUTFChars(env, parameterName, NULL);

    if (name != NULL)
    {
        p_name = heapStr(name);
        if(type == 'i')
        {
            ptype = INT_TYPE;
        }
        else if(type == 's')
        {
            ptype = STRING_TYPE;
        }
        else if(type == 'l')
        {
            ptype = LIST_TYPE;
        }
        else if(type == 'r')
        {
            ptype = REAL_TYPE;
        }
        else if(type == 'b')
        {
            ptype = BOOLEAN_TYPE;
        }
        PM_manage(pm, p_name, ptype, 1);
        (*env)->ReleaseStringUTFChars(env, parameterName, p_name);
    }
}

JNIEXPORT jboolean JNICALL Java_NativeManager_parseFrom(JNIEnv *env, jobject obj, jstring filename)
{
    FILE* f;
    const char* str;
    jboolean result = false;
    jclass cls;

    str = (*env)->GetStringUTFChars(env, filename, NULL);
    if (str)
    {
        f = fopen(str, "r");
        result = PM_parseFrom(GetJavaNativePointer(env, obj), f, '#');
        (*env)->ReleaseStringUTFChars(env, filename, str);
        if(f)
        {
            fclose(f);
        }
    }
    if(!result)
    {
        cls = (*env)->FindClass(env, "except/GenerateException");
        (*env)->ThrowNew(env, cls, "Error at parsing");
    }
    return result;
}

JNIEXPORT jboolean JNICALL Java_NativeManager_hasValue(JNIEnv * env, jobject obj, jstring parameterName)
{
    const char* str;
    char* name;
    jboolean result = false;
    str = (*env)->GetStringUTFChars(env, parameterName, NULL);
    if (str != NULL)
    {
        name = heapStr(str);
        result = PM_hasValue(GetJavaNativePointer(env, obj), name);
        (*env)->ReleaseStringUTFChars(env, parameterName, str);
        free(name);
    }
    return result;
}

JNIEXPORT jstring JNICALL Java_NativeManager_getStringValue(JNIEnv * env, jobject obj, jstring parameterName)
{
    const char* str;
    char* name;
    char* result = NULL;
    str = (*env)->GetStringUTFChars(env, parameterName, NULL);
    if (str != NULL)
    {
        name = heapStr(str);
        result = PM_getValue(GetJavaNativePointer(env, obj), name).str_val;
        (*env)->ReleaseStringUTFChars(env, parameterName, str);
        free(name);
    }
    return (*env)->NewStringUTF(env, result);
}

JNIEXPORT jint JNICALL Java_NativeManager_getIntValue(JNIEnv * env, jobject obj, jstring parameterName)
{
    const char* str;
    char* name;
    int result = 0;
    str = (*env)->GetStringUTFChars(env, parameterName, NULL);
    if (str != NULL)
    {
        name = heapStr(str);
        result = PM_getValue(GetJavaNativePointer(env, obj), name).int_val;
        (*env)->ReleaseStringUTFChars(env, parameterName, str);
        free(name);
    }
    return result;
}

JNIEXPORT jfloat JNICALL Java_NativeManager_getFloatValue(JNIEnv * env, jobject obj, jstring parameterName)
{
    const char* str;
    char* name;
    float result = 0.0;
    str = (*env)->GetStringUTFChars(env, parameterName, NULL);
    if (str != NULL)
    {
        name = heapStr(str);
        result = PM_getValue(GetJavaNativePointer(env, obj), name).real_val;
        (*env)->ReleaseStringUTFChars(env, parameterName, str);
        free(name);
    }
    return result;
}

JNIEXPORT jboolean JNICALL Java_NativeManager_getBooleanValue(JNIEnv * env, jobject obj, jstring parameterName)
{
    const char* str;
    char* name;
    jboolean result = false;
    str = (*env)->GetStringUTFChars(env, parameterName, NULL);
    if (str != NULL)
    {
        name = heapStr(str);
        result = PM_getValue(GetJavaNativePointer(env, obj), name).bool_val;
        (*env)->ReleaseStringUTFChars(env, parameterName, str);
        free(name);
    }
    return result;
}

JNIEXPORT jobjectArray JNICALL Java_NativeManager_getListValue(JNIEnv * env, jobject obj, jstring parameterName)
{
    const char* str;
    char* name;
    char* value;
    int i;
    ParameterList* list;
    jobjectArray result = NULL;
    str = (*env)->GetStringUTFChars(env, parameterName, NULL);
    if (str)
    {
        name = heapStr(str);
        list = PM_getValue(GetJavaNativePointer(env, obj), name).list_val;

        result = (jobjectArray) (*env)->NewObjectArray(env, list->size, (*env)->FindClass(env, "java/lang/String"), NULL);
        i = 0;
        while((value = PL_next(list)) != NULL)
        {
            (*env)->SetObjectArrayElement(env, result, i, (*env)->NewStringUTF(env, value));
            i++;
        }
        (*env)->ReleaseStringUTFChars(env, parameterName, str);
        free(name);
    }
    return result;
}
