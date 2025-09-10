package hr.ja.st.dev;

import java.util.ArrayList;
import java.util.List;

public class DebugInfo {
    public String requestUri;
    public String controllerClass;
    public String controllerMethod;
    public String controllerSourcePath;
    public Integer controllerSourceLine;
    public java.util.List<String> requiredRoles = new java.util.ArrayList<>();
    public String viewName; // e.g. pages/home.jte
    public List<String> templateFiles = new ArrayList<>(); // absolute paths
}
