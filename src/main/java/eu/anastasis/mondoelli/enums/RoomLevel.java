package eu.anastasis.mondoelli.enums;

public enum RoomLevel {
	LEVEL_01,
	LEVEL_02,
	LEVEL_11,
	LEVEL_12,
	LEVEL_21,
	LEVEL_22,
	LEVEL_31,
	LEVEL_32;

	public static RoomLevel getNext(RoomLevel l) {
		if (l.ordinal() == RoomLevel.values().length - 1) {
			return l;
		} else {
			return RoomLevel.values()[l.ordinal() + 1];
		}
	}

	public static RoomLevel getPrevious(RoomLevel l) {
		if (l.ordinal() == 0) {
			return l;
		} else {
			return RoomLevel.values()[l.ordinal() - 1];
		}
	}

}
