#include "ogl.h"
#include "gtest/gtest.h"

#include <iostream>

using namespace std;

TEST(HashTable, table_create)
{
    ogl::hash_table_t* my_table = ogl:: table_create();
    ASSERT_TRUE(my_table != 0);
    ogl::table_destroy(my_table);
}

TEST(HashTable, table_insert_get_single)
{
    ogl::hash_table_t* my_table = ogl::table_create();
    ASSERT_TRUE(my_table != 0);

    char d1[] = "world";
    char d2[] = "worldsssss";

    ogl::table_insert(my_table, "hello/0", d1);
    ogl::table_insert(my_table, "hello/1", d2);

    ASSERT_STREQ(d1, (char*)ogl::table_get(my_table, "hello/0"));
    ASSERT_STREQ(d2, (char*)ogl::table_get(my_table, "hello/1"));
    ogl::table_destroy(my_table);
}
