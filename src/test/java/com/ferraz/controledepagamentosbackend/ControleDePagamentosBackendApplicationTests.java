package com.ferraz.controledepagamentosbackend;

import com.ferraz.controledepagamentosbackend.domain.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ControleDePagamentosBackendApplicationTests {

	@Autowired
	private UserRepository userRepository;

	@Test
	void contextLoads() {
		assertThat(userRepository).isNotNull();
	}

}
