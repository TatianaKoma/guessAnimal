package animals.service;

import java.util.ResourceBundle;
import static animals.service.ResourceBundleService.getLocalString;

public enum MenuService {
    PLAY(1, getLocalString("play")),
    SHOW_LIST(2, getLocalString("show.list")),
    SEARCH_ANIMAL(3, getLocalString("search.animal")),
    STATISTICS(4, getLocalString("stats")),
    KNOWLEDGE_TREE(5, getLocalString("tree")),
    EXIT(0, getLocalString("exit")),
    UNDEFINED(-1, "");
    private final int command;
    private final String description;

    MenuService(int command, String description) {
        this.command = command;
        this.description = description;
    }

    public int getCommand() {
        return command;
    }

    public String getDescription() {
        return description;
    }

    public static MenuService getInstance(int command) {
        for (MenuService v : values()) {
            if (v.getCommand() == command) {
                return v;
            }
        }
        return UNDEFINED;
    }

    public static String getMenuStr() {
        ResourceBundle bundle = ResourceBundle.getBundle("application");
        StringBuilder str = new StringBuilder(bundle.getString("what.do"));
        for (MenuService item : MenuService.values()) {
            if (item.isUndefined()) {
                continue;
            }
            str.append(System.lineSeparator());
            str.append(item.getCommand()).append(") ").append(item.getDescription());
        }
        str.append("\n").append(bundle.getString("choice"));
        return str.toString();
    }

    public boolean isUndefined() {
        return this == UNDEFINED;
    }
}
