package main

import (
	"fmt"
)

func main() {
	for {
		var input string
		if _, err := fmt.Scanf("%s", &input); err != nil {
			return
		}

		cache := make(map[string]int, len(input))

		l := len(input)
		
		for i:=0; i<l; i++ {
			cache[input] = 1
			input = input[l-1:] + input[:l-1]
		}
		
		fmt.Println(len(cache))
	}
}
