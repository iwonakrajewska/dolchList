/*
 * Copyright (c) 2020 Mastercard. All rights reserved.
 */

package dolchlist.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Getter
public class PropertyLoader {

    private static final Logger LOGGER = LoggerFactory.getLogger(PropertyLoader.class);

    private Integer sizeX;
    private Integer sizeY;
    private String filesFolder;
    private Integer iterations;

    public PropertyLoader() {
        readProperties();
    }

    private void readProperties() {
        LOGGER.info("Loading properties");

        try (InputStream input = getClass().getClassLoader().getResourceAsStream("application.properties")) {
            Properties prop = new Properties();
            if (input == null) {
                LOGGER.error("Unable to find application.properties");
                throw new IllegalArgumentException("Unable to find application.properties");
            }
            prop.load(input);
            sizeX = Integer.valueOf(prop.getProperty("scene.size.x"));
            sizeY = Integer.valueOf(prop.getProperty("scene.size.y"));
            iterations = Integer.valueOf(prop.getProperty("iterations.number"));
            filesFolder = prop.getProperty("sound.files.folder");

        } catch (IOException ex) {
            LOGGER.error(ex.getMessage(), ex);
            throw new IllegalArgumentException(ex);
        }
    }

}
