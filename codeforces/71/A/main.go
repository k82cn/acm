package main

import (
	"fmt"
	"strconv"
)

func main() {
	var n int
	if _, err := fmt.Scanf("%d", &n); err != nil {
		return
	}

	for i := 0; i < n; i++ {
		var in string
		if _, err := fmt.Scanf("%s", &in); err == nil {
			l := len(in)
			if l > 10 {
				fmt.Printf("%s\n", in[:1]+strconv.Itoa(l-2)+in[l-1:])
			} else {
				fmt.Printf("%s\n", in)
			}
		}
	}
}
