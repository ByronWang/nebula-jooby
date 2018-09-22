package nebula.intellij.util.container;

import java.util.Iterator;

public class EmptyIterator {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <T> Iterator<T> getInstance() {
		// TODO Auto-generated method stub
		return (Iterator<T>) new Iterator() {

			@Override
			public boolean hasNext() {
				return false;
			}

			@Override
			public Object next() {
				return null;
			}
		};
	}

}
