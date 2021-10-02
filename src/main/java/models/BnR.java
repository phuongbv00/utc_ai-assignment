package models;

import lombok.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BnR {
    private Integer billionaires = 3;
    private Integer robbers = 3;
    private Integer shipState = 1;
    private BnR parent;

    public BnR(Integer billionaires, Integer robbers, Integer shipState) {
        this.billionaires = billionaires;
        this.robbers = robbers;
        this.shipState = shipState;
    }

    private BnR getReverse() {
        return new BnR(3 - billionaires, 3 - robbers, 1 - shipState);
    }

    private boolean isValid() {
        var reverseState = getReverse();
        var check1 = billionaires >= robbers || billionaires == 0;
        var check2 = reverseState.getBillionaires() >= reverseState.getRobbers() || reverseState.getBillionaires() == 0;
        var check3 = billionaires >= 0 && robbers >= 0;
        var check4 = reverseState.getBillionaires() >= 0 && reverseState.getRobbers() >= 0;
        return check1 && check2 && check3 && check4;
    }

    public List<BnR> getDistributions() {
        var b = getBillionaires();
        var r = getRobbers();
        var ss = getShipState();
        return (ss == 1
                ? Stream.of(
                new BnR(b - 1, r, 0, this),
                new BnR(b - 2, r, 0, this),
                new BnR(b, r - 1, 0, this),
                new BnR(b, r - 2, 0, this),
                new BnR(b - 1, r - 1, 0, this))
                : Stream.of(
                new BnR(b + 1, r, 1, this),
                new BnR(b + 2, r, 1, this),
                new BnR(b, r + 1, 1, this),
                new BnR(b, r + 2, 1, this),
                new BnR(b + 1, r + 1, 1, this)))
                .filter(BnR::isValid)
                .collect(Collectors.toList());
    }

    public String toString() {
        return String.format("(%d, %d, %d)", billionaires, robbers, shipState);
    }

    public boolean equals(BnR state2) {
        return toString().equals(state2.toString());
    }
}
