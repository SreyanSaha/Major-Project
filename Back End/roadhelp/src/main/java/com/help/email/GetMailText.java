package com.help.email;

public class GetMailText {
    public static final String userMailSubjectOTP="Account Registration OTP - User | Road Help";
    public static final String userMailTextOTP="""
    Dear User,

        Thank you for registering with Road Help.
        Please use the following One-Time Password (OTP) to complete your account registration:

        🔒 OTP: {}

        This OTP is valid for 1 minute.
        Please do not share this OTP with anyone for security reasons.
        If you did not initiate this registration, please ignore this email.

    Regards,
        Road Help Security Team
    """;
    public static final String adminMailSubjectOTP="Account Registration OTP - Admin | Road Help";
    public static final String adminMailTextOTP= """
       Dear Admin,

           An admin registration attempt has been made on Road Help.
           Please use the following One-Time Password (OTP) to complete the registration:

           🔒 OTP: {}

           This OTP is valid for 1 minute.
           Keep this OTP confidential to ensure the security of your admin account.
           If you did not initiate this request, please report to the System Administrator immediately.

       Regards,
           Road Help Security Team
    """;

}
