package com.alexie.demo;


import com.alexie.demo.MaterialTests.materialSearchTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.SuiteDisplayName;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;
import org.springframework.test.context.junit.jupiter.SpringExtension;


@Slf4j
//@ContextConfiguration()
@ExtendWith(SpringExtension.class)
@Component
@SpringBootTest(classes = DemoApplication.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@RunWith(JUnitPlatform.class)
//@SelectPackages({"com.alexie.demo.testMaterialSearch"})
@SelectClasses({
		materialSearchTest.class
})
@SuiteDisplayName("特赞中台接口测试入口～～～")
class DemoApplicationTests extends ENV_PREP{

	private static final Logger logger = LoggerFactory.getLogger(DemoApplicationTests.class);

	void contextLoads() {
		logger.info("###########################");
	}

//	@Autowired
//	Config config;

//	@Autowired
//	ENV_PREP sss;

//	@Test
//	public void test(){
//		System.out.println("========");
////		sss.setUp();
//	}

}
