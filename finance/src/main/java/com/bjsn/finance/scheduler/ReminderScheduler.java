package com.bjsn.finance.scheduler;

import com.bjsn.finance.Module.Item;
import com.bjsn.finance.Service.SmsService;
import com.bjsn.finance.repo.Itemrepo;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDate;
import java.util.List;

public class ReminderScheduler {
    private final Itemrepo pawnRepository;
    private final SmsService smsService;

    public ReminderScheduler(Itemrepo pawnRepository, SmsService smsService) {
        this.pawnRepository = pawnRepository;
        this.smsService = smsService;
    }

    // Runs daily at 10 AM
    @Scheduled(cron = "0 0 10 * * ?")
    public void sendSixMonthReminder() {
        List<Item> dueItems =
                pawnRepository.findByReminderDate(LocalDate.now());

        for (Item item : dueItems) {
            smsService.sendReminder(
                    item.getNumber(),
                    item.getName()
            );
        }
    }
}
