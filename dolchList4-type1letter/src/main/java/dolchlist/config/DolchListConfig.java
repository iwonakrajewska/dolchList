package dolchlist.config;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dolchlist.dto.DolchListElement;

public class DolchListConfig {

	private static final Logger LOGGER = LoggerFactory.getLogger(DolchListConfig.class);
	private static final int MINIMUM_WORDS_COUNT = 3;
	private static final String SOUND_FILE_EXTENTION = ".mp3";
	private static final String PICTURE_FILE_EXTENTION = ".jpg";

	private final PropertyLoader propertyLoader;

	private final List<DolchListElement> dolchList = new ArrayList<>();

	public DolchListConfig(PropertyLoader propertyLoader) {
		this.propertyLoader = propertyLoader;
		buildDolchList();
		validateDolchList();
	}

	private void buildDolchList() {
		try (Stream<Path> walk = Files.walk(Paths.get(propertyLoader.getFilesFolder()), 1)) {
			List<String> result = walk.map(x -> x.toString()).filter(f -> f.endsWith(SOUND_FILE_EXTENTION)).collect(Collectors.toList());
			for (String absoluteFile : result) {
				String word = new File(absoluteFile).getName().replace(SOUND_FILE_EXTENTION, "");
				String pictureFilePath = absoluteFile.replace(SOUND_FILE_EXTENTION, PICTURE_FILE_EXTENTION);
				dolchList.add(new DolchListElement(word, absoluteFile, pictureFilePath));
			}
		} catch (IOException e) {
			LOGGER.error(e.getMessage(), e);
		}
	}

	private void validateDolchList() {
		if (dolchList.size() < MINIMUM_WORDS_COUNT) {
			LOGGER.error("Minimum {} words required, found {}", MINIMUM_WORDS_COUNT, dolchList.size());
			throw new IllegalArgumentException("Minimum words count not met");
		}
	}

	public List<DolchListElement> getDolchList() {
		return dolchList;
	}

}
