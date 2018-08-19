package nebula.db;

import java.util.HashMap;
import java.util.Map;

public class Mapping<L, R> {
	Map<L, R> leftToright = new HashMap<>();
	Map<R, L> rightToLeft = new HashMap<>();

	void put(L l, R r) {
		leftToright.put(l, r);
		rightToLeft.put(r, l);
	}

	R getRight(L left) {
		return leftToright.get(left);
	}

	L getLeft(R right) {
		return rightToLeft.get(right);
	}
}
