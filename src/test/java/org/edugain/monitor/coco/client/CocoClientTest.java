package org.edugain.monitor.coco.client;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import org.edugain.monitor.coco_client.model.CocoResult;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ContextConfiguration(classes = { TestConfig.class })
public class CocoClientTest extends BaseTest {

	@Autowired
	private CocoClient cocoClient;

	@Before
	public void init() {
		this.addUniTestInterceptor(cocoClient.getRestTemplate());
	}

	@Test
	public void testGetAllSps() throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException {

		List<CocoResult> results = cocoClient.getCocoApi().getStatus(true, null, null);

		assertThat(results.size()).isGreaterThan(2000);
	}

}
