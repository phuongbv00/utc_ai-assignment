package models;

import lombok.Getter;
import lombok.SneakyThrows;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Getter
public class DisjunctionProposition {
    // Biểu diễn dạng thô của công thức
    private final String raw;

    // Biểu diễn các mệnh đề của công thức về dạng bảng băm
    // key: mệnh đề
    // value: true ~ khẳng định, false ~ phủ định
    private Map<String, Boolean> propositionsMap = new HashMap<>();

    public DisjunctionProposition(String raw) {
        this.raw = raw;
        List.of(raw.split("v"))
                .forEach(p -> propositionsMap.put(p.replace("!", ""), !p.contains("!")));
    }

    public DisjunctionProposition(Map<String, Boolean> propositionsMap) {
        this.propositionsMap = propositionsMap;
        this.raw = propositionsMap.entrySet()
                .stream()
                .map(entry -> entry.getValue() ? entry.getKey() : "!" + entry.getKey())
                .collect(Collectors.joining("v"));
    }

    @SneakyThrows
    public DisjunctionProposition resolve(DisjunctionProposition proposition2) {
        var flag = new AtomicBoolean(false);
        // Điểm giao - Ví dụ: điểm giao của AvB và Cv!B là B
        AtomicReference<String> intersection = new AtomicReference<>("");
        proposition2.getPropositionsMap()
                .forEach((k1, v1) -> propositionsMap
                        .forEach((k2, v2) -> {
                            // Trùng key và khác value thì hợp lệ để thực hiện phân giải
                            if (k1.equals(k2) && !v1.equals(v2)) {
                                flag.set(true);
                                intersection.set(k1);
                            }
                        }));
        var newPropositionsMap = new HashMap<String, Boolean>();
        // Gộp tất cả các mệnh đề và loại bỏ điểm giao
        newPropositionsMap.putAll(proposition2.getPropositionsMap());
        newPropositionsMap.putAll(propositionsMap);
        newPropositionsMap.remove(intersection.get());
        // Mâu thuẫn xảy ra
        if (newPropositionsMap.isEmpty())
            throw new Exception();
        return flag.get() ? new DisjunctionProposition(newPropositionsMap) : null;
    }

    public boolean match(DisjunctionProposition proposition2) {
        var flag = new AtomicBoolean(false);
        proposition2.getPropositionsMap()
                .forEach((k1, v1) -> propositionsMap
                        .forEach((k2, v2) -> {
                            // Trùng key và khác value thì hợp lệ để thực hiện phân giải
                            if (k1.equals(k2) && !v1.equals(v2)) {
                                flag.set(true);
                            }
                        }));
        return flag.get();
    }

    @Override
    public String toString() {
        return raw;
    }
}
