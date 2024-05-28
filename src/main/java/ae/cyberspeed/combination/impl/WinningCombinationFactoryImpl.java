package ae.cyberspeed.combination.impl;

import ae.cyberspeed.combination.WinningCombination;
import ae.cyberspeed.combination.WinningCombinationFactory;
import ae.cyberspeed.dto.Config;

import java.util.ArrayList;
import java.util.List;

public class WinningCombinationFactoryImpl implements WinningCombinationFactory {

    @Override
    public List<WinningCombination> buildCombinations(Config config) {
        List<WinningCombination> result = new ArrayList<>();
        for (var entry : config.getWinCombinations().entrySet()) {
            String wcName = entry.getKey();
            Config.WinningCombination combination = entry.getValue();

            if (combination.getCount() != null) {
                CountWinningCombination countWinningCombination = new CountWinningCombination(
                    combination.getCount(),
                    combination.getRewardMultiplier(),
                    wcName,
                    combination.getGroup());
                result.add(countWinningCombination);
            } else if (combination.getCoveredAreas() != null) {
                AreaWinningCombination areaWinningCombination = new AreaWinningCombination(
                    combination.getCoveredAreas(),
                    combination.getRewardMultiplier(),
                    wcName,
                    combination.getGroup());
                result.add(areaWinningCombination);
            }
        }
        return result;
    }
}
