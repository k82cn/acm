package leetcode

func findComplement(num int) int {
	n := uint32(num)
	f := uint32(1) << 31

	for {
		if f == 0 {
			break
		}

		if n & f == 0 {
			f >>= 1
		} else {
			break
		}
	}

	m := uint32(0)
	for {
		if f == 0 {
			break
		}

		if n & f == 0 {
			m |= f
		} else {
			m &= ^f
		}

		f >>= 1
	}

	return int(m)
}
