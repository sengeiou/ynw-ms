package com.ynw.system.util;

import com.ynw.system.entity.AcUser;
import com.ynw.system.entity.Statistics;
import com.ynw.system.service.AcUserService;
import com.ynw.system.service.StatisticsService;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Component
public class ScheduledTasks {

    @Resource
    private AcUserService acUserService;
    @Resource
    private DataToExcel dataToExcel;
    @Resource
    private StatisticsService statisticsService;

    /**
     * 每天晚上12点进行一些定时操作
     */

    @Scheduled(cron = "50 59 23 ? * *")
    public void everyNight(){
        System.out.println("------开始下载-------");
        String dateToString = DateUtils.dateString(new Date());
        //下载每天注册用户信息
        String sheetName = "每天注册用户信息";
        String fileName = "";
        String[][] content;
        List<AcUser> userList = acUserService.scheduleTimeRegisteredUser(dateToString);
        String[] title = {"id","编号","昵称","姓名","性别","年龄","生日","身份证","电话","行业","学校","学历",
                "故乡","城市","注册时间","推荐人", "最后登录时间"};
        if (userList.size() > 0) {
            content = new String[userList.size()][];
            setContent(userList, content, title.length);
            fileName = dateToString +"-1.xls";
            dataToExcel.downloadNativeExcel(sheetName, title, content, fileName);
        }
        //下载每天活跃用户信息
        List<AcUser> acUserList = acUserService.activeUser(dateToString);

        if (acUserList.size() > 0) {
            sheetName = "每日活跃用户";
            content = new String[userList.size()][];
            setContent(acUserList, content, title.length);
            fileName = dateToString +"-2.xls";
            dataToExcel.downloadNativeExcel(sheetName, title, content, fileName);
        }
        //下载统计信息
        Statistics statistics = statisticsService.statistics(dateToString);

        sheetName = "每日统计信息";
        String[] titles = {"用户总数","新增用户数","新增关注关系","新增好友关系","新增签到","新增动态","情豆发放",
                "情豆回收","情豆转移","问卷测试数","测试人数"};
        content = new String[1][];
        content[0] = new String[title.length];
        content[0][0] = String.valueOf(statistics.getAcUserCount());
        content[0][1] = String.valueOf(statistics.getNowAcUserCount());
        content[0][2] = String.valueOf(statistics.getNowAttention());
        content[0][3] = String.valueOf(statistics.getNowFriend());
        content[0][4] = String.valueOf(statistics.getNowSignIn());
        content[0][5] = String.valueOf(statistics.getNowMood());
        content[0][6] = String.valueOf(statistics.getNowLoveGrant());
        content[0][7] = String.valueOf(statistics.getNowLoveRecycle());
        content[0][8] = String.valueOf(statistics.getNowLoveTransfer());
        content[0][9] = String.valueOf(statistics.getNowExamText());
        content[0][10] = String.valueOf(statistics.getNowExamUserNum());
        fileName = dateToString +"-3.xls";
        dataToExcel.downloadNativeExcel(sheetName, titles, content, fileName);
    }

    /**
     *  二维数组赋值
     * @param userList
     * @param content
     */
    public void setContent(List<AcUser> userList, String[][] content, Integer length) {
        Integer index = 0;
        for (AcUser user: userList
        ) {
            content[index] = new String[length];
            content[index][0] = user.getAcUserId();
            content[index][1] = user.getNo();
            content[index][3] = user.getNickname();
            content[index][4] = user.getRealName();
            if (user.getSex() == 1) {
                content[index][4] = "女";
            } else {
                content[index][4] = "男";
            }
            content[index][5] = String.valueOf(user.getAge());
            if (null == user.getBirthday()) {
                content[index][6] = null;
            } else {
                content[index][6] = DateUtils.dateString(user.getBirthday());
            }
            content[index][7] = user.getIdNumber();
            content[index][8] = user.getPhoneNumber();
            content[index][9] = user.getBdBusinessName();
            content[index][10] = user.getBdUniversityName();
            content[index][11] = user.getBdDegreesId();
            content[index][12] = user.getHometownName();
            content[index][13] = user.getBdCityName();
            content[index][14] = DateUtils.dateString(user.getCreateTime());
            content[index][15] = user.getReferrerId();
            if (null == user.getLastCallTime()) {
                content[index][16] = null;
            } else {
                content[index][16] = DateUtils.dateString(user.getLastCallTime());
            }
            index++;
        }
    }

}
