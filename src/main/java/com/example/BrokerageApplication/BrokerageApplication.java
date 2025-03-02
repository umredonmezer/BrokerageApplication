package com.example.BrokerageApplication;

import com.example.BrokerageApplication.model.Asset;
import com.example.BrokerageApplication.model.Customer;
import com.example.BrokerageApplication.model.Role;
import com.example.BrokerageApplication.repository.AssetRepository;
import com.example.BrokerageApplication.repository.CustomerRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@SpringBootApplication
public class BrokerageApplication {
	public static void main(String[] args) {
		SpringApplication.run(BrokerageApplication.class, args);
	}

	@Bean
	CommandLineRunner initDatabase(CustomerRepository customerRepository,
								   AssetRepository assetRepository,
								   PasswordEncoder passwordEncoder) {
		return args -> {

			if (customerRepository.findByUsername("admin").isEmpty()) {
				Customer admin = new Customer();
				admin.setUsername("admin");
				admin.setPassword(passwordEncoder.encode("admin123"));
				admin.setFullName("Admin User");
				admin.setRole(Role.ADMIN);
				customerRepository.save(admin);
			}

			if (customerRepository.findByUsername("customer123").isEmpty()) {
				Customer customer1 = new Customer();
				customer1.setUsername("customer123");
				customer1.setPassword(passwordEncoder.encode("password123"));
				customer1.setFullName("Customer 123");
				customer1.setRole(Role.CUSTOMER);
				customerRepository.save(customer1);

				assetRepository.saveAll(List.of(
						new Asset(null, customer1.getId(), "TRY", 10000, 10000),
						new Asset(null, customer1.getId(), "AAPL", 50, 50)
				));
			}

			if (customerRepository.findByUsername("customer12345").isEmpty()) {
				Customer customer2 = new Customer();
				customer2.setUsername("customer12345");
				customer2.setPassword(passwordEncoder.encode("password45"));
				customer2.setFullName("Customer 12345");
				customer2.setRole(Role.CUSTOMER);
				customerRepository.save(customer2);

				assetRepository.saveAll(List.of(
						new Asset(null, customer2.getId(), "TRY", 15000, 15000),
						new Asset(null, customer2.getId(), "TSLA", 30, 30)
				));
			}
		};
	}
}
