package algorithms;

import models.BnR;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public abstract class SearchBnR {
    protected BnR result;

    // Mỗi khi sinh trạng thái kề thì parent của nó chính là trạng thái gốc
    // -> truy vết ngược theo parent để tìm đường đi
    public String getResult() {
        var rsList = new ArrayList<>(List.of(result));
        var root = result;
        while (root != null) {
            root = root.getParent();
            rsList.add(root);
        }
        return rsList
                .stream()
                .filter(Objects::nonNull)
                .map(BnR::toString)
                .collect(Collectors.joining(" <- "));
    }

    public abstract void solve(BnR startState, BnR targetState);
}
