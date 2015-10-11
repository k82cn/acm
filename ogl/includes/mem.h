#ifndef __OGL_MEM_H__
#define __OGL_MEM_H__

#include "types.h"

namespace ogl
{
    typedef struct _node
    {
        struct _node* next;
        struct _node* prev;
        void* data;
    } node_t;

    typedef struct
    {
        size_t size;
        node_t** data;
    } hash_table_t;

    typedef struct
    {
        char* key;
        void* data;
    } hash_table_node_t;

    typedef int (*node_comparator_t)(void*, void*);
    typedef int (*node_operator_t)(void* );

    node_t* list_create();

    int list_is_empty(node_t* head);

    void* list_pop(node_t* head);

    void list_destroy(node_t* head, node_operator_t oper = 0);

    void list_append(node_t* head, void* data);

    void list_insert(node_t* head, void* data);

    node_t* list_find(node_t* head, void* data, node_comparator_t cmp);

    int list_remove(node_t* head, void* data, node_comparator_t cmp, node_operator_t oper = 0);

    void list_for_each(node_t* head, node_operator_t oper);

    hash_table_t* table_create(size_t size = 997);

    int table_destroy(hash_table_t* table);

    int table_insert(hash_table_t* table, char* key, void* data);

    void* table_get(hash_table_t* table, char* key);

    int table_remove(hash_table_t* table, char* key);

    void table_for_each(hash_table_t* table, node_operator_t oper);

    class MemoryPool
    {
        public:
            virtual ~MemoryPool() = 0;
            virtual void* alloc(size_t size) = 0;
            virtual void free(void* ptr) = 0;
            virtual void refresh(size_t size, size_t blockSize) = 0;
    };

    class Memory
    {
        public:
            Memory(size_t size = BUFSIZ, size_t blockSize = 1);
            ~Memory();
            void* alloc(size_t size);
            void free(void* ptr);

            void refresh(size_t size, size_t blockSize);
        private:
            MemoryPool* m_memPool;
    };

}

#endif
