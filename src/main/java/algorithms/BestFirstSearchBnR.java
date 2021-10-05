package algorithms;

import models.BnR;

import java.util.*;
import java.util.stream.Collectors;

public class BestFirstSearchBnR extends SearchBnR {
    @Override
    public void solve(BnR startState, BnR targetState) {
        // Khởi tạo queue L với luật tăng dần theo hàm đánh giá f = số tỉ phú + số tên cướp ở bờ tả
        Comparator<BnR> comparator = Comparator
                .comparingInt(state -> state.getBillionaires() + state.getRobbers());
        Queue<BnR> L = new PriorityQueue<>(comparator);

        // Khởi tạo map Q để sàng lọc các trạng thái lặp
        Map<String, Boolean> Q = new HashMap<>();
        L.add(startState);
        while (true) {
            var currentState = L.poll();
            assert currentState != null;
            if (currentState.equals(targetState)) {
                result = currentState;
                return;
            }
            // Đưa các trạng thái kề vào queue L
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
        var algorithm = new BestFirstSearchBnR();
        algorithm.solve(new BnR(3, 3, 1), new BnR(0, 0, 0));
        System.out.println(algorithm.getResult());
    }
}
