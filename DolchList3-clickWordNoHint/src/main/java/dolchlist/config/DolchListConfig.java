package dolchlist.config;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import dolchlist.dto.DolchListElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DolchListConfig {

    private static final Logger logger = LoggerFactory.getLogger(DolchListConfig.class);
    private static final int MINIMUM_WORDS_COUNT = 3;

    private final PropertyLoader propertyLoader;
    private Random randomGenerator = new Random();

    private final List<DolchListElement> dolchList = new ArrayList<>();


    public DolchListConfig(PropertyLoader propertyLoader) {
        this.propertyLoader = propertyLoader;
        buildDolchList();
        validateDolchList();
    }


    private void buildDolchList() {
        try (Stream<Path> walk = Files.walk(Paths.get(propertyLoader.getSoundsFolder()), 1)) {
            List<String> result = walk.map(x -> x.toString()).filter(f -> f.endsWith(".mp3")).collect(Collectors.toList());
            for (String absoluteFile : result) {
                String word = new File(absoluteFile).getName().replace(".mp3", "");
                dolchList.add(new DolchListElement(word, absoluteFile));
            }
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

    private void validateDolchList() {
        if (dolchList.size() < MINIMUM_WORDS_COUNT) {
            logger.error("Minimum {} words required, found {}", MINIMUM_WORDS_COUNT, dolchList.size());
            throw new IllegalArgumentException("Minimum words count not met");
        }
    }

    public DolchListElement pickRandomWord() {
        if (dolchList.isEmpty()) {
            return null;
        }

        int index = randomGenerator.nextInt(dolchList.size());
        return dolchList.get(index);
    }


    public List<DolchListElement> pick3Words() {
        if (dolchList.size() < MINIMUM_WORDS_COUNT) {
            logger.error("Minimum 3 words required in configuration");
            throw new RuntimeException("Not enough words in configuration");
        }

        List<DolchListElement> list = new ArrayList();
        DolchListElement element1 = pickRandomWord();
        list.add(element1);
        dolchList.remove(element1);
        DolchListElement element2 = pickRandomWord();
        list.add(element2);
        dolchList.remove(element2);
        DolchListElement element3 = pickRandomWord();
        list.add(element3);
        dolchList.remove(element3);
        return list;
    }

}
