package com.ojal.service.service_impl;

import com.ojal.service.MessageService;
import com.twilio.http.TwilioRestClient;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service("twilioWhatsAppService")
public class TwilioWhatsAppServiceImpl implements MessageService {
    private final TwilioRestClient twilioClient;
    private final String fromWhatsAppNumber;

    @Autowired
    public TwilioWhatsAppServiceImpl(TwilioRestClient twilioClient, @Value("${twilio.whatsapp-number}") String fromWhatsAppNumber) {
        this.twilioClient = twilioClient;
        this.fromWhatsAppNumber = fromWhatsAppNumber;
    }

    @Override
    public void sendMessage(String to, String message) {
        Message.creator(
                new PhoneNumber("whatsapp:" + to),
                new PhoneNumber("whatsapp:" + fromWhatsAppNumber),
                message
        ).create(twilioClient);
    }
}