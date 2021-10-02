package models;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Node<T> implements Cloneable {
    private T data;
    private List<Node<T>> children;

    public Node(T data) {
        this.data = data;
        children = new ArrayList<>();
    }

    public Node<T> clone(TypeReference<Node<T>> typeRef) {
        try {
            var mapper = new ObjectMapper();
            var json = mapper.writeValueAsString(this);
            return mapper.readValue(json, typeRef);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
