package algorithms;

import models.BnR;

import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class DepthFistSearchBnR extends SearchBnR {
    @Override
    public void solve(BnR startState, BnR targetState) {
        // Tạo stack L (trong java thì dùng deque)
        Deque<BnR> L = new LinkedList<>();

        // Tạo map Q để loại bỏ trạng thái lặp
        Map<String, Boolean> Q = new HashMap<>();
        L.add(startState);
        while (true) {
            // Xét trạng thái đầu stack L
            var currentState = L.removeFirst();
            assert currentState != null;
            if (currentState.equals(targetState)) {
                result = currentState;
                return;
            }
            // Đưa các trạng thái kề vào stack L
            currentState.getDistributions()
                    .stream()
                    .filter(bnr -> !Q.containsKey(bnr.toString()))
                    .forEach(bnr -> {
                        L.addFirst(bnr);
                        Q.put(bnr.toString(), true);
                    });
            Q.put(currentState.toString(), true);
        }
    }

    public static void main(String[] args) {
        var algorithm = new DepthFistSearchBnR();
        algorithm.solve(new BnR(3, 3, 1), new BnR(0, 0, 0));
        System.out.println(algorithm.getResult());
    }
}
