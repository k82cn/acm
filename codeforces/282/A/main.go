package main

import (
	"fmt"
)

func main() {
	var n int
	if _, err := fmt.Scanf("%d", &n); err != nil {
		return
	}

	x := 0
	for i := 0; i < n; i++ {
		var in string
		if _, err := fmt.Scanf("%s", &in); err != nil {
			return
		}
		switch in {
		case "++X", "X++":
			x = x + 1
		case "--X", "X--":
			x = x - 1
		}
	}

	fmt.Printf("%d\n", x)
}
