package hr.ja.st.dev;

public class DebugContext {
    private static final ThreadLocal<DebugInfo> TL = new ThreadLocal<>();

    static void set(DebugInfo info) { TL.set(info); }
    public static DebugInfo current() { return TL.get(); }
    static void clear() { TL.remove(); }
}
