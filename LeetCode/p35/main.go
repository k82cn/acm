package main

import (
	"fmt"
	"sort"
)

func searchInsert(nums []int, target int) int {
	return sort.Search(len(nums), func(i int) bool { return nums[i] >= target })
}

func main() {
	fmt.Printf("%d\n", searchInsert([]int{1,3,5,6}, 5))
	fmt.Printf("%d\n", searchInsert([]int{1,3,5,6}, 2))
	fmt.Printf("%d\n", searchInsert([]int{1,3,5,6}, 7))
	fmt.Printf("%d\n", searchInsert([]int{1,3,5,6}, 0))
}
