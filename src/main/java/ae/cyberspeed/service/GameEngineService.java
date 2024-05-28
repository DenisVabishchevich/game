package ae.cyberspeed.service;

import ae.cyberspeed.combination.WinningCombination;
import ae.cyberspeed.combination.WinningCombinationFactory;
import ae.cyberspeed.dto.Config;
import ae.cyberspeed.dto.GameResult;
import ae.cyberspeed.utils.MatrixGenerator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class GameEngineService {

    public static final String BONUS = "bonus";
    public static final String EXTRA_BONUS = "extra_bonus";
    public static final String MULTIPLY_REWARD = "multiply_reward";

    private final Config config;
    private final WinningCombinationFactory combinationFactory;

    public GameEngineService(Config config,
                             WinningCombinationFactory combinationFactory) {
        this.config = config;
        this.combinationFactory = combinationFactory;
    }

    public GameResult playGame(int betAmount) {
        String[][] matrix = MatrixGenerator.generateMatrix(config);
        return this.playGame(betAmount, matrix);
    }

    public GameResult playGame(int betAmount, String[][] matrix) {
        Map<String, List<String>> winningCombinations = findWinningCombinations(matrix, config);
        double reward = calculateReward(winningCombinations, betAmount);
        Map<String, Config.Symbol> bonus = findBonusSymbol(matrix);
        Double rewardWithBonus = applyBonusSymbol(bonus, reward);

        String bonusSymbol = bonus.keySet().stream().findAny().orElse("");
        return new GameResult(matrix, rewardWithBonus, winningCombinations, bonusSymbol);
    }

    private Map<String, List<String>> findWinningCombinations(String[][] matrix, Config config) {

        Map<String, Map<String, WinningCombination>> symbolToWc = new HashMap<>();

        List<WinningCombination> winningCombinations = combinationFactory.buildCombinations(config);
        for (WinningCombination winningCombination : winningCombinations) {

            List<String> found = winningCombination.found(matrix);
            for (String symbol : found) {

                Map<String, WinningCombination> wcMap = symbolToWc.get(symbol);

                if (wcMap == null) {
                    // add combination for missing group
                    Map<String, WinningCombination> wc = new HashMap<>();
                    wc.put(winningCombination.getGroup(), winningCombination);
                    symbolToWc.put(symbol, wc);
                } else {
                    // replace combination for the same group
                    wcMap.compute(winningCombination.getGroup(), (group, wc) -> {
                        if (wc == null) {
                            return winningCombination;
                        } else {
                            if (wc.getMultiplier() > winningCombination.getMultiplier()) {
                                return wc;
                            } else {
                                return winningCombination;
                            }
                        }
                    });
                }
            }
        }

        return symbolToWc.entrySet()
            .stream()
            .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().values().stream().map(WinningCombination::getName).toList()));
    }

    private double calculateReward(Map<String, List<String>> winningCombinations, int betAmount) {
        double reward = 0;

        for (Map.Entry<String, List<String>> entry : winningCombinations.entrySet()) {
            String symbol = entry.getKey();
            List<String> combinations = entry.getValue();
            double symbolReward = config.getSymbols().get(symbol).getRewardMultiplier() * betAmount;

            for (String combinationKey : combinations) {
                symbolReward *= config.getWinCombinations().get(combinationKey).getRewardMultiplier();
            }
            reward += symbolReward;
        }

        return reward;
    }

    private Double applyBonusSymbol(Map<String, Config.Symbol> bonus, double reward) {

        Optional<Config.Symbol> bonusOpt = bonus.values().stream().findFirst();

        if (bonusOpt.isEmpty()) {
            return reward;
        }

        if (MULTIPLY_REWARD.equals(bonusOpt.get().getImpact())) {
            reward *= bonusOpt.get().getRewardMultiplier();
        } else if (EXTRA_BONUS.equals(bonusOpt.get().getImpact())) {
            reward += bonusOpt.get().getExtra();
        }
        return reward;

    }

    private Map<String, Config.Symbol> findBonusSymbol(String[][] matrix) {

        for (String[] strings : matrix) {
            for (String symbol : strings) {
                Config.Symbol symbolConfig = config.getSymbols().get(symbol);

                if (BONUS.equals(symbolConfig.getType())) {
                    return Map.of(symbol, symbolConfig);
                }
            }
        }

        return Map.of();
    }

}
