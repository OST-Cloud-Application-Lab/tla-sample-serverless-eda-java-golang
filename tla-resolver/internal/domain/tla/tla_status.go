package tla

import "strings"

type Status string

const (
	Proposed Status = "PROPOSED"
	Accepted Status = "ACCEPTED"
	Declined Status = "DECLINED"
	Archived Status = "ARCHIVED"
)

func NewStatus(status string) Status {
	if status == "" {
		panic("Status cannot be empty!")
	}

	// Convert the string to uppercase to ensure case-insensitive comparison
	status = strings.ToUpper(status)

	switch status {
	case string(Proposed):
		return Proposed
	case string(Accepted):
		return Accepted
	case string(Declined):
		return Declined
	case string(Archived):
		return Archived
	default:
		panic("Invalid status value!")
	}
}
