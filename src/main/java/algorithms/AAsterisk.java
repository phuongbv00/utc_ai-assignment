package algorithms;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import models.Node;
import models.Point;

import java.util.*;
import java.util.stream.Collectors;

public class AAsterisk {
    // Khởi tạo đồ thị
    Map<String, Node<Point>> nodeMap = getNodeMap();

    // Khởi tạo lưu trữ độ dài đường đi giữa các điểm
    Map<String, Integer> kMap = getKMap();

    // Khởi tạo độ dài đường đi hiện tại
    Integer currentPathLength = 0;

    // Khởi tạo danh sách các điểm đã đi qua
    List<Point> currentPath = new ArrayList<>();

    // Tên điểm đang được duyệt
    String currentNodeLabel;

    // Khởi tạo priority queue L sắp sếp theo f tăng dần
    Queue<Node<Point>> L = new PriorityQueue<>(Comparator.comparing(node -> node.getData().getF()));

    public Integer getCurrentPathLength() {
        return currentPathLength;
    }

    public String getCurrentPathInString() {
        return currentPath.stream().map(Point::getLabel).collect(Collectors.joining(" -> "));
    }

    private Map<String, Integer> getKMap() {
        var map = new HashMap<String, Integer>();
        map.put("AC", 20);
        map.put("AD", 13);
        map.put("AE", 7);
        map.put("AG", 9);
        map.put("CF", 4);
        map.put("CI", 6);
        map.put("DI", 5);
        map.put("DH", 2);
        map.put("ED", 4);
        map.put("EK", 8);
        map.put("GK", 6);
        map.put("HB", 3);
        map.put("IB", 5);
        map.put("IH", 9);
        map.put("KH", 5);
        return map;
    }

    private Map<String, Node<Point>> getNodeMap() {
        var nodeA = new Node<>(new Point("A", 14));
        var nodeB = new Node<>(new Point("B", 0));
        var nodeC = new Node<>(new Point("C", 7));
        var nodeD = new Node<>(new Point("D", 5));
        var nodeE = new Node<>(new Point("E", 6));
        var nodeF = new Node<>(new Point("F", 12));
        var nodeG = new Node<>(new Point("G", 15));
        var nodeH = new Node<>(new Point("H", 2));
        var nodeI = new Node<>(new Point("I", 4));
        var nodeK = new Node<>(new Point("K", 10));
        nodeA.setChildren(List.of(nodeC, nodeD, nodeE, nodeG));
        nodeC.setChildren(List.of(nodeI, nodeF));
        nodeD.setChildren(List.of(nodeI, nodeH));
        nodeE.setChildren(List.of(nodeD, nodeK));
        nodeG.setChildren(List.of(nodeK));
        nodeH.setChildren(List.of(nodeB));
        nodeI.setChildren(List.of(nodeB, nodeH));
        nodeK.setChildren(List.of(nodeH));
        return new ArrayList<>(List.of(nodeA, nodeB, nodeC, nodeD, nodeE, nodeF, nodeG, nodeH, nodeI, nodeK))
                .stream()
                .collect(Collectors.toMap(node -> node.getData().getLabel(), node -> node));
    }

    public void solve(String startLabel, String targetLabel) {
        // Đưa A vào queue L
        L.add(nodeMap.get(startLabel));
        while (!L.isEmpty()) {
            // Xét điểm đầu tiên của queue L
            var currentNode = L.poll();
            var currentPoint = currentNode.getData();
            currentPath.add(currentPoint);
            currentNodeLabel = currentPoint.getLabel();
            // Nếu như điểm được xét là điểm đích thì kết thúc
            if (currentNodeLabel.equals(targetLabel)) {
                return;
            }
            // Đưa các điểm kề với điểm được xét vào queue L
            var childrenNodes = currentNode.getChildren()
                    .stream()
                    .map(n -> n.clone(new TypeReference<Node<Point>>() {}))
                    .peek(n -> {
                        // Tính toán k, h, g, f
                        var k = kMap.get(currentNodeLabel + n.getData().getLabel());
                        var h = n.getData().getH();
                        var g = k + currentPathLength;
                        var f = h + g;
                        n.getData().setF(f);
                    })
                    .collect(Collectors.toList());
            L.addAll(childrenNodes);
            assert L.peek() != null;
            // Cộng dồn độ dài đường đi với điểm có f nhỏ nhất (điểm đầu tiên trong queue L)
            currentPathLength += kMap.get(currentNodeLabel + L.peek().getData().getLabel());
        }
    }

    public static void main(String[] args) {
        var algorithm = new AAsterisk();
        algorithm.solve("A", "B");
        System.out.println("Đường đi: " + algorithm.getCurrentPathInString());
        System.out.println("Độ dài đường đi: " + algorithm.getCurrentPathLength());
    }
}
