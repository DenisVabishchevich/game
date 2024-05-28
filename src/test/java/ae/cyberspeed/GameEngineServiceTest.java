package ae.cyberspeed;


import ae.cyberspeed.combination.impl.WinningCombinationFactoryImpl;
import ae.cyberspeed.dto.Config;
import ae.cyberspeed.dto.GameResult;
import ae.cyberspeed.service.GameEngineService;
import com.fasterxml.jackson.core.StreamReadFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Slf4j
class GameEngineServiceTest {

    @Test
    void testPlayGameRandomMatrix() throws IOException {
        String configStr = new ClassPathResource("config.json").getContentAsString(StandardCharsets.UTF_8);

        ObjectMapper mapper = JsonMapper.builder()
            .enable(StreamReadFeature.INCLUDE_SOURCE_IN_LOCATION)
            .disable(StreamReadFeature.AUTO_CLOSE_SOURCE)
            .build();

        Config config = mapper.readValue(configStr, Config.class);
        int betAmount = 100;
        GameEngineService gameEngineService = new GameEngineService(config, new WinningCombinationFactoryImpl());

        GameResult result = gameEngineService.playGame(betAmount);

        log.info(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(result));

        assertNotNull(result.getMatrix());
        assertNotNull(result.getReward());
    }

    @ParameterizedTest
    @MethodSource("factory")
    void testPlayGame(String[][] matrix, Double reward) throws IOException {
        String configStr = new ClassPathResource("config.json").getContentAsString(StandardCharsets.UTF_8);

        ObjectMapper mapper = JsonMapper.builder()
            .enable(StreamReadFeature.INCLUDE_SOURCE_IN_LOCATION)
            .disable(StreamReadFeature.AUTO_CLOSE_SOURCE)
            .build();

        Config config = mapper.readValue(configStr, Config.class);
        int betAmount = 100;
        GameEngineService gameEngineService = new GameEngineService(config, new WinningCombinationFactoryImpl());

        GameResult result = gameEngineService.playGame(betAmount, matrix);

        log.info(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(result));

        assertNotNull(result.getMatrix());
        assertEquals(result.getReward(), reward);
    }

    public static Stream<Arguments> factory() {
        return Stream.of(
            Arguments.of(
                new String[][]{
                    new String[]{"A", "B", "C"},
                    new String[]{"E", "B", "5x"},
                    new String[]{"F", "D", "C"}},
                0d
            ),
            Arguments.of(
                new String[][]{
                    new String[]{"A", "B", "C"},
                    new String[]{"E", "B", "10x"},
                    new String[]{"F", "D", "B"}},
                25000d
            ),
            Arguments.of(
                new String[][]{
                    new String[]{"B", "A", "F"},
                    new String[]{"C", "C", "A"},
                    new String[]{"C", "D", "10x"}},
                10000d
            )
        );
    }
}


