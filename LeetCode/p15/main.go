package main

import (
	"fmt"
	"sort"
)

func threeSum(nums []int) [][]int {
	var res [][]int

	sort.Ints(nums)

	for i := 0; i < len(nums); i++ {
		t := -nums[i]
		f := i + 1
		b := len(nums) - 1
		for f < b {
			s := nums[f] + nums[b]
			if s > t {
				b--
			} else if s < t {
				f++
			} else {
				fn := nums[f]
				bn := nums[b]
				res = append(res, []int{nums[i], nums[f], nums[b]})

				for f < b && nums[f] == fn {
					f++
				}
				for f < b && nums[b] == bn {
					b--
				}
			}
		}
		for i+1 < len(nums) && nums[i+1] == nums[i] {
			i++
		}
	}

	return res
}

func main() {
	fmt.Printf("%v\n", threeSum([]int{-1, 0, 1, 2, -1, -4}))
}
