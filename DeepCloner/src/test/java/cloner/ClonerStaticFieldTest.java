package cloner;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ClonerStaticFieldTest {
	@Test
	public void staticFieldTest() throws IllegalAccessException {
		TestClass testObject = new TestClass();
		Integer previewIntegerValue = testObject.staticInteger;
		String previewStringValue = testObject.getStaticString();
		TestClass clonedObject = new Cloner().deepClone(testObject);
		
		assertFalse("Test object copied", testObject == clonedObject);
		
		assertTrue("Static field was changed", previewIntegerValue == clonedObject.staticInteger);
		assertTrue("Static field was changed", previewStringValue == clonedObject.getStaticString());
	}
	
	private static class TestClass {
		public static Integer staticInteger = 100000;
		private static String staticString = "Hello";
		
		public TestClass() {}
		
		public String getStaticString() {
			return staticString;
		}
	}
}
