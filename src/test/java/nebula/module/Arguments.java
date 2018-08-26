package nebula.module;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

//Primitives: boolean, byte, short, int, long, char, float, and double
//
//java.lang: Boolean, Byte, Short, Integer, Long, Character, Float, Double, String, and Enum (stored as the enum value’s name)
//
//java.math: BigDecimal
//
//java.net: Inet4Address, Inet6Address, URL, and URI
//
//java.sql: Blob, Clob, Date, Time, and Timestamp
//
//java.time: Instant, LocalDate, LocalDateTime, LocalTime, OffsetDateTime, ZonedDateTime, and ZoneId
//
//java.util: Date, Optional (around any other supported type), and UUID
//
//java.util.Collection and Java arrays (stored as SQL arrays). Some additional setup may be required depending on the type of array element.
public class Arguments {

	Map<Class<?>, Map<Class<?>, Entry>> arguments = new HashMap<>();

	static class Entry {
		Argument convert;
		Argument revert;

		public Entry(Argument convert, Argument revert) {
			this.convert = convert;
			this.revert = revert;
		}

	}

	void register(Class<?> fromClazz, Class<?> toClazz, Argument convert, Argument revert) {
		if (!arguments.containsKey(toClazz)) {
			arguments.put(toClazz, new HashMap<>());
		}
		arguments.get(toClazz).put(fromClazz, new Entry(convert, revert));
	}

	Argument getConvert(Class<?> pojoClazz, Class<?> jdbcClazz) {
		if (!arguments.containsKey(jdbcClazz)) return null;
		return arguments.get(jdbcClazz).get(pojoClazz).convert;
	}

	Argument getRevert(Class<?> pojoClazz, Class<?> jdbcClazz) {
		if (!arguments.containsKey(jdbcClazz)) return null;
		return arguments.get(jdbcClazz).get(pojoClazz).revert;
	}

	{
		register(char.class, String.class, mv -> {
			mv.STATIC(String.class, "valueOf").parameter(char.class).reTurn(String.class).INVOKE();
		}, mv -> {
			mv.LOADConst(0);
			mv.VIRTUAL(String.class, "charAt").parameter(int.class).reTurn(char.class).INVOKE();
		});

		register(Boolean.class, boolean.class, mv -> {
			mv.VIRTUAL(Boolean.class, "booleanValue").reTurn(boolean.class).INVOKE();
		}, mv -> {
			mv.STATIC(Boolean.class, "valueOf").parameter(boolean.class).reTurn(Boolean.class).INVOKE();
		});

		register(Byte.class, byte.class, mv -> {
			mv.VIRTUAL(Byte.class, "byteValue").reTurn(byte.class).INVOKE();
		}, mv -> {
			mv.STATIC(Byte.class, "valueOf").parameter(byte.class).reTurn(Byte.class).INVOKE();
		});

		register(Short.class, short.class, mv -> {
			mv.VIRTUAL(Short.class, "shortValue").reTurn(short.class).INVOKE();
		}, mv -> {
			mv.STATIC(Short.class, "valueOf").parameter(short.class).reTurn(Short.class).INVOKE();
		});

		register(Integer.class, int.class, mv -> {
			mv.VIRTUAL(Integer.class, "intValue").reTurn(int.class).INVOKE();
		}, mv -> {
			mv.STATIC(Integer.class, "valueOf").parameter(int.class).reTurn(Integer.class).INVOKE();
		});

		register(Long.class, long.class, mv -> {
			mv.VIRTUAL(Long.class, "longValue").reTurn(long.class).INVOKE();
		}, mv -> {
			mv.STATIC(Long.class, "valueOf").parameter(long.class).reTurn(Long.class).INVOKE();
		});

		register(Character.class, String.class, mv -> {//
			mv.VIRTUAL(Character.class, "charValue").reTurn(char.class).INVOKE();
			mv.STATIC(String.class, "valueOf").parameter(char.class).reTurn(String.class).INVOKE();
		}, mv -> {
			mv.LOADConst(0);
			mv.VIRTUAL(String.class, "charAt").parameter(int.class).reTurn(char.class).INVOKE();
			mv.STATIC(Character.class, "valueOf").parameter(char.class).reTurn(Character.class).INVOKE();
		});

		register(Float.class, float.class, mv -> {
			mv.VIRTUAL(Float.class, "floatValue").reTurn(float.class).INVOKE();
		}, mv -> {
			mv.STATIC(Float.class, "valueOf").parameter(float.class).reTurn(Float.class).INVOKE();
		});

		register(Double.class, double.class, mv -> {
			mv.VIRTUAL(Double.class, "doubleValue").reTurn(double.class).INVOKE();
		}, mv -> {
			mv.STATIC(Double.class, "valueOf").parameter(double.class).reTurn(Double.class).INVOKE();
		});

		register(Date.class, java.sql.Date.class, mv -> {
			mv.STATIC(Arguments.class, "dateValueOf").parameter(Date.class).reTurn(java.sql.Date.class).INVOKE();
		}, mv -> {
			mv.STATIC(Arguments.class, "dateValueOf").parameter(java.sql.Date.class).reTurn(Date.class).INVOKE();
		});
		register(Date.class, java.sql.Time.class, mv -> {
			mv.STATIC(Arguments.class, "timeValueOf").parameter(Date.class).reTurn(java.sql.Time.class).INVOKE();
		}, mv -> {
			mv.STATIC(Arguments.class, "dateValueOf").parameter(java.sql.Time.class).reTurn(Date.class).INVOKE();
		});
		register(Date.class, java.sql.Timestamp.class, mv -> {
			mv.STATIC(Arguments.class, "timestampValueOf")
				.parameter(Date.class)
				.reTurn(java.sql.Timestamp.class)
				.INVOKE();
		}, mv -> {
			mv.STATIC(Arguments.class, "dateValueOf").parameter(java.sql.Timestamp.class).reTurn(Date.class).INVOKE();
		});
	}

	public static java.sql.Date dateValueOf(Date date) {
		return new java.sql.Date(date.getTime());
	}

	public static java.sql.Time timeValueOf(Date date) {
		return new java.sql.Time(date.getTime());
	}

	public static java.sql.Timestamp timestampValueOf(Date date) {
		return new java.sql.Timestamp(date.getTime());
	}

	public static Date dateValueOf(java.sql.Date date) {
		return new Date(date.getTime());
	}

	public static Date dateValueOf(java.sql.Time date) {
		return new Date(date.getTime());
	}

	public static Date dateValueOf(java.sql.Timestamp date) {
		return new Date(date.getTime());
	}
}
