#ifndef __OGL_BITMAP_H__
#define __OGL_BITMAP_H__

#include "types.h"
#include "log.h"

#define BITS_PER_UNIT         (ogl::uint_t)16
#define BLOCK_UNIT            0xFFFF

typedef ogl::uint_t           BitmapUnit;
typedef ogl::uint_t           BitmapSize;
typedef ogl::uint_t           BlockId;

class Bitmap
{

    public:
        Bitmap();

        Bitmap(BitmapSize size);

        Bitmap(const Bitmap& rhs);
        Bitmap& operator=(const Bitmap& rhs);

        virtual ~Bitmap();

        /*************************** PUBLIC METHODS *****************************/

        //Allocates multiple blocks.
        //@param    numBlocks       [in]  The number of blocks to allocate
        //@param    blocks          [out] A vector containing the block numbers
        //                                of the blocks that were allocated.  Note
        //                                that there may be fewer blocks in the
        //                                vector than requested.
        BlockId allocateBlock();

        //Deallocates a block
        //@param    blockId         [in]  The block id of the block to deallocate
        void deallocateBlock(const BlockId blockId);

        //Get the allocation status of the given block
        //@param    blockId         The block whose status to check
        //@return   True if the block is allocated, false if it is free
        bool getAllocationStatus(const BlockId& blockId);

        //Get the number of free blocks in this Bitmap
        //@return   The number of free blocks in this Bitmap
        BitmapSize getFreeBlocks();

        //Get the number of used blocks in this Bitmap
        //@return   The number of used blocks in this Bitmap
        BitmapSize getUsedBlocks();

    private:

        //The test bit - used to find out which blocks are used/free
        static const BitmapUnit m_testBit;

        /*************************** MEMBER VARIABLES ****************************/
        //The bitmap
        BitmapUnit* m_bitmap;

        BitmapSize m_lastAvalBit;
        //The number of units that make up the bitmap
        BitmapSize m_numBitmapUnits;

        // The total number of blocks in the whole bitmap
        BitmapSize m_numBlocks;

        // The number of used blocks
        BitmapSize m_usedBlocks;

        static ogl::LoggerPtr m_logger;

        /************************ PRIVATE HELPER METHODS *************************/

        //Determine whether the 'block'th block in BitmapUnit 'unit' is
        //allocated or not
        //@param    unit        The BitmapUnit to check
        //@param    block       The block within the given BitmapUnit to check
        //@return   True if the given block is allocated, false otherwise.
        bool isAllocated(const BitmapUnit& unit, const BlockId& block);

        //Set the given block as "allocated"
        //Preconditions:        The bit must be "free" before this method
        //                      is invoked
        //@param    unit        The BitmapUnit to set
        //@param    block       The block within the given BitmapUnit to set
        void setAllocated(BitmapUnit& unit, const BlockId& block);

        //Set the given block as "free"
        //Preconditions:        The bit must be "allocated" before this method
        //                      is invoked
        //@param    unit        The BitmapUnit to set
        //@param    block       The block within the given BitmapUnit to set
        void setDeallocated(BitmapUnit& unit, const BlockId& block);

        //Copy the member variables from rhs into this
        void copyMemberVariables(const Bitmap& rhs);

};

#endif
