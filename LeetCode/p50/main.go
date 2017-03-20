package main

import "math"

func myPow(x float64, n int) float64 {
	r := x

	if n == 0 || x == 1.0 {
		return 1.0
	}

	if x == -1.0 {
		if n % 2 == 0 {
			return 1.0
		} else {
			return -1.0
		}
	}

	m := n

	if m < 0 {
		m = -n
	}

	for i := 1; i < m; i++ {
		r = r * x
		if math.Abs(r) < math.SmallestNonzeroFloat64 {
			if n < 0 {
				return math.MaxFloat64
			} else {
				return 0
			}
		}

		if math.Abs(r) > math.MaxFloat64 {
			if n < 0 {
				return 0
			} else {
				math.MaxFloat64
			}
		}
	}

	if n < 0 {
		return 1 / r
	}

	return r
}
