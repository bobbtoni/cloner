package cloner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class ClonerSimpleTest {
	
	@Test
	public void testSimpleObject() throws IllegalAccessException {
		SimpleClass simpleObject = new SimpleClass(1, "Hello", 2.33);
		SimpleClass clonedObject = new Cloner().deepClone(simpleObject);
		
		assertFalse("Test object copied", simpleObject == clonedObject);
		
		assertEquals(1, clonedObject.getIntField());
		assertEquals("Hello", clonedObject.getStringField());
		assertEquals(Double.valueOf(2.33), clonedObject.getDoubleField());
	}
	
	private static class SimpleClass {
		
		private int intField;
		private String stringField;
		private Double doubleField;
		
		public SimpleClass() {}
		
		public SimpleClass(int intField, String stringField, Double doubleField) {
			this.intField = intField;
			this.stringField = stringField;
			this.doubleField = doubleField;
		}

		public int getIntField() {
			return intField;
		}

		public String getStringField() {
			return stringField;
		}

		public Double getDoubleField() {
			return doubleField;
		}
	}
}
