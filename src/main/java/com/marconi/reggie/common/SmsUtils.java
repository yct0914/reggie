package com.marconi.reggie.common;

import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.AddShortUrlRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.aliyun.tea.TeaException;
import com.aliyun.teaopenapi.models.Config;
import com.aliyun.teautil.models.RuntimeOptions;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Marconi
 * @date 2022/7/22
 */
@Slf4j
public final class SmsUtils {

    private static String accessKeyId = "your AccessKeyId";

    private static String accessKeySecret = "Your AccessKeySecret";

    private static String signName = "阿里云短信测试";

    private static String templateCode = "SMS_154950909";

    private static String validateCode;

    private SmsUtils(){}

    private static void generateValidateCode(int count) {
        validateCode = String.valueOf((int)((Math.random()*9+1)* Math.pow(10,count-1)));
    }

    public static Map<String,Object> sendShortMessage(String accessKeyId, String accessKeySecret, String signName, String templateCode, String phoneNumber) throws Exception {
        Config config = new Config()
                // 您的 AccessKey ID
                .setAccessKeyId(accessKeyId)
                // 您的 AccessKey Secret
                .setAccessKeySecret(accessKeySecret);
        // 访问的域名
        config.endpoint = "dysmsapi.aliyuncs.com";
        Client client = new Client(config);
        AddShortUrlRequest request = new AddShortUrlRequest();
        generateValidateCode(6);
        SendSmsRequest sendSmsRequest = new SendSmsRequest()
                .setSignName(signName)
                .setTemplateCode(templateCode)
                .setPhoneNumbers(phoneNumber)
                .setTemplateParam("{\"code\":\""+validateCode+"\"}");
        RuntimeOptions runtime = new RuntimeOptions();
        try {
            // 复制代码运行请自行打印 API 的返回值
            SendSmsResponse response = client.sendSmsWithOptions(sendSmsRequest, runtime);
            String message = response.getBody().getMessage();
            if ("OK".equals(message)){
                Map<String, Object> res = new HashMap<>(2);
                res.put("status",1);
                res.put("code",validateCode);
                return res;
            }
            Map<String, Object> res = new HashMap<>(2);
            res.put("status", 0);
            res.put("message", message);
            return res;
        } catch (TeaException error) {
            // 如有需要，请打印 error
            com.aliyun.teautil.Common.assertAsString(error.message);
        } catch (Exception _error) {
            TeaException error = new TeaException(_error.getMessage(), _error);
            // 如有需要，请打印 error
            com.aliyun.teautil.Common.assertAsString(error.message);
        }
        return  null;
    }

    public static Map<String, Object> sendShortMessage(String phoneNumber) throws Exception {
        return sendShortMessage(accessKeyId, accessKeySecret, signName, templateCode, phoneNumber);
    }

}
