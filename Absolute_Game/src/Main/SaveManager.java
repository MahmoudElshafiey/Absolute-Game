package Main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Persists stage/level unlock progress across game launches.
 * Format: one "stageIndex=completedLevels" per line in progress.dat.
 * completedLevels is 0-based: the number of levels fully completed.
 */
public class SaveManager {
    private static final String SAVE_FILE = "progress.dat";

    // stageIndex -> number of completed levels (0-based count)
    private final Map<Integer, Integer> stageCompleted = new HashMap<>();

    public SaveManager() {
        load();
    }

    public void load() {
        stageCompleted.clear();
        File file = new File(SAVE_FILE);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.trim().split("=");
                if (parts.length != 2) continue;
                try {
                    int stage = Integer.parseInt(parts[0].trim());
                    int completed = Integer.parseInt(parts[1].trim());
                    if (stage >= 0 && completed >= 0) {
                        stageCompleted.put(stage, completed);
                    }
                } catch (NumberFormatException ignored) {
                    // skip malformed lines
                }
            }
        } catch (IOException e) {
            System.err.println("[Save] Failed to load progress: " + e.getMessage());
        }
    }

    public void save() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(SAVE_FILE))) {
            for (Map.Entry<Integer, Integer> entry : stageCompleted.entrySet()) {
                writer.write(entry.getKey() + "=" + entry.getValue());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("[Save] Failed to save progress: " + e.getMessage());
        }
    }

    public int getCompletedLevels(int stageIndex) {
        Integer completed = stageCompleted.get(stageIndex);
        return completed == null ? 0 : completed;
    }

    public boolean isLevelUnlocked(int stageIndex, int levelIndex) {
        return levelIndex <= getCompletedLevels(stageIndex);
    }

    public boolean isStageUnlocked(int stageIndex) {
        if (stageIndex <= 0) return true;
        if (stageIndex >= StageManager.STAGES.length) return false;
        int prevStage = stageIndex - 1;
        return getCompletedLevels(prevStage) >= StageManager.STAGES[prevStage].levels.length;
    }

    public void completeLevel(int stageIndex, int levelIndex) {
        int completed = getCompletedLevels(stageIndex);
        int newCompleted = Math.max(completed, levelIndex + 1);
        if (newCompleted > completed) {
            stageCompleted.put(stageIndex, newCompleted);
            save();
        }
    }
}
