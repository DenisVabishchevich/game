package ae.cyberspeed;

import ae.cyberspeed.combination.impl.WinningCombinationFactoryImpl;
import ae.cyberspeed.dto.Config;
import ae.cyberspeed.dto.GameResult;
import ae.cyberspeed.service.GameEngineService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.io.File;

@Slf4j
public class Application {
    public static void main(String[] args)
        throws Exception {
        if (args.length != 4 || !args[0].equals("--config") || !args[2].equals("--betting-amount")) {
            log.error("Usage: java -jar <your-jar-file> --config config.json --betting-amount <amount>");
            return;
        }

        String configFilePath = args[1];
        int bettingAmount = Integer.parseInt(args[3]);

        ObjectMapper mapper = new ObjectMapper();
        Config config = mapper.readValue(new File(configFilePath), Config.class);

        GameEngineService gameEngineService = new GameEngineService(config, new WinningCombinationFactoryImpl());
        GameResult result = gameEngineService.playGame(bettingAmount);

        log.info(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(result));
    }
}
