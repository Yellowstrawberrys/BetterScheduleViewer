package me.yellowstrawberry.ystschoolhelper;

import org.json.JSONObject;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.*;
import java.util.stream.Collectors;

@SpringBootApplication
public class YstSchoolHelperApplication {

    public static String apiKey1;

    public static void main(String[] args) throws FileNotFoundException {
        apiKey1 = new JSONObject(new BufferedReader(new InputStreamReader(new FileInputStream("./keys.json"))).lines().collect(Collectors.joining())).getString("key1");
        SpringApplication.run(YstSchoolHelperApplication.class, args);
    }

}
