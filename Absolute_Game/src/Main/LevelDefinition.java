package Main;

public class LevelDefinition {
    public final String name;
    public final String mapPath;
    public final int goalPoints;
    public final int negPointCount;

    public LevelDefinition(String name, String mapPath, int goalPoints) {
        this(name, mapPath, goalPoints, 1);
    }

    public LevelDefinition(String name, String mapPath, int goalPoints, int negPointCount) {
        this.name = name;
        this.mapPath = mapPath;
        this.goalPoints = goalPoints;
        this.negPointCount = negPointCount;
    }
}
