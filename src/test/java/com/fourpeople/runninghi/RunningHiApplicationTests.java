package com.fourpeople.runninghi;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.util.Assert;

import java.security.interfaces.RSAPublicKey;
import java.util.List;

@SpringBootTest
class RunningHiApplicationTests {
	@Test
	void contextLoads() {
	}
}
