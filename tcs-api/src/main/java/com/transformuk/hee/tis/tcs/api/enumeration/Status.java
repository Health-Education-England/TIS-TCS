package com.transformuk.hee.tis.tcs.api.enumeration;

/**
 * The Status enumeration.
 */
public enum Status {
	CURRENT("current"),
	INACTIVE("inactive"),
	DELETE("delete");

	private final String text;

	Status(final String s) {
		text = s;
	}

	public String toString() {
		return text;
	}

	public static Status fromString(String text) {
		for (Status status : Status.values()) {
			if (status.text.equalsIgnoreCase(text)) {
				return status;
			}
		}
		return null;
	}
}
