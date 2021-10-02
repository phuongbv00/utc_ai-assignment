package models;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Point {
    private int h;
    private String label;
    private Integer f;

    public Point(String label, int h) {
        this.label = label;
        this.h = h;
    }
}
