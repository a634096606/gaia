package org.apel.gaia.persist.test;

import javax.sql.DataSource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath*:META-INF/spring/module-*.xml")
public class PersistTest {

	@Autowired
	private DataSource ds;
	
	@Test
	public void test1() throws Exception{
		System.out.println(ds);
	}
	
}
