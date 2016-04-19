package main

import "fmt"

func main() {
    var s int;
    for i := 1; i < 1000; i++ {
        if i % 3 == 0 || i % 5 == 0 {
            s += i;
        }
    }
    fmt.Println(s);
}
