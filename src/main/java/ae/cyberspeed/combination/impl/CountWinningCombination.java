package ae.cyberspeed.combination.impl;

import ae.cyberspeed.combination.WinningCombination;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@RequiredArgsConstructor
public class CountWinningCombination implements WinningCombination {

    private final Integer count;
    private final Double multiplier;
    private final String name;
    private final String group;

    @Override
    public List<String> found(String[][] matrix) {
        Map<String, Integer> symbols = new HashMap<>();

        for (String[] row : matrix) {
            for (String elem : row) {
                symbols.compute(elem, (e, c) -> {
                    if (c == null) {
                        return 1;
                    } else {
                        return ++c;
                    }
                });
            }
        }

        return symbols.entrySet().stream()
            .filter(entry -> entry.getValue() >= getCount())
            .map(Map.Entry::getKey)
            .toList();
    }
}
