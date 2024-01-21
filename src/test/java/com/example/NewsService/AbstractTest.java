package com.example.NewsService;

import com.example.NewsService.mapper.UserMapper;
import com.example.NewsService.model.Role;
import com.example.NewsService.model.RoleType;
import com.example.NewsService.model.User;
import com.example.NewsService.repository.UserRepository;
import com.example.NewsService.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.Collections;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Sql("classpath:db/init.sql")
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
@Testcontainers
public class AbstractTest {

    protected static PostgreSQLContainer postgreSQLContainer;

    static {
        DockerImageName postgres = DockerImageName.parse("postgres:12:3");

        postgreSQLContainer = (PostgreSQLContainer) new PostgreSQLContainer(postgres).withReuse(true);

        postgreSQLContainer.start();
    }

    @DynamicPropertySource
    public static void registerProperties(DynamicPropertyRegistry registry) {
        String jdbcUrl = postgreSQLContainer.getJdbcUrl();

        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
        registry.add("spring.datasource.url", () -> jdbcUrl);
    }

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected WebApplicationContext context;

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
//        userService.create(User.builder()
//                .name("USER")
//                .password("12345")
//                .roles(Collections.singletonList(Role.from(RoleType.ROLE_USER)))
//                .build());
//        userService.create(User.builder()
//                .name("ADMIN")
//                .password("12345")
//                .roles(Collections.singletonList(Role.from(RoleType.ROLE_ADMIN)))
//                .build());
    }

    @AfterEach
    public void afterEach(){
//        userRepository.deleteByName("USER");
//        userRepository.deleteByName("ADMIN");
    }
}
