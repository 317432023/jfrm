package com.soaringloong.jfrm.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(
		scanBasePackages = { "${jfrm.info.base-package}.server", "${jfrm.info.base-package}.module", "com.cmpt.oss" })
public class JFrmApplication {

	public static void main(String[] args) {
		SpringApplication.run(JFrmApplication.class, args);
	}

}
