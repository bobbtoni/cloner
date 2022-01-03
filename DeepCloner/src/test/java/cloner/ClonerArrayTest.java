package cloner;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class ClonerArrayTest {
	
	@Test
	public void arrayTest() throws IllegalAccessException {
		TestClass testObject = new TestClass(new int[] {1, 2});
		TestClass clonedObject = new Cloner().deepClone(testObject);
		
		assertFalse("Test object copied", testObject == clonedObject);
		
		assertArrayEquals(testObject.getArray(), clonedObject.getArray());
	}
	
	private static class TestClass {
		private int[] array;
		
		public TestClass() {}

		public TestClass(int[] array) {
			this.array = array;
		}

		public int[] getArray() {
			return array;
		}
	}
}
