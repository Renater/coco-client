# Coco client

A Java Spring-Boot client to consume eduGAIN Coco check

## Usage

Add dependency in your pom.xml:

```xml
	<dependency>
		<groupId>org.edugain.monitor</groupId>
		<artifactId>coco-client</artifactId>
		<version>1.0.0-SNAPSHOT</version>
	</dependency>
```

Add configuration in `application.yml`:

```yml
application:
  clients:
    coco:
      skipCertificate: false
      basePath: https://monitor.edugain.org/coco/json.php
      connectTimeout: 5000
      readTimeout: 30000
```

In your code:
  * In you App declaration:
  
```java
@SpringBootApplication
@EnableConfigurationProperties({ CocoClient.class })
public class MyApp {
```

  * And "voilà", just inject the client in your code:
  
```java
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MyApp.class)
public class CocoClientTest extends Assertions {

	@Autowired
	private CocoClient cocoClient;

	@Test
	public void testGetOneSp() {

		List<CocoResult> results = cocoClient.getCocoApi().getStatus(false, "https://rendez-vous.renater.fr/shibboleth",
				null);
		assertThat(results.size()).isEqualTo(1);
		assertThat(results.get(0).getIdStatus()).isEqualTo("2");
	}
}
```
