package TestLayer;

import org.testng.annotations.Test;

public class JenkinsTest {
	
	@Test (groups="J")
	public void m1() {
		System.out.println("m1");
	}
	
	@Test (groups="J")
	public void m2() {
		System.out.println("m2");
	}

}
