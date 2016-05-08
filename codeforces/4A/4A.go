/*
 *http://codeforces.com/contest/4/problem/A
 */

package main

import (
	"fmt"
)

func watermelon(w int) string {
	if w%2 == 0 && w != 2 {
		return "YES"
	}

	return "NO"
}

func main() {
	for {
		var w int
		rc, err := fmt.Scanf("%d", &w)
		if rc < 0 || err != nil {
			break
		}
		fmt.Printf("%s\n", watermelon(w))
	}
}
