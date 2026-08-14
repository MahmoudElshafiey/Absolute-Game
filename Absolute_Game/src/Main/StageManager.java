package Main;

public class StageManager {
    public static final Stage[] STAGES = {
        new Stage("Stage 1", new LevelDefinition[] {
            new LevelDefinition("Level 1", "/Map/level1_1.txt", 10),
            new LevelDefinition("Level 2", "/Map/level1_2.txt", 15, 2),
            new LevelDefinition("Level 3", "/Map/level1_3.txt", 20, 3),
            new LevelDefinition("Level 4", "/Map/level1_4.txt", 25, 4),
            new LevelDefinition("Level 5", "/Map/level1_5.txt", 30, 5)
        })
    };
}
