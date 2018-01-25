package org.manu.jobmanager;

import org.junit.Before;
import org.junit.Test;
import org.manu.jobmanager.job.Job;
import org.manu.jobmanager.job.SampleJob;
import org.mockito.InOrder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.manu.jobmanager.job.JobStatus.*;
import static org.mockito.Mockito.*;

public class JobManagerTest {

    private JobManager jobManager;

    @Before
    public void setUp() {
        jobManager = JobManager.getInstance();
    }

    @Test
    public void should_update_job_status_to_queued_when_added_to_jobmanager() {
        // Given
        Job job = new SampleJob(2, "Job1");

        // When
        jobManager.addJob(job);

        // Then
        assertThat(job.getStatus()).isEqualTo(QUEUED);
    }

    @Test
    public void should_update_job_status_to_success_when_after_execution() {
        // Given
        Job job = new SampleJob(2, "Job1");
        jobManager.addJob(job);

        // When
        jobManager.run();

        // Then
        assertThat(job.getStatus()).isEqualTo(SUCCESS);
    }


    @Test
    public void should_not_fail_job_manager_when_one_of_the_jobs_fails() {
        // Given
        Job job1 = getFailingJob();
        Job job2 = new SampleJob(1, "Job2");

        jobManager.addJob(job1);
        jobManager.addJob(job2);

        // When
        jobManager.run();

        // Then
        assertThat(job1.getStatus()).isEqualTo(FAILED);
        assertThat(job2.getStatus()).isEqualTo(SUCCESS);
    }

    @Test
    public void should_execute_jobs_according_to_priority() {
        // Given
        Job job1 = spy(new SampleJob(1, "Job1"));
        Job job2 = spy(new SampleJob(3, "Job2"));
        Job job3 = spy(new SampleJob(2, "Job3"));

        jobManager.addJob(job1);
        jobManager.addJob(job2);
        jobManager.addJob(job3);

        // When
        jobManager.run();

        // Then
        InOrder inOrder = inOrder(job1, job2, job3);
        inOrder.verify(job1).execute();
        inOrder.verify(job3).execute();
        inOrder.verify(job2).execute();
    }


    private Job getFailingJob() {
        Job job1 = spy(new SampleJob(1, "Job1"));
        doThrow(new IllegalStateException()).when(job1).execute();
        return job1;
    }
}