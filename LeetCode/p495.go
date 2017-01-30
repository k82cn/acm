package leetcode

func findPosisonedDuration(timeSeries []int, duration int) int {
	if timeSeries == nil || len(timeSeries) == 0 {
		return 0
	}

	lastTime := timeSeries[0]
	posioned := 0

	for _, i := range timeSeries[1:] {
		if i - lastTime >= duration {
			posioned += duration
		} else {
			posioned += i - lastTime
		}
		lastTime = i
	}

	return posioned + duration
}
