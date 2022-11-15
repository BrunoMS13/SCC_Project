package utils;

public class IdGenerator {

    private static IdGenerator instance;
    private static long idCounter = 0;

    public static synchronized IdGenerator getInstance() {
        if(instance != null)
            return instance;
        instance = new IdGenerator();
        return instance;
    }
    private IdGenerator() {}

    public String generateUniqueId() {
        return "" + idCounter++;
    }
}
