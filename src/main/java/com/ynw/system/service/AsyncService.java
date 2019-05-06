package com.ynw.system.service;

import com.aliyuncs.exceptions.ClientException;
import com.ynw.system.entity.Activity;
import com.ynw.system.util.DataToExcel;
import com.ynw.system.util.DateUtils;
import com.ynw.system.util.SendSmsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class AsyncService {

    @Autowired
    private DataToExcel dataToExcel;

    /**
     * 开启异步发送短信
     * @param activityList
     */
    @Async
    public void asyncInvokeWithParameter(List<Activity> activityList) {
        List<String> successPhone = new ArrayList<>();//将发送成功的电话存储
        List<String> phoneList = new ArrayList<>();//将发送失败的电话存储
        String dateToString = DateUtils.dateString(new Date());
        String[] title = {"电话"};
        String[][] content;
        try {
            for (Activity activity: activityList
            ) {
                String phone = activity.getPhoneNumber();
                Boolean fun = SendSmsUtil.CPNote(activity.getPhoneNumber(), "SMS_163437263");
                if (fun) {
                    successPhone.add(phone);
                } else {
                    phoneList.add(phone);
                }
            }
        } catch (ClientException e) {
            System.out.println("---------------阿里发送报错发送失败-------------------");
            Integer successPhoneSize = successPhone.size();
            content = new String[successPhoneSize][];
            setContent(successPhone, content);
            String sheetName = dateToString + "发送成功用户电话";
            dataToExcel.downloadNativeExcel(sheetName, title, content, sheetName + ".xls");
            e.printStackTrace();
        }
        Integer phoneListSize = phoneList.size();
        if (phoneListSize > 0) {
            content = new String[phoneListSize][];
            setContent(successPhone, content);
            String sheetName = dateToString + "发送失败用户电话";
            System.out.println("---------------部分用户发送失败-------------------");
            dataToExcel.downloadNativeExcel(sheetName, title, content, sheetName + ".xls");
        }
        System.out.println("-----------发送完成------------");
    }

    /**
     *  二维数组赋值
     * @param phoneList
     * @param content
     */
    public void setContent(List<String> phoneList, String[][] content) {
        Integer i = 0;
        for (String string: phoneList
             ) {
            content[i] = new String[1];
            content[i][0] = string;
        }
    }

}
