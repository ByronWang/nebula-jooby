package nebula.module;

import nebula.tinyasm.data.MethodCode;

public interface Argument {
	void apply(MethodCode mv);
}
