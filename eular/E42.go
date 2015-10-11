package main

import (
	"bufio"
	"fmt"
	"os"
	"math"
)

func is_triangle (n int) bool {
	i := int(math.Sqrt(float64(n * 2)));

	if n == i * (i+1) / 2 {
		return true;
	}
	return false;
}

func main() {
	out := 0;

	bio := bufio.NewReader(os.Stdin)
	for {
		line, _, err := bio.ReadLine()
		if err != nil {
			break;
		}
		r := 0;
		for _, i := range(line) {
			r += int(i) - int('A') + 1;
		}

		if is_triangle(r) {
			out++;
		}
	}

	fmt.Println(out);
}
