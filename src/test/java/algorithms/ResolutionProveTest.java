package algorithms;

import java.util.List;

public class ResolutionProveTest {
    public static void main(String[] args) {
        var algorithm = new ResolutionProve();
        var clauses = List.of(
                "!PvQ",
                "PvR",
                "!Q",
                "!RvS"
        );
        algorithm.propositionProve("S", clauses);
        algorithm.print();

        clauses = List.of(
                "!EvF",
                "!Av!BvC",
                "!BvD",
                "!Cv!DvE",
                "A",
                "B"
        );
        algorithm.propositionProve("F", clauses);
        algorithm.print();
    }
}
