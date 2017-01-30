package leetcode

import (
	"math"
)

func isPalindrome(x int) bool {
	if x < 0 {
		return false
	}

	o := x
	d := 0.0

	for {
		d *= 10
		d += float64(x % 10)

		x = x / 10
		if x == 0 {
			break
		}
	}

	if math.Abs(d - float64(o)) < 0.1 {
		return true
	}

	return false
}
