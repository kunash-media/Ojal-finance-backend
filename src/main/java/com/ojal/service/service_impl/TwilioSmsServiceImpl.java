package com.ojal.service.service_impl;

import com.ojal.service.MessageService;
import com.twilio.http.TwilioRestClient;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service("twilioSmsService")
public class TwilioSmsServiceImpl implements MessageService {
    private final TwilioRestClient twilioRestClient;
    private final String twilioPhoneNumber;

    @Autowired
    public TwilioSmsServiceImpl(
            TwilioRestClient twilioRestClient,
            @Value("${twilio.phone.number}") String phoneNumber) {
        this.twilioRestClient = twilioRestClient;
        this.twilioPhoneNumber = phoneNumber;
    }

    @Override
    public void sendMessage(String to, String message) {
        Message.creator(
                new PhoneNumber(to),
                new PhoneNumber(twilioPhoneNumber),
                message
        ).create(twilioRestClient);
    }
}