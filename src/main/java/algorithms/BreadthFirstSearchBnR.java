package algorithms;

import models.BnR;

import java.util.*;
import java.util.stream.Collectors;

public class BreadthFirstSearchBnR {
    protected BnR result;

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

    public void solve(BnR startState, BnR targetState) {
        Queue<BnR> L = new LinkedList<>();
        Map<String, Boolean> Q = new HashMap<>();
        L.add(startState);
        while (true) {
            var currentState = L.poll();
            assert currentState != null;
            if (currentState.equals(targetState)) {
                result = currentState;
                return;
            }
            var distributions = currentState.getDistributions()
                    .stream()
                    .filter(bnr -> !Q.containsKey(bnr.toString()))
                    .peek(bnr -> Q.put(bnr.toString(), true))
                    .collect(Collectors.toList());
            L.addAll(distributions);
            Q.put(currentState.toString(), true);
        }
    }

    public static void main(String[] args) {
        var algorithm = new BreadthFirstSearchBnR();
        algorithm.solve(new BnR(3, 3, 1), new BnR(0, 0, 0));
        System.out.println(algorithm.getResult());
    }
}
