#!/usr/bin/env python

class Solution:
        # @return a tuple, (index1, index2)
        def twoSum(self, num, target):
            d = {};
            for i, n in enumerate(num):
                # put in map
                if ( n not in d):
                    d[n]=i;
            for i, n in enumerate(num):
                if ((target - n) in d):
                    if ( i == d[target-n]):
                        continue;
                    if (i > d[target-n]):
                        return (d[target-n]+1, i+1);
                    else:
                        return (i+1, d[target-n]+1);

sln = Solution();
print sln.twoSum([2, 7, 11, 15], 9);
print sln.twoSum([3,2,4], 6);
print sln.twoSum([0,4,3,0], 0);