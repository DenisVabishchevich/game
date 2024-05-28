package ae.cyberspeed.combination;

import ae.cyberspeed.dto.Config;

import java.util.List;

public interface WinningCombinationFactory {

    List<WinningCombination> buildCombinations(Config config);

}
