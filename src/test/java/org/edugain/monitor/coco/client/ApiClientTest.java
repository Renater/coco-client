package org.edugain.monitor.coco.client;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.SSLContext;

import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.ssl.SSLContexts;
import org.edugain.monitor.coco_client.ApiClient;
import org.edugain.monitor.coco_client.api.CocoApi;
import org.edugain.monitor.coco_client.model.CocoResult;
import org.junit.Test;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.MediaType;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

/**
 * Simple tests
 * 
 * @author arnoud
 *
 */
public class ApiClientTest extends BaseTest {

	@Test
	public void testGetOneSp() throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException {
		ApiClient apiClient = new ApiClient(getRestTemplate());
		CocoApi api = new CocoApi(apiClient.setBasePath("https://monitor.edugain.org/coco/json.php"));
		List<CocoResult> results = api.getStatus(false, "https://rendez-vous.renater.fr/shibboleth", null);

		assertThat(results.size()).isEqualTo(1);
		assertThat(results.get(0).getIdStatus()).isEqualTo("2");
	}

	private RestTemplate getRestTemplate() throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException {
		RestTemplateBuilder builder = new RestTemplateBuilder();
		HttpComponentsClientHttpRequestFactory reqFactory;
		HttpClientBuilder hcb = HttpClientBuilder.create().useSystemProperties();

		SSLContext sslContext;

		TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;
		sslContext = SSLContexts.custom().loadTrustMaterial(null, acceptingTrustStrategy).build();

		SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);
		hcb.setSSLSocketFactory(csf);

		reqFactory = new HttpComponentsClientHttpRequestFactory(hcb.build());
		reqFactory.setConnectTimeout(5000);
		reqFactory.setReadTimeout(30000);

		// Buffered Client in order to use interceptor logger

		RestTemplate rt = builder.requestFactory(() -> new BufferingClientHttpRequestFactory(reqFactory)).build();

		for (HttpMessageConverter converter : rt.getMessageConverters()) {
			if (converter instanceof AbstractJackson2HttpMessageConverter) {
				// Workaround text/html media type
				List<MediaType> supportedMediaTypes = new ArrayList<>();
				supportedMediaTypes.addAll(((AbstractJackson2HttpMessageConverter) converter).getSupportedMediaTypes());
				supportedMediaTypes.add(MediaType.TEXT_HTML);
				((AbstractJackson2HttpMessageConverter) converter).setSupportedMediaTypes(supportedMediaTypes);
			}
//			if (converter instanceof AbstractJackson2HttpMessageConverter) {
//				ObjectMapper mapper = ((AbstractJackson2HttpMessageConverter) converter).getObjectMapper();
//				ThreeTenModule module = new ThreeTenModule();
//				module.addDeserializer(Instant.class, CustomInstantDeserializer.INSTANT);
//				module.addDeserializer(OffsetDateTime.class, CustomInstantDeserializer.OFFSET_DATE_TIME);
//				module.addDeserializer(ZonedDateTime.class, CustomInstantDeserializer.ZONED_DATE_TIME);
//				mapper.registerModule(module);
//			}
		}

		this.addUniTestInterceptor(rt);

		return rt;

	}
}
