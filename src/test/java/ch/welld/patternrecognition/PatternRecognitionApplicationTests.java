package ch.welld.patternrecognition;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ch.welld.patternrecognition.controller.PatternRecognitionController;
import ch.welld.patternrecognition.model.Space;

@SpringBootTest
class PatternRecognitionApplicationTests {
	
	@Autowired
	private PatternRecognitionController patternRecognitionController;
	
	@Autowired
	private Space space;

	@Test
	public void contextLoads() {
		assertThat(patternRecognitionController).isNotNull();
	}
	
	@Test
	public void componentLoads() {
		assertThat(space).isNotNull();
	}

}
