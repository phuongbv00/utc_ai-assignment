package algorithms;

import models.BnR;

import java.util.*;
import java.util.stream.Collectors;

public class HillClimbingSearchBnR extends SearchBnR {
    @Override
    public void solve(BnR startState, BnR targetState) {
        // Khởi tạo stack L
        Deque<BnR> L = new LinkedList<>();

        // Khởi tạo queue L với luật giảm dần theo hàm đánh giá f = số tỉ phú + số tên cướp ở bờ tả
        Comparator<BnR> comparator = Comparator
                .comparingInt((BnR state) -> state.getBillionaires() + state.getRobbers())
                .reversed();
        Queue<BnR> L1 = new PriorityQueue<>(comparator);

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
            // Lấy các trạng thái kề và loại bỏ trạng thái lặp
            var distributions = currentState.getDistributions()
                    .stream()
                    .filter(bnr -> !Q.containsKey(bnr.toString()))
                    .peek(bnr -> Q.put(bnr.toString(), true))
                    .collect(Collectors.toList());
            Q.put(currentState.toString(), true);

            // Đưa vào queue L1 để sắp xếp sau đó đưa vào stack L
            L1.addAll(distributions);
            while (!L1.isEmpty()) {
                L.addFirst(L1.poll());
            }
        }
    }

    public static void main(String[] args) {
        var algorithm = new HillClimbingSearchBnR();
        algorithm.solve(new BnR(3, 3, 1), new BnR(0, 0, 0));
        System.out.println(algorithm.getResult());
    }
}
