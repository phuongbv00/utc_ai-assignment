package algorithms;

import lombok.*;
import models.DisjunctionProposition;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ResolutionProve {
    // Danh sách kết quả, sử dụng để in bảng chứng minh
    private List<Result> results;

    // Kết quả chứng minh
    private boolean isProved;

    private String needle;

    public void propositionProve(String needle, List<String> clauses) {
        results = new ArrayList<>();
        isProved = false;
        this.needle = needle;

        // Tạo phản mệnh đề của mệnh đề cần chứng minh
        var contrastNeedleProposition = new DisjunctionProposition("!" + needle);

        // Các mệnh đề giả thiết
        var propositions = clauses
                .stream()
                .map(DisjunctionProposition::new)
                .collect(Collectors.toList());

        // Tạo map Q để loại bỏ sinh mệnh đề lặp
        Map<String, Boolean> Q = new HashMap<>();

        // Tạo queue thỏa
        Queue<DisjunctionProposition> THOA = new LinkedList<>();
        THOA.add(contrastNeedleProposition);

        results.add(Result.builder().THOA(List.of(contrastNeedleProposition.toString())).build());

        while (!THOA.isEmpty()) {
            var result = new Result();
            // Lấy mệnh đề đầu queue thỏa
            var u = THOA.poll();
            assert u != null;
            result.setU(u.toString());
            try {
                propositions
                        .stream()
                        // Lọc các mệnh đề v có thế phân giải với u
                        .filter(c -> c.match(u))
                        .peek(p -> result.getV().add(p.toString()))
                        // Phân giải được res(u, v)
                        .map(c -> c.resolve(u))
                        .peek(p -> result.getRes().add(p.toString()))
                        // Lọc các mệnh đề đã tồn tại
                        .filter(p -> !Q.containsKey(p.toString()))
                        .peek(p -> Q.put(p.toString(), true))
                        // Đưa các mệnh đề mới vào queue thỏa
                        .forEach(THOA::add);
                result.setTHOA(THOA.stream()
                        .map(DisjunctionProposition::toString)
                        .collect(Collectors.toList()));
                results.add(result);
            } catch (Exception e) {
                isProved = true;
                result.getRes().add("Mâu thuẫn");
                result.setTHOA(THOA.stream()
                        .map(DisjunctionProposition::toString)
                        .collect(Collectors.toList()));
                results.add(result);
                break;
            }
        }
    }

    public void print() {
        var f1 = results
                .stream()
                .filter(r -> r.getU() != null)
                .mapToInt(r -> r.getU().length())
                .max()
                .orElse(0) + 2;
        var f2 = results
                .stream()
                .filter(r -> r.getV() != null)
                .mapToInt(r -> r.getV()
                        .stream()
                        .mapToInt(String::length)
                        .max()
                        .orElse(0))
                .max()
                .orElse(0) + 2;
        var f3 = results
                .stream()
                .filter(r -> r.getRes() != null)
                .mapToInt(r -> r.getRes()
                        .stream()
                        .mapToInt(String::length)
                        .max()
                        .orElse(0))
                .max()
                .orElse(0) + 2;
        var f4 = results
                .stream()
                .filter(r -> r.getTHOA() != null)
                .mapToInt(r -> r.getTHOA()
                        .stream()
                        .mapToInt(String::length)
                        .map(operand -> operand + 2)
                        .sum())
                .max()
                .orElse(0);
        System.out.printf("|%" + f1 + "s|%" + f2 + "s|%" + f3 + "s|%" + f4 + "s|%n", "u", "v", "res(u,v)", "THOA");
        results.forEach(result -> {
            System.out.printf("+%" + f1 + "s+%" + f2 + "s+%" + f3 + "s+%" + f4 + "s+%n",
                    IntStream.range(0, f1).mapToObj(__ -> "-").collect(Collectors.joining()),
                    IntStream.range(0, f2).mapToObj(__ -> "-").collect(Collectors.joining()),
                    IntStream.range(0, f3).mapToObj(__ -> "-").collect(Collectors.joining()),
                    IntStream.range(0, f4).mapToObj(__ -> "-").collect(Collectors.joining()));
            if (result.getV().size() == 0) {
                System.out.printf("|%" + f1 + "s|%" + f2 + "s|%" + f3 + "s|%" + f4 + "s|%n", result.getU(), "", "", String.join(", ", result.getTHOA()));
            }
            for (var i = 0; i < result.getV().size(); i++) {
                if (i == 0) {
                    System.out.printf("|%" + f1 + "s|%" + f2 + "s|%" + f3 + "s|%" + f4 + "s|%n", result.getU(), result.getV().get(i), result.getRes().get(i), String.join(", ", result.getTHOA()));
                } else {
                    System.out.printf("|%" + f1 + "s|%" + f2 + "s|%" + f3 + "s|%" + f4 + "s|%n", "", result.getV().get(i), result.getRes().get(i), "");
                }
            }
        });
        if (isProved) {
            System.out.println("=> " + needle + " được chứng minh");
        } else {
            System.out.println("=> " + needle + " không được chứng minh");
        }
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    private static class Result {
        @Builder.Default
        private String u = "";
        @Builder.Default
        private List<String> v = new ArrayList<>();
        @Builder.Default
        private List<String> res = new ArrayList<>();
        @Builder.Default
        private List<String> THOA = new ArrayList<>();
    }

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
