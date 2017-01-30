package main

import (
	"fmt"
)

func findMedianSortedArrays(nums1 []int, nums2 []int) float64 {
	var p1, p2 int
	var m1, m2 int
	var c1, c2, cur int

	totalLen := len(nums1) + len(nums2)

	p1 = totalLen / 2
	p2 = p1 + (totalLen+1)%2

	for {
		if nums1[c1] < nums2[c2] {
			m1 = m2
			m2 = nums1[c1]
			cur++
			c1++
		} else if nums1[c1] == nums2[c2] {
			m1 = m2
			m2 = nums1[c1]
			cur++
			c1++

			if p1 != p2 && cur == p2 {
				break
			}

			if p1 == p2 && cur > p2 {
				m1 = m2
				break
			}

			m1 = m2
			m2 = nums2[c2]
			cur++
			c2++
		} else {
			m1 = m2
			m2 = nums2[c2]

			cur++
			c2++
		}

		if p1 != p2 && cur == p2 {
			break
		}

		if p1 == p2 && cur > p2 {
			m1 = m2
			break
		}

	}

	return float64(m1+m2) / 2
}

func main() {
	nums1 := []int{1, 3}
	nums2 := []int{2, 4}

	fmt.Print(findMedianSortedArrays(nums1, nums2))
}

/**
**/
