package be.vub.Linking;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesLoader {

    Properties prop;

    public PropertiesLoader() {
        try (InputStream input = PropertiesLoader.class.getClassLoader().getResourceAsStream("config.properties")) {
            prop = new Properties();
            if (input == null) {
                System.out.println("Sorry, unable to find config.properties");
                return;
            }
            prop.load(input);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
    provenance.endpoint = http://localhost:3030/inferred
    elements.endpoint = http://localhost:8089/parliament/sparql
    users.filename = usersData.txt
    */
    public String getProp(String propertyName) {
        return prop.getProperty(propertyName);
    }



}
