package ae.cyberspeed.utils;

import ae.cyberspeed.dto.Config;
import lombok.experimental.UtilityClass;

import java.security.SecureRandom;
import java.util.List;
import java.util.Map;

@UtilityClass
public class MatrixGenerator {

    public static final String BONUS = "bonus";
    public static final String STANDARD = "standard";

    public String[][] generateMatrix(Config config) {
        SecureRandom random = new SecureRandom();

        int rows = config.getRows();
        int columns = config.getColumns();
        String[][] matrix = new String[rows][columns];

        // Fill matrix with standard symbols based on probabilities
        for (Config.StandardSymbolProbability prob : config.getProbabilities().getStandardSymbols()) {
            String symbol = getRandomSymbol(prob.getSymbols(), random);
            matrix[prob.getRow()][prob.getColumn()] = symbol;
        }

        // Fill remaining cells with random standard symbols
        List<String> standardSymbols = config.getSymbols().entrySet().stream()
            .filter(e -> STANDARD.equals(e.getValue().getType()))
            .map(Map.Entry::getKey)
            .toList();

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (matrix[i][j] == null) {
                    matrix[i][j] = standardSymbols.get(random.nextInt(standardSymbols.size()));
                }
            }
        }

        // Randomly place a bonus symbol
        List<String> bonusSymbols = config.getSymbols().entrySet().stream()
            .filter(e -> BONUS.equals(e.getValue().getType()))
            .map(Map.Entry::getKey)
            .toList();

        int bonusRow = random.nextInt(rows);
        int bonusCol = random.nextInt(columns);
        matrix[bonusRow][bonusCol] = bonusSymbols.get(random.nextInt(bonusSymbols.size()));

        return matrix;
    }

    public String getRandomSymbol(Map<String, Integer> symbolProbabilities,
                                  SecureRandom random) {
        int sum = symbolProbabilities.values().stream().mapToInt(Integer::intValue).sum();
        int randomValue = random.nextInt(sum);
        int cumulativeSum = 0;
        for (Map.Entry<String, Integer> entry : symbolProbabilities.entrySet()) {
            cumulativeSum += entry.getValue();
            if (randomValue < cumulativeSum) {
                return entry.getKey();
            }
        }
        throw new IllegalStateException("Should never reach here");
    }
}
