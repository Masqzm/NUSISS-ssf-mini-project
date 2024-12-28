package ssf.miniproject;

import ssf.miniproject.config.Constants;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import jakarta.json.Json;
import jakarta.json.JsonReader;

@Component
public class AppBootstrap implements CommandLineRunner {
    @Override
    public void run(String... args) {
        try {
            String line;
            StringBuilder sb = new StringBuilder();

            // Load file from classpath
            try(InputStreamReader is = new InputStreamReader(new ClassPathResource("data/fieldmask.txt").getInputStream());
                BufferedReader br = new BufferedReader(is)) {

                while ((line = br.readLine()) != null) 
                    sb.append(line.trim());
            }
            // Read and store fields text into Constants
            Constants.GOOGLE_PLACES_TEXTSEARCH_FIELDS = sb.toString();


            // Load file from classpath
            InputStream inputStream = new ClassPathResource("data/locationRestriction.json").getInputStream();

            // Read and store JSON object into Constants
            JsonReader reader = Json.createReader(inputStream);
            Constants.GOOGLE_PLACES_TEXTSEARCH_LOC_JSON = reader.readObject();


            // Load file from classpath
            try(InputStreamReader is = new InputStreamReader(new ClassPathResource("data/topics.txt").getInputStream());
                BufferedReader br = new BufferedReader(is)) {
                // Reset SB
                sb.setLength(0);

                while ((line = br.readLine()) != null) {
                    sb.append(line.trim() + ",");
                }
                // Remove last comma
                sb.setLength(sb.length() - 1);

                Constants.JIO_TOPICS_LIST = Arrays.asList(sb.toString().split(","));
            }
        } catch (IOException ex) {
            System.err.println("Error loading files!");
            ex.printStackTrace();
        }
    }
}
