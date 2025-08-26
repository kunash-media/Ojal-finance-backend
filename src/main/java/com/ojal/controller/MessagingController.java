package com.ojal.controller;


import com.ojal.model_entity.dto.request.MessageRequest;
import com.ojal.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/messages")
public class MessagingController {
    private final MessageService smsService;
    private final MessageService whatsAppService;

    @Autowired
    public MessagingController(
            @Qualifier("twilioSmsService") MessageService smsService,
            @Qualifier("twilioWhatsAppService") MessageService whatsAppService
    ) {
        this.smsService = smsService;
        this.whatsAppService = whatsAppService;
    }

    @PostMapping("/sms")
    public ResponseEntity<String> sendSms(@RequestBody MessageRequest request) {
        smsService.sendMessage(request.getTo(), request.getMessage());
        return ResponseEntity.ok("SMS sent successfully!");
    }

    @PostMapping("/whatsapp")
    public ResponseEntity<String> sendWhatsApp(@RequestBody MessageRequest request) {
        whatsAppService.sendMessage(request.getTo(), request.getMessage());
        return ResponseEntity.ok("WhatsApp message sent successfully!");
    }
}