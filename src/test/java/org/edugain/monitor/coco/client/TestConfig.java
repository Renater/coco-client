package org.edugain.monitor.coco.client;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

@SpringBootConfiguration
@EnableAutoConfiguration

@ComponentScan(basePackages = { "org.edugain.monitor.coco.client" }, excludeFilters = {

		@ComponentScan.Filter(type = FilterType.REGEX, pattern = "fr.renater.federation.clients.guichet.api.*") })
@TestConfiguration
public class TestConfig {

}