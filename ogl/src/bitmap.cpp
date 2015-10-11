#include <stdlib.h>
#include <unistd.h>

#include "ogl.h"


ogl::LoggerPtr Bitmap::m_logger(ogl::Logger::getLogger("ogl.Bitmap"));


//The test bit - used to find out which blocks are used/free
const BitmapUnit Bitmap::m_testBit = 1;


//Default Constructor
Bitmap::Bitmap()
    :  m_bitmap(0), m_numBitmapUnits(0), m_numBlocks(0), m_usedBlocks(0)
{
}


//Constructor
Bitmap::Bitmap(BitmapSize size) : m_usedBlocks(0)
{
    m_numBlocks = size;

    // Determine how many units we will need to store this bitmap
    m_numBitmapUnits = (m_numBlocks & ~BLOCK_UNIT) >> BITS_PER_UNIT;
    m_lastAvalBit = (m_numBlocks & BLOCK_UNIT);
    if (m_lastAvalBit != 0)
    {
        m_numBitmapUnits ++;
    }
    else
    {
        m_lastAvalBit = BITS_PER_UNIT;
    }

    m_bitmap = (BitmapUnit*)::calloc(m_numBitmapUnits, sizeof(BitmapUnit));
}


//Copy Constructor
Bitmap::Bitmap(const Bitmap& rhs)
{
    copyMemberVariables(rhs);
}


Bitmap& Bitmap::operator=(const Bitmap& rhs)
{
    // Check for self-assignment
    if (this != &rhs)
    {
        copyMemberVariables(rhs);
    }
    return (*this);
}

//Copy the member variables from rhs into this
void Bitmap::copyMemberVariables(const Bitmap& rhs)
{
    // deep copy
    this->m_bitmap = (BitmapUnit*)::malloc(sizeof(BitmapUnit) * rhs.m_numBitmapUnits);
    memcpy(this->m_bitmap, rhs.m_bitmap, sizeof(BitmapUnit) * rhs.m_numBitmapUnits);

    this->m_numBitmapUnits = rhs.m_numBitmapUnits;
    this->m_numBlocks = rhs.m_numBlocks;
    this->m_usedBlocks = rhs.m_usedBlocks;
}

//Destructor
Bitmap::~Bitmap()
{
    if (m_bitmap)
    {
        ::free(m_bitmap);
    }
}

//Allocates multiple blocks.
//@param    numBlocks       [in]  The number of blocks to allocate
//@param    blocks          [out] A vector containing the block numbers
//                                of the blocks that were allocated.  Note
//                                that there may be fewer blocks in the
//                                vector than requested.
BlockId Bitmap::allocateBlock()
{
    // skip the alst unit
    for (ogl::uint_t i = 0; i < m_numBitmapUnits - 1; i++)
    {
        if (m_bitmap[i] < BLOCK_UNIT)
        {
            ogl::uint_t freeBlocks = ~m_bitmap[i];
            for (ogl::uint_t x = m_testBit; x < BLOCK_UNIT; x <<= 1)
            {
                if (x & freeBlocks)
                {
                    // allocate the block
                    m_bitmap[i] |= x;
                    m_usedBlocks++;
                    return (i << BITS_PER_UNIT) + x;
                }
            }
        }
    }

    // handle the alst unit here
    ogl::uint_t freeBlocks = ~m_bitmap[m_numBitmapUnits - 1];
    for (ogl::uint_t i = 1; i <= m_lastAvalBit; i++)
    {
        ogl::uint_t x = m_testBit << i;
        if (freeBlocks & x)
        {
            m_bitmap[m_numBitmapUnits - 1] |= x;
            m_usedBlocks++;
            return ((m_numBitmapUnits - 1) << BITS_PER_UNIT) + i;
        }
    }

    throw std::bad_alloc();
}

//Deallocates a block
//@param    blockId         [in]  The block id of the block to deallocate
void Bitmap::deallocateBlock(const BlockId blockId)
{

    // Decipher 'blockId'
    BitmapSize bitmapUnitIndex = (BitmapSize)((blockId & ~BLOCK_UNIT) >> BITS_PER_UNIT);
    BlockId blockIndex = blockId & BLOCK_UNIT;

    // Check that this block is allocated.
    if (isAllocated(m_bitmap[bitmapUnitIndex], blockIndex))
    {
        // Mark the bit as "free"
        setDeallocated(m_bitmap[bitmapUnitIndex], blockIndex);

        // Decrement the used block count
        m_usedBlocks--;
    }
}

//Get the allocation status of the given block
//@param    blockId         The block whose status to check
//@return   True if the block is allocated, false if it is free
bool Bitmap::getAllocationStatus(const BlockId& blockId)
{

    // Decipher 'blockId'
    BitmapSize bitmapUnitIndex = (BitmapSize)((blockId & ~BLOCK_UNIT) >> BITS_PER_UNIT);
    BlockId blockIndex = blockId & BLOCK_UNIT;

    // Check if it's allocated or not
    return isAllocated(m_bitmap[bitmapUnitIndex], blockIndex);
}


//Determine whether the 'block'th block in BitmapUnit 'unit' is
//allocated or not
//@param    unit        The BitmapUnit to check
//@param    block       The block within the given BitmapUnit to check
//@return   True if the given block is allocated, false otherwise.
bool Bitmap::isAllocated(const BitmapUnit& unit, const BlockId& block)
{
    return ((unit >> block) & m_testBit) != 0;
}


//Set the given block as "allocated"
//Preconditions:        The bit must be "free" before this method
//                      is invoked
//@param    unit        The BitmapUnit to set
//@param    block       The block within the given BitmapUnit to set
void Bitmap::setAllocated(BitmapUnit& unit, const BlockId& block)
{
    unit = (unit | (m_testBit << block));
}


//Set the given block as "free"
//Preconditions:        The bit must be "allocated" before this method
//                      is invoked
//@param    unit        The BitmapUnit to set
//@param    block       The block within the given BitmapUnit to set
void Bitmap::setDeallocated(BitmapUnit& unit, const BlockId& block)
{
    unit = (unit - (m_testBit << block));
}


//Get the number of free blocks in this Bitmap
//@return   The number of free blocks in this Bitmap
BitmapSize Bitmap::getFreeBlocks()
{
    return m_numBlocks - m_usedBlocks;
}


//Get the number of used blocks in this Bitmap
//@return   The number of used blocks in this Bitmap
BitmapSize Bitmap::getUsedBlocks()
{
    return m_usedBlocks;
}

