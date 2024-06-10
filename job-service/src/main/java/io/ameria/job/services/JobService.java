package io.ameria.job.services;

import lombok.extern.slf4j.Slf4j;
import org.jobrunr.jobs.annotations.Job;
import org.jobrunr.scheduling.JobScheduler;
import org.jobrunr.spring.annotations.Recurring;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class JobService {

    @Recurring(id = "armsoft_pay", cron = "0 0 1 * * *", zoneId = "Asia/Yerevan")
    @Job(name = "armsoft_pay")
    public void sendPaymen() {

        log.info("Sending payment to most important people");

    }

    @Recurring(id = "send-emails", cron = "*/1 * * * *")
    @Job(name = "telegram")
    public void doSend() {
        log.info("Sending email: " + " Hello bla bla!");
    }

}
