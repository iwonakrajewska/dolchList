package dolchlist.dto;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dolchlist.config.DolchListConfig;
import lombok.Data;

@Data
public class Word {

	private static final Logger LOGGER = LoggerFactory.getLogger(Word.class);
	private Random randomGenerator = new Random();

	private DolchListElement wordToType;
	private int letterIndex;
	private String letterText;

	public void assignWord(DolchListConfig dolchListConfig) {
		this.wordToType = pickRandomWord(dolchListConfig);
		pickRandomLetter();
	}

	private DolchListElement pickRandomWord(DolchListConfig dolchListConfig) {
		if (dolchListConfig.getDolchList().isEmpty()) {
			return null;
		}
		int index = randomGenerator.nextInt(dolchListConfig.getDolchList().size());
		return dolchListConfig.getDolchList().get(index);
	}

	private void pickRandomLetter() {
		int wordLength = wordToType.getWord().length();
		if (wordLength < 1) {
			LOGGER.debug("Word is too short: {}", wordLength);
			throw new IllegalArgumentException("Minimum word length not met");
		}

		this.letterIndex = randomGenerator.nextInt(wordLength);
		this.letterText = "" + wordToType.getWord().charAt(this.letterIndex);
		LOGGER.info("Type letter '{}' at index {} for word: {}", new Object[] { letterText, letterIndex, wordToType.getWord() });
	}

}
