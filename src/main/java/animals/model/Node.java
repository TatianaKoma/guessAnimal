package animals.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import static animals.service.ResourceBundleService.getLocalString;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Node {

    private String data;
    private Node yes;
    private Node no;

    public Node(String data) {
        this.data = data;
    }

    public Node() {
    }

    public String getData() {
        return data;
    }

    public Node getYes() {
        return yes;
    }

    public Node getNo() {
        return no;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void setYes(Node yes) {
        this.yes = yes;
    }

    public void setNo(Node no) {
        this.no = no;
    }

    @JsonIgnore
    public String askQ() {
        if (isLeaf()) {
            return getLocalString("is.it") + data + "?";
        }
        return data;
    }

    @JsonIgnore
    public boolean isLeaf() {
        return no == null && yes == null;
    }
}
