package org.jrts.core.store;

import java.util.Map;

public class TestDependency {

    private String test;
    private Map<String, String> dependencies;

    public TestDependency(String test) {
        this.test = test;
    }

    public TestDependency(String test, Map<String, String> dependencies) {
        this.test = test;
        this.dependencies = dependencies;
    }

    public String getTest() {
        return test;
    }

    public Map<String, String> getDependencies() {
        return dependencies;
    }

    public void setDependencies(Map<String, String> dependencies) {
        this.dependencies = dependencies;
    }
}
