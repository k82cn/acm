/*
 * Problem: http://codeforces.com/problemset/problem/1/A
 */

package main

import (
	"fmt"
)

func flagstone(n, m, a int) int64 {
	_n := (int64)(n / a)
	if n%a != 0 {
		_n = _n + 1
	}

	_m := (int64)(m / a)
	if m%a != 0 {
		_m++
	}

	return _m * _n
}

func main() {
	for {
		var n, m, a int
		rc, err := fmt.Scanf("%d %d %d", &n, &m, &a)
		if rc < 0 || err != nil {
			break
		}
		fmt.Printf("%d\n", flagstone(n, m, a))
	}
}
