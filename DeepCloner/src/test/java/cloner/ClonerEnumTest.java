package cloner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class ClonerEnumTest {
	
	@Test
	public void enumTest() throws IllegalAccessException {
		TestClass testObject = new TestClass(TestEnum.CBA);
		TestClass clonedObject = new Cloner().deepClone(testObject);
		
		assertFalse("Test object copied", testObject == clonedObject);
		
		assertEquals(testObject.getTestEnum(), clonedObject.getTestEnum());
	}
	
	private static class TestClass {
		
		private TestEnum testEnum;
		
		public TestClass() {}
		
		public TestClass(TestEnum testEnum) {
			this.testEnum = testEnum;
		}
		
		public TestEnum getTestEnum() {
			return testEnum;
		}
	}
	
	private enum TestEnum {
		ABC, CBA;
	}
}
