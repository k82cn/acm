package main

func myPow(x float64, n int) float64 {
	r := x

	if n == 0 {
		return 1.0
	}

	m := n

	if m < 0 {
		m = -n
	}

	for i := 1; i < m; i++ {
		r = r * x
	}

	if n < 0 {
		return 1 / r
	}

	return r
}
