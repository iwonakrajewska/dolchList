
package dolchlist.config;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import dolchlist.config.DolchListElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import dolchlist.Main;

public class DolchListConfig {
	private Logger logger = LoggerFactory.getLogger(Main.class);

	private List<DolchListElement> dolchList = new ArrayList<>();
	private Random randomGenerator = new Random();
	private Integer sizeX;
	private Integer sizeY;
	private String soundsFolder;
	private Integer iterations;

	public DolchListConfig() {
		readProperties();
		buildDolchList();
	}

	private void readProperties() {
		logger.info("loading properties");
		
		try (InputStream input = getClass().getClassLoader().getResourceAsStream("DolchList.properties")) {
			Properties prop = new Properties();
			if (input == null) {
				System.out.println("Sorry, unable to find DolchList.properties");
				return;
			}
			prop.load(input);
			sizeX = Integer.valueOf(prop.getProperty("scene.size.x"));
			sizeY = Integer.valueOf(prop.getProperty("scene.size.y"));
			iterations = Integer.valueOf(prop.getProperty("iterations.number"));
			soundsFolder = prop.getProperty("sound.files.folder");
		} catch (IOException ex) {
			ex.printStackTrace();
			logger.error(ex.getMessage(),ex);
		}
	}

	private void buildDolchList() {
		try (Stream<Path> walk = Files.walk(Paths.get(soundsFolder))) {
			List<String> result = walk.map(x -> x.toString()).filter(f -> f.endsWith(".mp3"))
					.collect(Collectors.toList());
			for (String absoluteFile : result) {
				String word = new File(absoluteFile).getName().replace(".mp3", "");
				dolchList.add(new DolchListElement(word, absoluteFile));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public DolchListElement pickRandomWord() {
		if (dolchList.isEmpty())
			return null;

		int index = randomGenerator.nextInt(dolchList.size());
		return dolchList.get(index);
	}

	public Integer getSizeX() {
		return sizeX;
	}

	public Integer getSizeY() {
		return sizeY;
	}

	public String getSoundsFolder() {
		return soundsFolder;
	}

	public Integer getIterations() {
		return iterations;
	}

}
