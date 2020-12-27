package com.example.timeserver;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

@SpringBootTest
class TimeserverApplicationTests {

	@Test
	void contextLoads() {

		String x = "x";
		String y = "y";

		String s = "";

		s += s.format("Something %s something %s", x,y);

		System.out.println(s);


	}



}
