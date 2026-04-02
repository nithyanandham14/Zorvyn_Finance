package com.bjsn.finance.Service;

import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SmsService {

    @Value("${twilio.phone.number}")
    private String fromNumber;

    public void sendReminder(Long toNumber, String customerName) {

        String formattedNumber = "+91" + toNumber;

        Message.creator(
                new PhoneNumber(formattedNumber),
                new PhoneNumber(fromNumber),
                "Hello " + customerName +
                        ", your pawn item has completed 6 months. " +
                        "Please visit the shop to avoid extra interest."
        ).create();
    }
}
