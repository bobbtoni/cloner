package cloner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class ClonerEmbeddedObjectTest {
	
	
	@Test
	public void embeddedObjectTest() throws IllegalAccessException {
		Embedded embeddedObject = new Embedded("Hello");
		TestClass testObject = new TestClass(embeddedObject);
		TestClass clonedObject = new Cloner().deepClone(testObject);
		
		assertFalse("Test object copied", testObject == clonedObject);
		assertFalse("Embedded object copied", embeddedObject == clonedObject.getEmbeddedObject());
		
		assertEquals("Hello", clonedObject.getEmbeddedObject().getStringField());
	}
	
	private static class TestClass {
		Embedded embeddedObject;
		
		public TestClass() {}

		public TestClass(Embedded embeddedObject) {
			this.embeddedObject = embeddedObject;
		}

		public Embedded getEmbeddedObject() {
			return embeddedObject;
		}
	}
	
	private static class Embedded {
		private String stringField;
		
		public Embedded() {}
		
		public Embedded(String stringField) {
			this.stringField = stringField;
		}
		
		public String getStringField() {
			return stringField;
		}
	}
}
