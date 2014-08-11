package eu.ammw.msc.plaga.common;

/**
 *
 * @author AMW
 */
public enum ServiceType {
    EXEC("Task Executor"),
    UI("Task upload interface"),
    MONITOR("Task Monitor"),
    COLLECTOR("Data Collector");

    private String description;

    ServiceType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
