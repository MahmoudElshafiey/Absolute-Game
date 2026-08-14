package Main;

public class Stage {
    public final String name;
    public final LevelDefinition[] levels;

    public Stage(String name, LevelDefinition[] levels) {
        this.name = name;
        this.levels = levels;
    }
}
