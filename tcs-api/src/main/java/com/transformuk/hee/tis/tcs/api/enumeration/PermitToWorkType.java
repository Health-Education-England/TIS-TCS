package com.transformuk.hee.tis.tcs.api.enumeration;

public enum PermitToWorkType {
	INDEFINITE_LEAVE("Indefinite leave"),
	HSMP("HSMP"),
	PERMIT_FREE("Permit free"),
	TIER_2("Tier 2"),
	TIER_1("Tier 1"),
	LIMITED_LTR("Limited LTR"),
	WORK_PERMIT("Work permit"),
	DEPENDENT_OF_HSMP("Dependent of HSMP"),
	SPOUSE_OF_EEA_NATIONAL("Spouse of EEA National"),
	SPOUSE_OF_HSMP_HOLDER("Spouse of HSMP holder"),
	TIER_4("Tier 4"),
	STUDENT_VISA("Student visa"),
	DEPENDENT_OF_WORK_PERMIT("Dependent of work permit"),
	OTHER("Other"),
	INDEFINITE_LEAVE_TO_REMAIN("Indefinite Leave to remain"),
	TIER_5("Tier 5"),
	REFUGEE_DOCTOR("Refugee Doctor"),
	POSTGRADUATE_VISA("Postgraduate Visa"),
	TIER_2_POINTS_BASED_SYSTEM("Tier 2 - points based system"),
	UK_NATIONAL("UK National"),
	RESIDENT_PERMIT("Resident Permit"),
	INDEFINATE_LEAVE("Indefinate leave"),
	TIER_4_GENERALS_STUDENT("TIER 4 (GENERAL(S)) STUDENT"),
	ANCESTRY_VISA("Ancestry visa"),
	DEPENDENT_OF_HMSP("Dependent of HMSP"),
	TWES_MTI("TWES/MTI"),
	UNSPECIFIED("Unspecified"),
	YES("Yes"),
	REFUGEE_IN_THE_UK("Refugee in the UK"),
	EVIDENCE_OF_ENTITLEMENT("Evidence of Entitlement"),
	EC_EEA_NATIONAL("EC/EEA National");

	private final String text;

	PermitToWorkType(final String s) {
		text = s;
	}

	public String toString() {
		return text;
	}

	public static PermitToWorkType fromString(String text) {
		for (PermitToWorkType permitToWorkType : PermitToWorkType.values()) {
			if (permitToWorkType.text.equalsIgnoreCase(text)) {
				return permitToWorkType;
			}
		}
		return null;
	}
}

