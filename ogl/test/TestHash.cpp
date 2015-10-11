#include "ogl.h"
#include "gtest/gtest.h"

#include <iostream>

using namespace std;

TEST(ConsistentHashNet, addNode)
{
    char nodeName[] = "hello world";
    char key[] = "hello";

    ogl::ConsistentHashNet hashNet;

    hashNet.addNode(nodeName);
    ASSERT_STREQ(hashNet.getNode(key).c_str(), "hello world");
}
