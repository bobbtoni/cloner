package cloner;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

public class Cloner {
	
	private Map<Object, Object> readyClones;
	private Collection<DeferredClone> deferredClones;
	
	/**
	 * Клонирует объект. Есть ограничение - у объекта и всех вложенных объектов должен быть пустой конструктор.
	 * Как создать объект через конструктор с параметрами стандартными средствами я не нашел. Подобрать конструктор 
	 * по типу или наименованию параметров не получиться, т.к. могут быть параметры с одинаковыми типами и 
	 * названиями отличичными от наименования свойств. 
	 * Я читал про Objenesis, но он не входит в стандартные библиотеки (поправте, если ошибаюсь).
	 */
	public <T> T deepClone(final T object) throws IllegalAccessException {
		readyClones = new HashMap<>();
		deferredClones = new ArrayList<>();
		T clone = createClone(object);
		for (DeferredClone deferredClone : deferredClones) {
			deferredClone.injectDeferredClone(readyClones);
		}
		return clone;
	}
	
	private <T> T createClone(final T object) throws IllegalAccessException {
		if (object == null) {
			return null;
		}
		final Class objectClass = object.getClass();
		if (objectClass.isPrimitive()) {
			return object;
		}
		
		if (Map.class.isAssignableFrom(objectClass)) {
			return (T) cloneMap(object, objectClass);
		} else if (Collection.class.isAssignableFrom(objectClass)) {
			return (T) cloneCollection(object, objectClass);
		}
		T clone = (T) fastClone(object, objectClass);
		if (clone == null) {
			clone = (T) newInstance(object.getClass());
			readyClones.put(object, clone);
		}
		for (final Field field : objectClass.getDeclaredFields()) {
			final int modifier = field.getModifiers();
			if (!Modifier.isStatic(modifier)) {
				field.setAccessible(true);
				final Object clonedValue;
				if (field.getType().isPrimitive()) {
					clonedValue = field.get(object);
				} else if (field.getType().isArray()) {
					clonedValue = cloneArray(field.get(object));
				} else {
					if (readyClones.containsKey(field.get(object))) {
						clonedValue = null;
						deferredClones.add(new DeferredClone(field.get(object), clone, field));
					} else {
						readyClones.put(field.get(object), null);
						clonedValue = createClone(field.get(object));
						readyClones.put(field.get(object), clonedValue);
					}
					
				}
				field.set(clone, clonedValue);
			}
		}
		return clone;
	}
	
	private <T> T newInstance(final Class<T> clazz) {
		try {
			return (T) clazz.getDeclaredConstructor().newInstance();
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			throw new RuntimeException(String.format("Class %s doesn't have empty constuctor", clazz.getName()), e);
		}
	}
	
	private <T> T cloneArray(final T array) throws IllegalAccessException {
		final Class<T> classArray = (Class<T>) array.getClass();
		final int lenght = Array.getLength(array);
		T clone = (T) Array.newInstance(classArray.getComponentType(), lenght);
		for (int i=0; i < lenght; i++) {
			final Object value = Array.get(array, i);
			Array.set(clone, i, createClone(value));
		}
		return clone;
	}
	
	private <T> T fastClone(final Object object, final Class<T> classObject) {
		try {
			Method valueOf = classObject.getDeclaredMethod("valueOf", String.class);
			valueOf.setAccessible(true);
			return (T) valueOf.invoke(null, object.toString());
		} catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
			return null;
		}
	}
	
	private Map<Object, Object> cloneMap(final Object object, final Class objectClass) throws IllegalAccessException {
		final Map<Object, Object> clonedMap;
		final Map<Object, Object> sourceMap = (Map<Object, Object>) object;
		if (HashMap.class.isAssignableFrom(objectClass)) {
			clonedMap = new HashMap<>();
		} else if (TreeMap.class.isAssignableFrom(objectClass)) {
			 clonedMap = new TreeMap<>();
		} else if (LinkedHashMap.class.isAssignableFrom(objectClass)) {
			clonedMap = new LinkedHashMap<>();
		} else if (ConcurrentHashMap.class.isAssignableFrom(objectClass)) {
			clonedMap = new ConcurrentHashMap<>();
		} else {
			throw new IllegalArgumentException(String.format("Unknown map type %s", objectClass.getName()));
		}
		
		for (Entry<Object, Object> entry : sourceMap.entrySet()) {
			Object key = createClone(entry.getKey());
			Object value = createClone(entry.getValue());
			clonedMap.put(key, value);
		}
		return clonedMap;
	}
	
	private Collection<Object> cloneCollection(Object object, Class objectClass) throws IllegalAccessException {
		final Collection<Object> clonedCollection;
		final Collection<Object> sourceCollection = (Collection<Object>) object;
		if (ArrayList.class.isAssignableFrom(objectClass)) {
			clonedCollection = new ArrayList<>();
		} else if (LinkedList.class.isAssignableFrom(objectClass)) {
			clonedCollection = new LinkedList<>();
		} else if (HashSet.class.isAssignableFrom(objectClass)) {
			clonedCollection = new HashSet<>();
		} else if (LinkedHashSet.class.isAssignableFrom(objectClass)) {
			clonedCollection = new LinkedHashSet<>();
		} else if (AbstractList.class.isAssignableFrom(objectClass)) {
			// такая коллекция создается в java.util.Arrays
			return cloneAbstractList(object);
		} else {
			throw new IllegalArgumentException(String.format("Unknown collection type %s", objectClass.getName()));
		}
		for (Object item : sourceCollection) {
			Object clonedItem = createClone(item);
			clonedCollection.add(clonedItem);
		}
		return clonedCollection;
	}
	
	private Collection<Object> cloneAbstractList(final Object object) throws IllegalAccessException {
		final AbstractList<Object> sourceList = (AbstractList<Object>) object;
		Object[] arrayItems = new Object[sourceList.size()];
		int i = 0;
		for (Object item : sourceList) {
			arrayItems[i++] = createClone(item);
		}
		return Arrays.asList(arrayItems);
	}
}
