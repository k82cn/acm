package main

import (
	"fmt"
)

func removeDuplicates(nums []int) int {
	if len(nums) == 0 {
		return 0
	}

	res := 1
	cur := nums[0]

	for _, n := range nums {
		if n == cur {
			continue
		}
		nums[res] = n

		res ++
		cur = n
	}

	return res
}

func main() {
	tests := []struct {
		nums     []int
		expectedLen int
		expectedArr []int
	}{
		{
			nums:[]int{1,1,2},
			expectedLen:2,
			expectedArr: []int{1,2},
		},
	}

	for i, test := range tests {
		got := removeDuplicates(test.nums)
		if got != test.expectedLen {
			fmt.Printf("Failed at case %d: got %d, expected %d", i, got, test.expectedLen)
		}
	}
}