package main

import (
	"fmt"
	"strings"
)

func main() {
	for {
		var n string
		if _, err := fmt.Scanf("%s", &n); err != nil {
			return
		}
		
		n = strings.ToLower(n)
		
		for _, s := range []rune(n) {
			switch s {
			case 'a', 'o', 'y', 'e', 'u', 'i': // Do nothing
			default:
				fmt.Printf(".%c", s)
			}
		}
		fmt.Println()
	}
}
