package com.siddharth.plutocracy.security.configuration.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@ConfigurationProperties(prefix = "com.siddharth.plutocracy")
@Data
public class JwtConfiguration {

	private Configuration jwt = new Configuration();

	@Data
	public class Configuration {
		private String secretKey;
	}

}