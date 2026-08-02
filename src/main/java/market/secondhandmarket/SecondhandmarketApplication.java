package market.secondhandmarket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import market.secondhandmarket.upload.StorageProperties;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
public class SecondhandmarketApplication {

	public static void main(String[] args) {
		SpringApplication.run(SecondhandmarketApplication.class, args);
	}

	
}
