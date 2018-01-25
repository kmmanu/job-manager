package org.manu.jobmanager.job;

public interface Job extends Comparable<Job> {
    void execute();

    String getName();

    JobStatus getStatus();

    void setStatus(JobStatus status);

    int getPriority();

    @Override
    default int compareTo(Job other) {
        return Integer.compare(this.getPriority(), other.getPriority());
    }
}
