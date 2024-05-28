package ae.cyberspeed.combination;

import java.util.List;

public interface WinningCombination {

    String getName();

    String getGroup();

    Double getMultiplier();

    List<String> found(String[][] matrix);
}
