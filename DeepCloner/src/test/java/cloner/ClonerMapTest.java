package cloner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class ClonerMapTest {
	
	@Test
	public void mapTest() throws IllegalAccessException {
		int key1 = 10000;
		int key2 = 20000;
		Map<Integer, String> map = new HashMap<>();
		map.put(key1, "String1");
		map.put(key2, "String2");
		TestClass testObject = new TestClass(map);
		TestClass clonedObject = new Cloner().deepClone(testObject);
		
		assertFalse("Test object copied", testObject == clonedObject);
		assertFalse("Map copied", testObject.getMap() == clonedObject.getMap());
		
		assertTrue("Cloned map doesn't contains first key", clonedObject.getMap().containsKey(key1));
		assertFalse("Value for first key copied", testObject.getMap().get(key1) == clonedObject.getMap().get(key1));
		assertEquals(testObject.getMap().get(key1), clonedObject.getMap().get(key1));
		
		assertTrue("Cloned map doesn't contains second key", clonedObject.getMap().containsKey(key2));
		assertFalse("Value for second key copied", testObject.getMap().get(key2) == clonedObject.getMap().get(key2));
		assertEquals(testObject.getMap().get(key2), clonedObject.getMap().get(key2));
	}
	
	private static class TestClass {
		
		private Map<Integer, String> map; 
		
		public TestClass() {}
		
		public TestClass(Map<Integer, String> map) {
			this.map = map;
		}
		
		public Map<Integer, String> getMap() {
			return map;
		}
	}
}
