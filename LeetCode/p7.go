package leetcode

func reverse(x int) int {
	MaxInt := float64(int(^uint32(0) >> 1))

	sign := 1
	if x < 0 {
		sign = -1
	}
	x *= sign

	m := 0
	d := 0.0

	for {
		m *= 10
		m += x % 10
		x = x / 10

		d = float64(m)
		if d > MaxInt {
			return 0
		}

		if x == 0 {
			break
		}
	}

	return m * sign
}

