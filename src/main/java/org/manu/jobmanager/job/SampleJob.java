package org.manu.jobmanager.job;

public class SampleJob implements Job {
    private final int priority;
    private final String name;
    private JobStatus status = JobStatus.NONE;

    public SampleJob(int priority, String name) {
        this.priority = priority;
        this.name = name;
    }

    @Override
    public void execute() {
        System.out.println("Executing " + this);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getPriority() {
        return priority;
    }

    @Override
    public JobStatus getStatus() {
        return status;
    }

    @Override
    public void setStatus(JobStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "SampleJob{" +
                "priority=" + priority +
                ", name='" + name + '\'' +
                '}';
    }
}
