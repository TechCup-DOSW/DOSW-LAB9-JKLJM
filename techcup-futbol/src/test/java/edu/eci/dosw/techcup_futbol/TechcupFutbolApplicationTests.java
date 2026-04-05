package edu.eci.dosw.techcup_futbol;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import static org.mockito.Mockito.mock;

import edu.eci.dosw.techcup_futbol.repository.UserRepository;
import edu.eci.dosw.techcup_futbol.repository.TournamentRepository;

@SpringBootTest(properties = {
		"spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration,"
				+ "org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration",
		"spring.data.jpa.repositories.enabled=false"
})
class TechcupFutbolApplicationTests {

	@TestConfiguration
	static class TestConfig {
		@Bean
		UserRepository userRepository() {
			return mock(UserRepository.class);
		}

		@Bean
		TournamentRepository tournamentRepository() {
			return mock(TournamentRepository.class);
		}
	}

	@Test
	void contextLoads() {
	}

}
