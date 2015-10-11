#include "ogl.h"

namespace ogl
{

    void ConsistentHashNet::addNode(const std::string& nodeName)
    {
        char digest[16] = {0};
        computeMd5(nodeName, digest);

        for(int h = 0; h < 4; h++)
        {
            long m = this->hash(digest, h);
            m_nodes[m] = nodeName;
        }
    }

    void ConsistentHashNet::removeNode(const std::string& nodeName)
    {
        char digest[16] = {0};
        computeMd5(nodeName, digest);

        for(int h = 0; h < 4; h++)
        {
            long m = this->hash(digest, h);
            m_nodes.erase(m);
        }
    }

    std::string ConsistentHashNet::getNode(const std::string& key)
    {

        if (m_nodes.empty())
        {
            return NULL;
        }

        char digest[16] = {0};
        computeMd5(key, digest);
        long k = this->hash(digest, 0);

        std::map<long, std::string>::iterator res = m_nodes.find(k);

        if (res != m_nodes.end())
        {
            return res->second;
        }

        res = m_nodes.upper_bound(k);
        if (res != m_nodes.end())
        {
            return res->second;
        }
        return m_nodes.begin()->second;
    }

    long ConsistentHashNet::hash(char* digest, int nTime)
    {
        long rv = ((long) (digest[3 + nTime * 4] & 0xFF) << 24) |
                  ((long) (digest[2 + nTime * 4] & 0xFF) << 16) |
                  ((long) (digest[1 + nTime * 4] & 0xFF) << 8) |
                  ((long) (digest[0 + nTime * 4] & 0xFF));

        return rv & 0xffffffffL; /* Truncate to 32-bits */
    }

    void ConsistentHashNet::computeMd5(const std::string& key, char* digest)
    {
        md5_state_t md5state;

        md5_init( &md5state );
        md5_append( &md5state, (const md5_byte_t *) key.c_str(), key.length() );
        md5_finish( &md5state, (unsigned char *) digest);
    }
}
