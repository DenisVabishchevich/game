package ae.cyberspeed.combination.impl;

import ae.cyberspeed.combination.WinningCombination;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@RequiredArgsConstructor
public class AreaWinningCombination implements WinningCombination {

    private final List<List<String>> combinations;
    private final Double multiplier;
    private final String name;
    private final String group;

    @Override
    public List<String> found(String[][] matrix) {
        List<String> result = new ArrayList<>();

        for (List<String> area : combinations) {
            Map<String, Long> areaSymbolCounts = new HashMap<>();
            for (String position : area) {
                int row = Integer.parseInt(position.split(":")[0]);
                int col = Integer.parseInt(position.split(":")[1]);
                String symbol = matrix[row][col];
                areaSymbolCounts.put(symbol, areaSymbolCounts.getOrDefault(symbol, 0L) + 1);
            }
            for (Map.Entry<String, Long> areaCountEntry : areaSymbolCounts.entrySet()) {
                if (areaCountEntry.getValue() == area.size()) {
                    result.add(areaCountEntry.getKey());
                }
            }
        }

        return result;
    }

}
