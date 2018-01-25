package org.manu.jobmanager;

import org.manu.jobmanager.job.Job;
import org.manu.jobmanager.job.SampleJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;

import static org.manu.jobmanager.job.JobStatus.*;

public class JobManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(SampleJob.class);

    private static JobManager INSTANCE = new JobManager();
    private final PriorityBlockingQueue<Job> jobs;
    private volatile boolean shouldStop;
    private boolean executionStarted;

    private JobManager() {
        jobs = new PriorityBlockingQueue<>();
    }

    public static JobManager getInstance() {
        return INSTANCE;
    }

    public void addJob(Job newJob) {
        newJob.setStatus(QUEUED);
        jobs.offer(newJob);
    }

    public void run() {
        executionStarted = true;
        while (!shouldStop) {
            Job jobToExecute = getJob();
            if (jobToExecute == null) {
                break;
            }
            execute(jobToExecute);
        }
    }

    /**
     * Execute the job and update the status.
     */
    private void execute(Job jobToExecute) {
        try {
            jobToExecute.setStatus(QUEUED);
            jobToExecute.execute();
            jobToExecute.setStatus(SUCCESS);
        } catch (Exception e) {
            LOGGER.warn("Failed to execute job {}", jobToExecute, e);
            jobToExecute.setStatus(FAILED);
        }
    }

    private Job getJob() {
        try {
            return jobs.poll(1, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            LOGGER.info("No more jobs to execute.  Exiting job manager", e);
        }
        return null;
    }
}
