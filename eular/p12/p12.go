package main

import "fmt"

func getSum(n int64) int64 {
	return n * (n + 1) / 2
}

func getFactor(s int64) int64 {
	var r int64 = 0
	for i := int64(1); i < s; i++ {
		if s % i == 0 {
			r++
		}
	}
	return r
}

func main() {
	i := int64(1)
	for {
		s := getSum(i)


		f := getFactor(s)

		fmt.Printf("%d %d\n", i, f)

		if f > 500 {
			break
		}

		i *= 2
	}

}


