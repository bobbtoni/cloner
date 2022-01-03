package cloner;

import java.lang.reflect.Field;
import java.util.Map;

public class DeferredClone {
	private final Object source;
	private final Object clone;
	private final Field field;
	
	public DeferredClone(Object source, Object clone, Field field) {
		this.source = source;
		this.clone = clone;
		this.field = field;
	}
	
	public void injectDeferredClone(final Map<Object, Object> clones) throws IllegalAccessException {
		if (clones == null || !clones.containsKey(source)) {
			throw new IllegalArgumentException("Clone daesn't found");
		}
		field.setAccessible(true);
		field.set(clone, clones.get(source));
	}
}
