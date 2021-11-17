package dolchlist.dto;

import java.util.Random;

import dolchlist.config.DolchListConfig;
import javafx.scene.control.TextField;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Data
public class Word {

    private static final Logger LOGGER = LoggerFactory.getLogger(Word.class);
    private Random randomGenerator = new Random();

    private DolchListElement wordToType;
    private boolean isExitEnabled = false;
    private TextField fieldWithFocus;

    public void assignWord(DolchListConfig dolchListConfig) {
        this.wordToType = pickRandomWord(dolchListConfig);
    }

    private DolchListElement pickRandomWord(DolchListConfig dolchListConfig) {
        if (dolchListConfig.getDolchList().isEmpty()) {
            return null;
        }
        int index = randomGenerator.nextInt(dolchListConfig.getDolchList().size());
        return dolchListConfig.getDolchList().get(index);
    }

}
