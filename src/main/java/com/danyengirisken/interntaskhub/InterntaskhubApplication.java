package com.danyengirisken.interntaskhub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Uygulama giris noktasi.
 *
 * Carbon konvansiyonu: aktif profile gore hem uygulama hem de veritabani
 * property dosyalari {@code @PropertySources} ile yuklenir:
 *   - application-<profil>.properties
 *   - database-<profil>.properties
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.danyengirisken.interntaskhub"})
@EntityScan(basePackages = {"com.danyengirisken.interntaskhub.entity"})
@EnableJpaRepositories(basePackages = {"com.danyengirisken.interntaskhub.repository"})
@PropertySources({
        @PropertySource("classpath:application-${spring.profiles.active}.properties"),
        @PropertySource("classpath:database-${spring.profiles.active}.properties")
})
public class InterntaskhubApplication {

    public static void main(String[] args) {
        SpringApplication.run(InterntaskhubApplication.class, args);
    }
}
