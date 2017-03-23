package main

import "fmt"

func longestCommonPrefix(strs []string) string {
	l := 0
	str := ""

	if len(strs) == 0 {
		return str
	}

	for {
		next := true
		if len(strs[0]) <= l {
			break
		}

		c := []rune(strs[0])[l]

		for _, s := range strs {
			if len(s) <= l {
				next = false
				break
			}
			if c != []rune(s)[l] {
				next = false
				break
			}
		}

		if !next {
			break
		}

		str = str + string(c)
		l++
	}

	return str
}

func main() {
	fmt.Printf("%s\n", longestCommonPrefix([]string{}))
	fmt.Printf("%s\n", longestCommonPrefix([]string{ "aa", "a" }))
	fmt.Printf("%s\n", longestCommonPrefix([]string{ "11", "112" }))
	fmt.Printf("%s\n", longestCommonPrefix([]string{ "1xx", "112" }))
}
