package DataEntities;

public enum Mode {
    NOT_FAIL, // Allow to execute the tasks when the CPU consumed reaches the max limit: 100% in the processor
    FAIL // Fail the tasks when the CPU consumed reaches the max limit: 100% in the processor
}
