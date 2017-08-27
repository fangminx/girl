package com.fangminx;

import com.fangminx.service.GirlService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest //启动整个工程
public class GirlApplicationTests {

	@Autowired
	private GirlService girlService;

	@Test
	public void contextLoads() {
		Assert.assertEquals("111","111");
	}

}
