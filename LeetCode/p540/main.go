package main

import "fmt"

func singleNonDuplicate(nums []int) int {
	// check the first item.
	if nums[1] == nums[2] && nums[0] != nums[1] {
		return nums[0]
	}

	for i := 0; i < len(nums)-2; i++ {
		if nums[i+1] != nums[i] && nums[i+1] != nums[i+2] {
			return nums[i+1]
		}
	}

	// check the latest one.
	l := len(nums)
	if nums[l-3] == nums[l-2] && nums[l-2] != nums[l-1] {
		return nums[l-1]
	}

	return 0
}

func main() {
	fmt.Printf("%d\n", singleNonDuplicate([]int{3, 3, 7, 7, 10, 11, 11}))
	fmt.Printf("%d\n", singleNonDuplicate([]int{1, 1, 2, 3, 3, 4, 4, 8, 8}))
	fmt.Printf("%d\n", singleNonDuplicate([]int{1, 1, 2, 2, 4, 4, 5, 5, 9}))
}
