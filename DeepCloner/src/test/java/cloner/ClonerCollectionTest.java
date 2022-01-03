package cloner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;

public class ClonerCollectionTest {
	
	@Test
	public void collectionTest() throws IllegalAccessException {
		Embedded embeddedObject1 = new Embedded("this is object 1");
		Embedded embeddedObject2 = new Embedded("this is object 2");
		TestClass testObject = new TestClass(Arrays.asList(embeddedObject1, embeddedObject2));
		TestClass clonedObject = new Cloner().deepClone(testObject);
		
		assertFalse("Test object copied", testObject == clonedObject);
		assertFalse("Test collection copied", testObject.getCollection() == clonedObject.getCollection());
		
		assertEquals(testObject.getCollection().size(), clonedObject.getCollection().size());
		assertFalse("First embedded object copied", clonedObject.getCollection().contains(embeddedObject1));
		assertFalse("Second embedded object copied", clonedObject.getCollection().contains(embeddedObject2));
		assertTrue("Cloned collection doesn't contains embedded object 1", 
				clonedObject.getCollection().stream().anyMatch(item -> embeddedObject1.getStringField().equals(embeddedObject1.getStringField())));
		assertTrue("Cloned collection doesn't contains embedded object 2", 
				clonedObject.getCollection().stream().anyMatch(item -> embeddedObject1.getStringField().equals(embeddedObject1.getStringField())));
		
		
	}
	
	private static class TestClass {
		private Collection<Embedded> collection;
		
		public TestClass() {}

		public TestClass(Collection<Embedded> collection) {
			this.collection = collection;
		}

		public Collection<Embedded> getCollection() {
			return collection;
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
