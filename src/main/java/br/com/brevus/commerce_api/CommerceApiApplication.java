package br.com.brevus.commerce_api;

import br.com.brevus.commerce_api.efi.pix.PixConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableSpringDataWebSupport(pageSerializationMode = EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO)
@EnableConfigurationProperties(PixConfig.class)
@EnableScheduling
public class CommerceApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(CommerceApiApplication.class, args);
	}

}
