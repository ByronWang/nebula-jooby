package nebula.module;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

public class UserMoreComplex {
	private long id;
	private String string;
	private BigDecimal bigDecimal;
	private Boolean z;
	private Character c;
	private Byte b;
	private Short s;
	private Integer i;
	private Long l;
	private Float f;
	private Double d;
	private Date date;
	private Time time;
	private Timestamp timestamp;

	public long getId() {
		return id;
	}

	public String getString() {
		return string;
	}

	public BigDecimal getBigDecimal() {
		return bigDecimal;
	}

	public Boolean getZ() {
		return z;
	}

	public Character getC() {
		return c;
	}

	public Byte getB() {
		return b;
	}

	public Short getS() {
		return s;
	}

	public Integer getI() {
		return i;
	}

	public Long getL() {
		return l;
	}

	public Float getF() {
		return f;
	}

	public Double getD() {
		return d;
	}

	public Date getDate() {
		return date;
	}

	public Time getTime() {
		return time;
	}

	public Timestamp getTimestamp() {
		return timestamp;
	}

	public UserMoreComplex(long id, String string, BigDecimal bigDecimal, Boolean z, Character c, Byte b, Short s,
			Integer i, Long l, Float f, Double d, Date date, Time time, Timestamp timestamp) {
		this.id = id;
		this.string = string;
		this.bigDecimal = bigDecimal;
		this.z = z;
		this.c = c;
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
		builder.append("UserMoreComplex [id=");
		builder.append(id);
		builder.append(", string=");
		builder.append(string);
		builder.append(", bigDecimal=");
		builder.append(bigDecimal);
		builder.append(", z=");
		builder.append(z);
		builder.append(", c=");
		builder.append(c);
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
