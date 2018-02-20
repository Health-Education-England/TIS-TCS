package com.transformuk.hee.tis.tcs.api.enumeration;

/**
 * The ProgrammeMembershipType enumeration.
 */
public enum ProgrammeMembershipType {
	SUBSTANTIVE("Substantive"),
	LAT("LAT"),
	FTSTA("FTSTA"),
	MILITARY("Military"),
	VISITOR("Visitor"),
	ACADEMIC("Academic");

	private final String text;

	ProgrammeMembershipType(final String s) {
		text = s;
	}

	public String toString() {
		return text;
	}

	public static ProgrammeMembershipType fromString(String text) {
		for (ProgrammeMembershipType programmeMembershipType : ProgrammeMembershipType.values()) {
			if (programmeMembershipType.text.equalsIgnoreCase(text)) {
				return programmeMembershipType;
			}
		}
		return null;
	}
}
