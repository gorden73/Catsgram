package ru.yandex.practicum.catsgram.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class HackCatService {

    private Logger log = LoggerFactory.getLogger(getClass());

    public static final String JDBC_URL = "jdbc:postgresql://127.0.0.1:5432/cats";
    public static final String JDBC_USERNAME = "kitty";
    public static final String JDBC_DRIVER = "org.postgresql.Driver";

    public void tryPassword(String jdbcPassword) {
        DriverManagerDataSource dataSourceConst = new DriverManagerDataSource();
        dataSourceConst.setDriverClassName(JDBC_DRIVER);
        dataSourceConst.setUrl(JDBC_URL);
        dataSourceConst.setUsername(JDBC_USERNAME);
        dataSourceConst.setPassword(jdbcPassword);
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSourceConst);
        jdbcTemplate.execute("SELECT 1;");
    }

    public String doHackNow() {
        List<String> catWordList = Arrays.asList("meow", "purr", "shhh", "zzz", "purrrrrr");
        for (String pass : catWordList) {
            try {
                tryPassword(pass);
                return "Такой пароль подходит " + pass;
            } catch (Exception e) {
                log.info("Такой пароль не подходит: {}", pass);
            }
        }
        return "В списке нет подходящего пароля";
    }
}
