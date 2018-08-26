package nebula.module;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

public class UserComplex {
	private long id;
	private String string;
	private BigDecimal bigDecimal;
	private boolean z;
//	private char c;
	private byte b;
	private short s;
	private int i;
	private long l;
	private float f;
	private double d;
	private Date date;
	private Time time;
	private Timestamp timestamp;

	public long getId() {
		return id;
	}

	public byte getB() {
		return b;
	}
//
//	public char getC() {
//		return c;
//	}

	public short getS() {
		return s;
	}

	public int getI() {
		return i;
	}

	public long getL() {
		return l;
	}

	public boolean isZ() {
		return z;
	}

	public float getF() {
		return f;
	}

	public double getD() {
		return d;
	}

	public BigDecimal getBigDecimal() {
		return bigDecimal;
	}

	public Time getTime() {
		return time;
	}

	public Date getDate() {
		return date;
	}

	public Timestamp getTimestamp() {
		return timestamp;
	}

	public String getString() {
		return string;
	}

	public UserComplex(long id, String string, BigDecimal bigDecimal, boolean z, byte b, short s, int i, long l,
			float f, double d, Date date, Time time, Timestamp timestamp) {
		super();
		this.id = id;
		this.string = string;
		this.bigDecimal = bigDecimal;
		this.z = z;
		this.b = b;
		this.s = s;
		this.i = i;
		this.l = l;
		this.f = f;
		this.d = d;
		this.date = date;
		this.time = time;
		this.timestamp = timestamp;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("UserComplex [id=");
		builder.append(id);
		builder.append(", string=");
		builder.append(string);
		builder.append(", bigDecimal=");
		builder.append(bigDecimal);
		builder.append(", z=");
		builder.append(z);
		builder.append(", b=");
		builder.append(b);
		builder.append(", s=");
		builder.append(s);
		builder.append(", i=");
		builder.append(i);
		builder.append(", l=");
		builder.append(l);
		builder.append(", f=");
		builder.append(f);
		builder.append(", d=");
		builder.append(d);
		builder.append(", date=");
		builder.append(date);
		builder.append(", time=");
		builder.append(time);
		builder.append(", timestamp=");
		builder.append(timestamp);
		builder.append("]");
		return builder.toString();
	}



}
