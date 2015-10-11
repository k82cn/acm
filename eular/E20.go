package main

import "math/big"
import "fmt"

func main() {
	res := big.NewInt(1);
	for i:=2; i<=100; i++ {
		res.Mul(res, big.NewInt(int64(i)));
	}

	var n int;
	for _, i := range(res.String()) {
		n += int(i) - 48;
	}

	fmt.Println(n);
}
