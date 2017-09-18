package main

import "fmt"

func judgeCircle(moves string) bool {
	var x, y int

	for _, v := range moves {
		switch v {
		case 'U':
			y++
		case 'D':
			y--
		case 'R':
			x++
		case 'L':
			x--
		}
	}

	return x == 0 && y == 0
}

func main() {
	tests := []struct {
		in  string
		out bool
	}{
		{
			in:  "UD",
			out: true,
		},
		{
			in:  "LL",
			out: false,
		},
	}

	for _, test := range tests {
		if judgeCircle(test.in) != test.out {
			fmt.Printf("case %s failed.\n", test.in)
		}
	}
}
