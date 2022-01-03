package cloner;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ClonerRecursiveLinks {
	
	@Test
	public void nestedRecursionTest() throws IllegalAccessException {
		TestClass testObject = new TestClass();
		TestClass testLink = new TestClass();
		testObject.setLink(testLink);
		testLink.setLink(testObject);
		TestClass clonedObject = new Cloner().deepClone(testObject);
		
		assertFalse("Test object copied", testObject == clonedObject);
		assertFalse("Test link copied", testLink == clonedObject.getLink());
		
		assertTrue("Objects do not point to each other ", clonedObject == clonedObject.getLink().getLink());
	}
	
	@Test
	public void yourSelfLinkTest() throws IllegalAccessException {
		TestClass testObject = new TestClass();
		testObject.setLink(testObject);
		TestClass clonedObject = new Cloner().deepClone(testObject);
		
		assertFalse("Test object copied", testObject == clonedObject);
		
		assertTrue("The object does not point to itself ", clonedObject == clonedObject.getLink());
	}
	
	private static class TestClass {
		private TestClass link;
		
		public TestClass() {}

		public TestClass getLink() {
			return link;
		}
		
		public void setLink(TestClass link) {
			this.link = link;
		}
	}
	
}
