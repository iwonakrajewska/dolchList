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

    private static final Logger logger = LoggerFactory.getLogger(PropertyLoader.class);

    private Integer sizeX;
    private Integer sizeY;
    private String soundsFolder;
    private Integer iterations;

    public PropertyLoader() {
        readProperties();
    }

    private void readProperties() {
        logger.info("Loading properties");

        try (InputStream input = getClass().getClassLoader().getResourceAsStream("application.properties")) {
            Properties prop = new Properties();
            if (input == null) {
                logger.error("Unable to find DolchList.properties");
                return;
            }
            prop.load(input);
            sizeX = Integer.valueOf(prop.getProperty("scene.size.x"));
            sizeY = Integer.valueOf(prop.getProperty("scene.size.y"));
            iterations = Integer.valueOf(prop.getProperty("iterations.number"));
            soundsFolder = prop.getProperty("sound.files.folder");

        } catch (IOException ex) {
            logger.error(ex.getMessage(), ex);
        }
    }

}
