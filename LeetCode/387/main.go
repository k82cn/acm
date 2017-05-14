package main

import "fmt"

func firstUniqChar(s string) int {
	cache := map[rune]int{}

	for _, u := range []rune(s) {
		cache[u]++
	}

	for i, u := range []rune(s) {
		c := cache[u]
		if c == 1 {
			return i
		}
	}

	return -1
}

func main() {
	fmt.Printf("%d\n", firstUniqChar("leetcode"))
	fmt.Printf("%d\n", firstUniqChar("loveleetcode"))
}
