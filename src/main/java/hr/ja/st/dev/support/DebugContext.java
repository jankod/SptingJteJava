package hr.ja.st.dev.support;

public class DebugContext {
    private static final ThreadLocal<DebugInfo> TL = new ThreadLocal<>();

    public static void set(DebugInfo info) { TL.set(info); }
    public static DebugInfo current() { return TL.get(); }
    public static void clear() { TL.remove(); }
}
