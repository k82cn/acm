package leetcode

import (
	"math"
)

func divide(dividend int, divisor int) int {
	if divisor == 0 {
		return math.MaxInt16
	}

	res := 0
	for {
		dividend -= divisor
		if dividend <= 0 {
			break
		}
		res ++
	}

	return res
}
