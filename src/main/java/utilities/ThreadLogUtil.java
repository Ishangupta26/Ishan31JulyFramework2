package utilities;

public class ThreadLogUtil {

    private static ThreadLocal<StringBuilder> threadLog = ThreadLocal.withInitial(StringBuilder::new);

    // Append message to this thread's log buffer
    public static void log(String message) {
        StringBuilder sb = threadLog.get();
        sb.append(message).append("\n");
    }

    // Print all logs for this thread, then clear buffer
    public static void printAndClear() {
        StringBuilder sb = threadLog.get();
        System.out.println("----- Logs for Thread " + Thread.currentThread().getId() + " -----");
        System.out.println(sb.toString());
        sb.setLength(0);  // Clear buffer for next use
    }
}
