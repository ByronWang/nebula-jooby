package nebula.module;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

public class UserComplex {
	private long id;
	private byte b;
	private char c;
	private short s;
	private int i;
	private long l;
	private boolean z;
	private float f;
	private double d;
	private BigDecimal bigDecimal;
	private Time time;
	private Date date;
	private Timestamp timestamp;
	private String string;

	public UserComplex(long id, byte b, char c, short s, int i, long l, boolean z, float f, double d,
			BigDecimal bigDecimal, Time time, Date date, Timestamp timestamp, String string) {
		super();
		this.id = id;
		this.b = b;
		this.c = c;
		this.s = s;
		this.i = i;
		this.l = l;
		this.z = z;
		this.f = f;
		this.d = d;
		this.bigDecimal = bigDecimal;
		this.time = time;
		this.date = date;
		this.timestamp = timestamp;
		this.string = string;
	}

}
