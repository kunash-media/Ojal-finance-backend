package com.ojal.util;

public class MessageTemplate {

    // SMS Templates
    public static String getOtpSms(String otp) {
        return "Your OTP for Ojal Microfinance is: " + otp + ". Valid for 5 minutes.";
    }

    public static String getPaymentConfirmationSms(String amount, String date) {
        return "Payment of $" + amount + " confirmed on " + date + ". Thank you!";
    }

    // WhatsApp Templates
    public static String getWelcomeWhatsApp(String userName) {
        return "👋 Welcome *" + userName + "* to Ojal Microfinance! Reply HELP for support.";
    }
}