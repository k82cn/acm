package main

import (
	"fmt"
)

func removeElement(nums []int, val int) int {
	var c int
	for i := 0; i < len(nums); i++ {
		if val == nums[i] {
			continue
		}
		nums[c] = nums[i]
		c++
	}
	return c
}

func main() {
	fmt.Printf("%v\n", removeElement([]int{3, 2, 2, 3}, 3))
}
