package animals.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import static animals.service.ResourceBundleService.getLocalString;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Node {
    private boolean isLeaf;
    public String data;
    public Node yes;
    public Node no;

    public Node(String data) {
        this.data = data;
        yes = null;
        no = null;
        isLeaf = true;
    }

    public Node() {
    }

    public boolean isLeaf() {
        return isLeaf;
    }

    public void setLeaf(boolean leaf) {
        isLeaf = leaf;
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

    @JsonIgnore
    public String askQ() {
        if (isLeaf) {
            return getLocalString("is.it") + data + "?";
        }
        return data;
    }
}
