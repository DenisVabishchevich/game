package ae.cyberspeed.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class Config {
    private Integer columns = 3;
    private Integer rows = 3;
    private Map<String, Symbol> symbols;
    private Probabilities probabilities;
    @JsonProperty("win_combinations")
    private Map<String, WinningCombination> winCombinations;

    @Data
    public static class Symbol {
        @JsonProperty("reward_multiplier")
        private Double rewardMultiplier;
        private String type;
        private String impact;
        private Integer extra;
    }

    @Data
    public static class Probabilities {
        @JsonProperty("standard_symbols")
        private List<StandardSymbolProbability> standardSymbols;
        @JsonProperty("bonus_symbols")
        private BonusSymbols bonusSymbols;
    }

    @Data
    public static class StandardSymbolProbability {
        private Integer column;
        private Integer row;
        private Map<String, Integer> symbols;
    }

    @Data
    public static class BonusSymbols {
        private Map<String, Integer> symbols;
    }

    @Data
    public static class WinningCombination {
        @JsonProperty("reward_multiplier")
        private Double rewardMultiplier;
        private String when;
        private Integer count;
        private String group;
        @JsonProperty("covered_areas")
        private List<List<String>> coveredAreas;
    }

}