package com.ynw.system.controller;

import com.ynw.system.dao.CpRelMapper;
import com.ynw.system.entity.*;
import com.ynw.system.entity.Dictionary;
import com.ynw.system.enums.ResultEnums;
import com.ynw.system.exception.MyException;
import com.ynw.system.service.*;
import com.ynw.system.util.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/ynw-ms/register")
@Api(tags = "一周情侣活动接口",description = "一周情侣活动查询及操作接口")
public class RegisterController {

    @Autowired
    private RegisterService registerService;
    @Autowired
    private RegisterClassifyService registerClassifyService;
    @Autowired
    private ActivityService activityService;
    @Autowired
    private HandleFile handleFile;
    @Autowired
    private HuanXinService huanXinService;
    @Autowired
    private DictionaryService dictionaryService;
    @Autowired
    private AcUserService acUserService;
    @Autowired
    private ImGroupService imGroupService;
    @Autowired
    private CpRelService cpRelService;

    /**
     *  条件查询活动数据
     * @param register
     * @param nowPage
     * @return
     */
    @PostMapping("/conditionQueryRegister")
    @ApiOperation(value = "条件查询活动数据", notes = "传入：页码（nowPage必传），活动名称（name），活动状态（status）")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "nowPage", value = "页码", required = true, dataType = "Long", paramType = "query",example = "1"),
            @ApiImplicitParam(name = "status", value = "活动状态", dataType = "Long", paramType = "query",example = "1"),
    })
    public ResponseEntity<Result> conditionQueryRegister(Register register, Integer nowPage, Integer status) {
        if (null != nowPage && nowPage > 0) {
            Integer pageSize = 10;
            Integer start = (nowPage - 1) * pageSize;
            List<Register> registerList = registerService.conditionQueryRegister(register, pageSize, start, status);
            if (registerList.size() > 0) {
                List<Map<String,Object>> mapList = new ArrayList<>();
                //获取今天开始时间
                Date beginDate = DateUtils.getDayBegin();
                //获取当天的结束时间
                Date endDate = DateUtils.getDayEnd();
                for (Register registers: registerList
                     ) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("register", registers);
                    //判断活动是否开始
                    if (registers.getBeginTime().after(beginDate)) {
                        map.put("status", 1);
                    } else if ((registers.getBeginTime().before(beginDate) || registers.getBeginTime().equals(beginDate))
                            && (registers.getEndTime().after(endDate) || registers.getEndTime().equals(endDate))){
                        map.put("status", 2);
                    } else if (registers.getEndTime().before(endDate) || registers.getEndTime().equals(endDate)) {
                        map.put("status", 3);
                    }
                    mapList.add(map);
                }
                return ResultUtil.successResponse(mapList);
            }
            return ResultUtil.errorResponse(ResultEnums.NO_DATA_ERROR);
        }
        return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
    }

    /**
     *  条件查询数据总数
     * @param register
     * @return
     */
    @PostMapping("/count")
    @ApiOperation(value = "条件查询数据总数", notes = "传入：活动名称（name），活动状态（status）")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "status", value = "活动状态", dataType = "Long", paramType = "query",example = "1"),
    })
    public ResponseEntity<Result> count(Register register, Integer status) {
        Integer count = registerService.count(register, status);
        return ResultUtil.successResponse(count);
    }

    /**
     *  根据编号查询活动
     * @param atRegisterId
     * @return
     */
    @PostMapping("/findById")
    @ApiOperation(value = "根据编号查询活动", notes = "传入：活动编号（atRegisterId)(必传)")
    @ApiImplicitParam(name = "atRegisterId", value = "活动编号", required = true, dataType = "String", paramType = "query")
    public ResponseEntity<Result> findById(String atRegisterId) {
        if (StringUtils.isNotEmpty(atRegisterId)) {
            Register register = registerService.selectOne(new Register(atRegisterId));
            if (null != register) {
                String url = ParaConfController.paraConfController.findUrlByKey("URL_ROOT_PATH");
                if (StringUtils.isNotEmpty(url)) {
                    register.setBgImageUrl(url+register.getBgImageUrl());
                }
                return ResultUtil.successResponse(register);
            }
            return ResultUtil.errorResponse(ResultEnums.NO_DATA_ERROR);
        }
        return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
    }

    /**
     *  添加活动
     * @param register
     * @return
     */
    @PostMapping("/insert")
    @ApiOperation(value = "添加活动", notes = "传入：活动分类id（atCtgyId），活动名称（name），活动开始时间（beginTime）" +
            "，活动结束时间（endTime），报名开始时间（applyBeginTime），报名结束时间（applyEndTime），活动海报文件（fileName）" +
            "，活动内容描述（content），添加系统日志的主体（LogContent）")
    public ResponseEntity<Result> insert(Register register, HttpServletRequest request) throws IOException {
        if (StringUtils.isNotEmpty(register.getName()) && StringUtils.isNotEmpty(register.getAtCtgyId())
                && StringUtils.isNotEmpty(register.getContent()) && null != register.getBeginTime()
                && null != register.getEndTime() && null != register.getApplyBeginTime() && null != register.getApplyEndTime()) {
            RegisterClassify classify = new RegisterClassify(register.getAtCtgyId());
            if (null != classify) {
                String pictureUrl = upload(request);
                if (null == pictureUrl) {
                    return ResultUtil.errorResponse(ResultEnums.FILE_ERROR);
                }
                //判断文件格式是否正常
                if (pictureUrl.equals("1")) {
                    return ResultUtil.errorResponse(ResultEnums.FILE_FORMAT_ERROR);
                }
                register.setBgImageUrl(pictureUrl);
                if (registerService.insert(register) > 0) {
                    return ResultUtil.successResponse();
                }
                return ResultUtil.errorResponse(ResultEnums.ADDITION_FAILED);
            }
            return ResultUtil.errorResponse(ResultEnums.DATA_RELEVANCE_ERROR);
        }
        return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
    }

    /**
     *  上传图片
     * @param request
     * @return
     */
    public String upload(HttpServletRequest request) throws IOException {
        List<MultipartFile> file1 = new ArrayList<MultipartFile>();
        if (request instanceof MultipartHttpServletRequest) {
            file1 = ((MultipartHttpServletRequest) request).getFiles("fileName");
        }
        if (file1.get(0).getSize() > 0) {
            return handleFile.saveFile(file1.get(0) ,"valentineDay");
        }
//        String fileName = file1.get(0).getOriginalFilename();
//        //字母转小写
//        fileName.toLowerCase();
//        if (fileName.contains(".jpg") || fileName.contains(".png") || fileName.contains(".gif")) {
//            return handleFile.saveFile(file1.get(0) ,"valentineDay");
//        }
        return "1";
    }

    /**
     *  删除活动
     * @param register
     * @return
     */
    @PostMapping("/delete")
    @ApiOperation(value = "删除活动", notes = "传入：活动登记id（atRegisterId），添加系统日志的主体（LogContent）")
    public ResponseEntity<Result> delete(Register register) {
        if (StringUtils.isNotEmpty(register.getAtRegisterId())) {
            if (registerService.delete(register) > 0) {
                return ResultUtil.successResponse();
            }
            return ResultUtil.errorResponse(ResultEnums.ADDITION_FAILED);
        }
        return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
    }

    /**
     *  更新活动
     * @param register
     * @return
     */
    @PostMapping("/update")
    @ApiOperation(value = "更新活动", notes = "传入：活动登记id（atRegisterId），活动分类id（atCtgyId），活动名称（name），活动开始时间（beginTime）" +
            "，活动结束时间（endTime），报名开始时间（applyBeginTime），报名结束时间（applyEndTime），活动海报文件（fileName）,是否上传图片文件(pictureTip)" +
            "，活动内容描述（content），添加系统日志的主体（LogContent）")
    @ApiImplicitParam(name = "pictureTip",value = "是否上传图片文件", dataType = "Long", paramType = "query", example = "1")
    public ResponseEntity<Result> update(Register register, HttpServletRequest request, Integer pictureTip) throws IOException {
        if (StringUtils.isNotEmpty(register.getName()) && StringUtils.isNotEmpty(register.getAtCtgyId())
                && StringUtils.isNotEmpty(register.getAtRegisterId()) && StringUtils.isNotEmpty(register.getContent())
                && null != register.getBeginTime()
                && null != register.getEndTime() && null != register.getApplyBeginTime() && null != register.getApplyEndTime()) {
            Register register1 = registerService.selectOne(new Register(register.getAtRegisterId()));
            RegisterClassify classify = registerClassifyService.selectOne(new RegisterClassify(register.getAtCtgyId()));
            if (null != register1 && null != classify) {
                if (pictureTip == 1) {
                    String pictureUrl = upload(request);
                    if (null != pictureUrl && !pictureUrl.equals("1")) {
                        register1.setBgImageUrl(pictureUrl);
                    }
                }
                register1.setName(register.getName());
                register1.setApplyBeginTime(register.getApplyBeginTime());
                register1.setApplyEndTime(register.getApplyEndTime());
                register1.setEndTime(register.getEndTime());
                register1.setBeginTime(register.getBeginTime());
                register1.setContent(register.getContent());
                register1.setAtCtgyId(register.getAtCtgyId());
                if (registerService.update(register1) > 0) {
                    return ResultUtil.successResponse();
                }
                return ResultUtil.errorResponse(ResultEnums.ADDITION_FAILED);
            }
            return ResultUtil.errorResponse(ResultEnums.NO_DATA_ERROR);
        }
        return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
    }

    /**
     *  根据活动查询所有用户信息
     * @return
     */
    @PostMapping("/conditionQueryActivity")
    @ApiOperation(value = "根据活动查询所有活动报名用户信息", notes = "传入：页码（nowPage必传），报名者姓名（name），电话（phoneNumber）" +
            "，性别（sex），报名者身份（identity），匹配编号（matchNo）")
    @ApiImplicitParam(name = "nowPage", value = "页码", required = true, dataType = "Long", paramType = "query",example = "1")
    public ResponseEntity<Result> conditionQueryActivity(Activity activity, Integer nowPage) {
        if (null != nowPage && nowPage > 0 && StringUtils.isNotEmpty(activity.getAtRegisterId())) {
            Integer pageSize = 10;
            Integer start = (nowPage - 1) * pageSize;
            if (StringUtils.isEmpty(activity.getMatchNo()))
                activity.setMatchNo(null);
            List<Activity> activityList = activityService.conditionQueryActivity(activity, pageSize, start);
            if (activityList.size() > 0) {
                String url = ParaConfController.paraConfController.findUrlByKey("URL_ROOT_PATH");
                for (Activity act: activityList
                     ) {
                    act.setShareScreenshotUrl(url + act.getShareScreenshotUrl());
                    act.setPhotoUrl(url+act.getPhotoUrl());
                }
                return ResultUtil.successResponse(activityList);
            }
            return ResultUtil.errorResponse(ResultEnums.NO_DATA_ERROR);
        }
        return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
    }

    /**
     *  统计数量
     * @param activity
     * @return
     */
    @PostMapping("/count-activity")
    @ApiOperation(value = "统计数量", notes = "传入：报名者姓名（name），电话（phoneNumber）" +
            "，性别（sex），报名者身份（identity），匹配编号（matchNo）")
    public ResponseEntity<Result> countActivity(Activity activity) {
        if (StringUtils.isEmpty(activity.getMatchNo()))
            activity.setMatchNo(null);
        return ResultUtil.successResponse(activityService.count(activity));
    }

    /**
     *  根据编号查询活动用户
     * @param activityId
     * @return
     */
    @PostMapping("/findByActivityId")
    @ApiOperation(value = "根据编号查询活动用户", notes = "传入：报名用户编号（activityId)(必传)")
    @ApiImplicitParam(name = "activityId", value = "报名用户编号", required = true, dataType = "String", paramType = "query")
    public ResponseEntity<Result> findByActivityId(String activityId) {
        if (StringUtils.isNotEmpty(activityId)) {
            Activity activity = activityService.findActivity(activityId);
            if (null != activity) {
                String url = ParaConfController.paraConfController.findUrlByKey("URL_ROOT_PATH");
                if (StringUtils.isNotEmpty(url)) {
                    activity.setShareScreenshotUrl(url+activity.getShareScreenshotUrl());
                    activity.setPhotoUrl(url+activity.getPhotoUrl());
                }
                return ResultUtil.successResponse(activity);
            }
            return ResultUtil.errorResponse(ResultEnums.NO_DATA_ERROR);
        }
        return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
    }

    /**
     *  导出报表
     * @param registerId
     * @param response
     * @return
     */
    @GetMapping("/exportExcel")
    @ApiOperation(value = "根据编号导出报表", notes = "传入：活动编号（registerId)(必传)")
    @ApiImplicitParam(name = "registerId", value = "活动编号", required = true, dataType = "String", paramType = "query")
    public void exportExcel(String registerId,HttpServletResponse response) {
        if (StringUtils.isEmpty(registerId))
            throw new MyException(ResultEnums.INCOMPLETE_INFORMATION);
        //获取数据
        Activity activity = new Activity();
        activity.setAtRegisterId(registerId);
        List<Activity> list = activityService.findAllByRegisterId(activity);
        String url = ParaConfController.paraConfController.findUrlByKey("URL_ROOT_PATH");
        if (list.size() > 0) {
            //excel标题
            String[] title = {"id","姓名","性别","年龄","身份","学校/学历","身高","体重","情感经历","性格类型","分享截图","电话","微信","自我备注",
                    "要求的性别","要求的身高","要求的体重","要求的年龄","要求的情感经历","要求的性格类型","其他要求","编号"};
            //excel文件名
            String fileName = "学生信息表"+System.currentTimeMillis()+".xls";
            //sheet名
            String sheetName = "学生信息表";
            String[][] content = new String[list.size()][];
            Integer size = list.size();
            for (int i = 0; i < size; i++) {
                content[i] = new String[title.length];
                Activity obj = list.get(i);
                content[i][0] = obj.getAcUserId();
                content[i][1] = obj.getName();
                if (obj.getSex() == 1) {
                    content[i][2] = "女";
                } else {
                    content[i][2] = "男";
                }
                content[i][3] = String.valueOf(obj.getAge());
                content[i][4] = obj.getIdentity();
                if (obj.getIdentity().equals("学生")) {
                    content[i][5] = obj.getBdUniversityId();
                } else {
                    content[i][5] = obj.getBdDegreesId();
                }
                content[i][6] = String.valueOf(obj.getHeight());
                content[i][7] = String.valueOf(obj.getWeight());
                content[i][8] = obj.getLoveHistory();
                content[i][9] = obj.getCharacterType();
                content[i][10] = url + obj.getShareScreenshotUrl();
                content[i][11] = obj.getPhoneNumber();
                content[i][12] = obj.getWechat();
                content[i][13] = obj.getRemark();
                content[i][14] = obj.getWantSex();
                content[i][15] = obj.getWantHeight();
                content[i][16] = obj.getWantWeight();
                content[i][17] = obj.getWantAge();
                content[i][18] = obj.getWantLoveHistory();
                content[i][19] = obj.getWantCharacterType();
                content[i][20] = obj.getWantOther();
                if (null == obj.getMatchNo()) {
                    content[i][21] = "未匹配";
                } else {
                    content[i][21] = obj.getMatchNo();
                }
            }
            //下载Excel
            DataToExcel.downloadExcel(sheetName, title, content, fileName, response);
        }
    }

    /**
     *  活动短信发送
     * @return
     */
    @PostMapping("/sendTextMessage")
    @ApiOperation(value = "活动短信发送", notes = "传入：活动编号（registerId)(必传)")
    @ApiImplicitParam(name = "registerId", value = "活动编号", required = true, dataType = "String", paramType = "query")
    public ResponseEntity<Result> sendTextMessage(String registerId) {
        if (StringUtils.isNotEmpty(registerId)) {
            thinkTime(registerId);
            List<Activity> activityList = activityService.selectListLimit(new Activity(registerId,1), 1000);
            if (activityList.size() > 0) {
                if (activityService.updateStatus(activityList, 2) > 0)
                    return ResultUtil.successResponse();
                return ResultUtil.errorResponse(ResultEnums.PORTION_SMS_CODE);
            }
            return ResultUtil.errorResponse(ResultEnums.NO_DATA_ERROR);
        }
        return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
    }

    /**
     * 群组通知短信
     * @param registerId
     * @return
     */
    @PostMapping("/sendGroupMessage")
    @ApiOperation(value = "群组通知短信", notes = "传入：活动编号（registerId)(必传)")
    @ApiImplicitParam(name = "registerId", value = "活动编号", required = true, dataType = "String", paramType = "query")
    public ResponseEntity<Result> sendGroupMessage(String registerId) {
        if (StringUtils.isEmpty(registerId))
            return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
        thinkTime(registerId);
        activityService.findByRegisterIdAndLimit(registerId);
        return ResultUtil.successResponse();
    }

    /**
     *  更换cp
     * @param registerId 活动编号
     * @return
     */
    @PostMapping("/changeCp")
    @ApiOperation(value = "更换cp", notes = "传入：活动编号（registerId)(必传)")
    @ApiImplicitParam(name = "registerId", value = "活动编号", required = true, dataType = "String", paramType = "query")
    public ResponseEntity<Result> changeCp(String registerId) {
        activityService.updateStatusByConfirmCp(registerId);
        return matchMate(registerId, 2);
    }

    /**
     * 统一匹配
     * @param registerId 活动编号
     * @return
     */
    @PostMapping("/findBySexAndWantSex")
    @ApiOperation(value = "统一匹配", notes = "传入：活动编号（registerId)(必传)")
    @ApiImplicitParam(name = "registerId", value = "活动编号", required = true, dataType = "String", paramType = "query")
    public ResponseEntity<Result> findBySexAndWantSex(String registerId) {
        //初始化活动数据
        activityService.updateByRegisterIdOrStatus(registerId);
        return matchMate(registerId, 1);
    }

    /**
     * 匹配
     * @param registerId 活动编号
     * @param type 类型，2代表更换cp
     * @return
     */
    public ResponseEntity<Result> matchMate(String registerId, Integer type) {
        thinkTime(registerId);
        activityService.updateMatchNoByWoman(registerId, type);
        return ResultUtil.successResponse();
    }

    /**
     *  判断是否在某个时间内(报名结束后到活动开始，包含开始当天)
     * @param registerId
     */
    public void thinkTime(String registerId) {
        Register register = registerService.selectOne(new Register(registerId));
        if (null == register)
            throw new MyException(ResultEnums.NO_DATA_ERROR);
        Date data = new Date();
        //活动未开始
        if (DateUtils.dateDiff(register.getApplyEndTime(), data) < 0) {
            throw new MyException(ResultEnums.ACTIVITY_NOT_STARTED);
        }
        //活动已结束
        if (DateUtils.dateDiff(DateUtils.getDayEndTime(register.getBeginTime()), data) > 0) {
            throw new MyException(ResultEnums.ACTIVITY_OVER);
        }
    }

    /**
     *  解散传入活动编号的下属环信群聊
     * @param registerId
     * @return
     */
    @PostMapping("/deleteGroup")
    @ApiOperation(value = "解散传入活动编号的下属环信群聊", notes = "传入：活动编号（registerId)(必传)")
    @ApiImplicitParam(name = "registerId", value = "活动编号", required = true, dataType = "String", paramType = "query")
    public ResponseEntity<Result> deleteGroup(String registerId) {
        if (StringUtils.isEmpty(registerId))
            return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
        Register register = registerService.selectOne(new Register(registerId));
        if (null == register)
            return ResultUtil.errorResponse(ResultEnums.NO_DATA_ERROR);
        if (DateUtils.dateDiff(register.getEndTime(), new Date()) < 0)
            return ResultUtil.errorResponse(ResultEnums.ACTIVITY_NOT_OVER);
        List<ImGroup> groupList = imGroupService.selectList(new ImGroup(0,"IGBT_WEEKCP",registerId,1));
        List<ImGroup> list = imGroupService.selectList(new ImGroup(2,"IGBT_WEEKCP",registerId,1));
        if (groupList.size() > 0 || list.size() > 0) {
            if (groupList.size() > 0) {
                for (ImGroup group: groupList
                ) {
                    if (!huanXinService.deleteGroup(group.getHxGroupId())) {
                        return ResultUtil.errorResponse(ResultEnums.HUAN_XIN_GROUP_DISSOLVE_ERROR);
                    }
                }
            }
            if (list.size() > 0) {
                for (ImGroup group: list
                ) {
                    if (!huanXinService.deleteGroup(group.getHxGroupId()))
                        return ResultUtil.errorResponse(ResultEnums.HUAN_XIN_GROUP_DISSOLVE_ERROR);
                }
            }
            return ResultUtil.successResponse();
        }
        return ResultUtil.errorResponse(ResultEnums.NO_DATA_ERROR);
    }

    public void deleteImGroup(List<ImGroup> groupList) {
        for (ImGroup group: groupList
        ) {
            System.out.println(group.getHxGroupId() + "--------------------");
            if (!huanXinService.deleteGroup(group.getHxGroupId()))
                throw new MyException(ResultEnums.HUAN_XIN_GROUP_DISSOLVE_ERROR);
        }
    }

    @PostMapping("/deleteCp")
    @ApiOperation(value = "根据活动编号删除cp关系", notes = "传入：活动编号（registerId)(必传)")
    @ApiImplicitParam(name = "registerId", value = "活动编号", required = true, dataType = "String", paramType = "query")
    public ResponseEntity<Result> deleteCp(String registerId) {
        cpRelService.delete(registerId);
        return ResultUtil.successResponse();
    }

    /**
     *  建立环信群聊天和cp聊天室
     * @param registerId
     * @return
     */
    @PostMapping("/establishGroup")
    @ApiOperation(value = "根据活动建立环信群聊天和cp聊天室", notes = "传入：活动编号（registerId)(必传)")
    @ApiImplicitParam(name = "registerId", value = "活动编号", required = true, dataType = "String", paramType = "query")
    public ResponseEntity<Result> chatRoom(String registerId) {
        if (StringUtils.isEmpty(registerId))
            return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
        thinkTime(registerId);
        activityService.chatRoom(registerId);
        return ResultUtil.successResponse();
    }

    /**
     *  建立环信群聊天和cp聊天室
     * @param registerId 活动编号
     * @return
     */
//    @PostMapping("/establishGroup")
//    public ResponseEntity<Result> establishGroup(String registerId) {
//        if (StringUtils.isEmpty(registerId))
//            return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
//        List<String> matchNoList = activityService.findMatchNoByRegisterId(registerId);
//        Map<String, String> map = new HashMap<>();
//        if (matchNoList.size() > 0) {
//            for (String matchNo: matchNoList
//                 ) {
//                String[] letter = matchNo.split(":");
//                if (!map.containsKey(letter[0])) {
//                    huanXinGroup(letter[0], registerId);
//                    map.put(letter[0],letter[0]);
//
//                }
//            }
//            return ResultUtil.successResponse();
//        }
//        return ResultUtil.errorResponse(ResultEnums.NO_DATA_ERROR);
//    }
//
//    /**
//     *  添加环信群组和群组成员
//     * @param grouping 群组字母编号
//     * @param registerId 活动id
//     * @return
//     */
//    public Boolean huanXinGroup(String grouping, String registerId) {
//        List<Activity> list = activityService.findByMatchNoAndStatus(grouping, registerId);
//        List<Activity> copyList = list;//复制一份数据便于比较编号相同的用户建立cp房间
////        Map<String, List<String>> activityMap = new HashMap<>();//临时存储60对cp
////        Dictionary dictionary = new Dictionary();
////        dictionary.setItemKey("SYSTEM_USER_ONE");
//        AcUser acUser = new AcUser();
//        acUser.setType(1);
//        String ownerId = acUserService.selectList(acUser).get(0).getImUserId();
//        String groupName = registerService.selectOne(new Register(registerId)).getName() + grouping + "群";//群名
//        Integer maxUsers = Integer.valueOf(ParaConfController.paraConfController.findUrlByKey("HUAN_XIN_GROUP_MAX"));
//        Integer activityMapSize = 0;
//        Integer listSize = list.size();
//        List imUserIdList = new ArrayList<>();
//        for (Activity act: list
//        ) {
//            if (activityMapSize%59 == 0) {
////                List<String> imUserIdList = new ArrayList<>();
////                imUserIdList.add(act.getImUserId());
////                activityMap.put("groupMember", imUserIdList);
//                if (activityMapSize == 0) {
//                    ImGroup imGroup = imGroupService.selectOne(new ImGroup(groupName, "IGBT_WEEKCP", registerId, 1, 0));
//                    if (null != imGroup)
//                        throw new MyException(ResultEnums.HUAN_XIN_GROUP_EXIST);
//                    huanXinService.createChatGroup(ownerId, groupName, groupName, true, maxUsers,
//                            false, new ArrayList<>(), "IGBT_WEEKCP", registerId, null, 0);
//                } else {
//                    ImGroup imGroup = imGroupService.selectOne(new ImGroup(groupName, "IGBT_WEEKCP", registerId, 1, 0));
//                    if (null == imGroup)
//                        throw new MyException(ResultEnums.HUAN_XIN_GROUP_ERROR);
////                    huanXinService.addMemberInGroup(activityMap.get("groupMember"), imGroup.getHxGroupId());
//                    huanXinService.addMemberInGroup(imUserIdList, imGroup.getHxGroupId());
//                    imUserIdList = new ArrayList();
//                }
//                imUserIdList.add(act.getImUserId());
//            } else {
////                activityMap.get("groupMember").add(act.getImUserId());
//                imUserIdList.add(act.getImUserId());
//                if (listSize == activityMapSize + 1) {
//                    ImGroup imGroup = imGroupService.selectOne(new ImGroup(groupName, "IGBT_WEEKCP", registerId, 1, 0));
//                    if (null == imGroup)
//                        throw new MyException(ResultEnums.HUAN_XIN_GROUP_ERROR);
////                    huanXinService.addMemberInGroup(activityMap.get("groupMember"), imGroup.getHxGroupId());
//                    huanXinService.addMemberInGroup(imUserIdList, imGroup.getHxGroupId());
//                }
//            }
//            for (Activity activity: copyList
//                 ) {
//                if (activity.getMatchNo().equals(act.getMatchNo()) && !act.getAtWkcpUserId().equals(activity.getAtWkcpUserId())) {
//                    ImGroup imGroup = imGroupService.selectOne(new ImGroup("IGBT_WEEKCP", registerId, 1, activity.getMatchNo()));
//                    if (null == imGroup) {
//                        List<String> members = new ArrayList<>();
//                        members.add(act.getImUserId());
//                        members.add(activity.getImUserId());
//                        huanXinService.createChatGroup(ownerId, act.getName()+"&"+activity.getName()+"的CP房间", "CP", true, 200,
//                                false, members, "IGBT_WEEKCP", registerId, activity.getMatchNo(), 1);
//                        List<String> target = new ArrayList<>();
//                        ImGroup group = imGroupService.selectOne(new ImGroup("IGBT_WEEKCP", registerId, 1, activity.getMatchNo()));
//                        target.add(group.getHxGroupId());
//                        huanXinService.sendMessage("chatgroups", target, "txt", "您已经进入："+act.getName()+"&"+activity.getName()+"的CP房间", "一周情侣工作人员");
//                    }
//                    break;
//                }
//            }
//            activityMapSize++;
//        }
//        List<String> target = new ArrayList<>();
//        ImGroup imGroup = imGroupService.selectOne(new ImGroup(groupName, "IGBT_WEEKCP", registerId, 1, 0));
//        target.add(imGroup.getHxGroupId());
//        huanXinService.sendMessage("chatgroups", target, "txt", "您已经进入："+ groupName, "一周情侣工作人员");
//        return true;
//    }

    /**
     *  建群
     * @param registerId
     * @return
     */
    @PostMapping("/colonization")
    public ResponseEntity<Result> colonization(String registerId) {
        if (StringUtils.isEmpty(registerId))
            return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
        List<Activity> activityList = activityService.findByRegisterIdAndGroup(registerId);
        Integer crowdNum = Integer.valueOf(ParaConfController.paraConfController.findUrlByKey("MAXIMUM_NUMBER_OF_AUXILIARY_GROUP"));
        if (activityList.size() < crowdNum/2)
            return ResultUtil.errorResponse(ResultEnums.LESS_THAN_HELF);
        if (activityList.size() > 0) {
//            Map<String, List<Activity>> manMap = new HashMap<>();//男生
//            Map<String, List<Activity>> womanMap = new HashMap<>();//女生
            List<Activity> manList = new ArrayList<>();
            List<Activity> womanList = new ArrayList<>();
            for (Activity activity: activityList
                 ) {
                if (activity.getSex() == 1) {
//                    setMap(activity, womanMap);
                    womanList.add(activity);
                } else {
//                    setMap(activity, manMap);
                    manList.add(activity);
                }
            }
            Integer groupNum = activityList.size()/crowdNum;
            if (groupNum == 0) {
                groupNum =1;
            } else if (activityList.size()%crowdNum > crowdNum/2) {
                groupNum++;
            }
            AcUser acUser = new AcUser();
//        acUser.setNo(dictionaryService.selectOne(dictionary).getItemValue());
            acUser.setType(1);
            String ownerId = acUserService.selectList(acUser).get(0).getImUserId();
            String groupName = "意难忘畅聊";
            String[] matchNo = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
            Integer womanSize = womanList.size();//女生人数
            Integer womanNum = womanSize/groupNum;//每个群女生人数
            Map<String, List<String>> members = new HashMap<>();//放入每个群组成员
            Integer num = 1;//群编号
            for (Integer i = 0; i < womanSize; i++
                 ) {
                if (i % womanNum == 0) {
                    List<String> list = new ArrayList<>();
                    list.add(womanList.get(i).getImUserId());
                    if (i != 0)
                        num++;
                    if (num > groupNum) {
                        members.get(groupName + 1).add(womanList.get(i).getImUserId());
                    } else {
                        members.put(groupName + num, list);
                    }
                } else {
                    if (num > groupNum) {
                        members.get(groupName + 1).add(womanList.get(i).getImUserId());
                    } else {
                        members.get(groupName + num).add(womanList.get(i).getImUserId());
                    }
                }
            }
            num = 1;
            for (Activity activity: manList
                 ) {
                //判断key是否存在
                if (members.containsKey(groupName + num)) {
                    if (members.get(groupName+num).size() == crowdNum) {
                        num ++;
                        //判断key是否存在
                        if (members.containsKey(groupName + num)) {
                            members.get(groupName + num).add(activity.getImUserId());
                        } else {
                            members.get(groupName + 1).add(activity.getImUserId());
                        }
                    } else {
                        members.get(groupName + num).add(activity.getImUserId());
                    }
                } else {
                    members.get(groupName + 1).add(activity.getImUserId());
                }
            }
            List<ImGroup> groupList = imGroupService.findNowByRegisterId(new ImGroup("IGBT_WEEKCP", registerId, 1));
            Integer matchNoSize = 0;
            Integer oldGroupNum = 1;
            if (groupList.size() != 0) {
                String[] strings = groupList.get(0).getMatchNo().split(" ");
                for (; matchNoSize > 26;matchNoSize ++
                ) {
                    if (matchNo[matchNoSize].equals(strings[0])) {
                        break;
                    }
                }
                Integer letterNum = Integer.valueOf(ParaConfController.paraConfController.findUrlByKey("MAX_GROUP_NUMBER"));//每个字母的最大数字
                oldGroupNum = Integer.valueOf(strings[1]);
                if (letterNum <= oldGroupNum) {
                    matchNoSize++;
                    oldGroupNum = 1;
                } else {
                    oldGroupNum++;
                }
            } else {

            }
            for (Integer i = 0; i < groupNum; i++) {
                huanXinService.createChatGroup(ownerId, groupName + matchNo[matchNoSize] + oldGroupNum + "群", groupName, true, crowdNum * 2,
                        false, members.get(groupName+(i+1)), "IGBT_WEEKCP", registerId, matchNo[matchNoSize] +" " + oldGroupNum, 2);
                List<String> target = new ArrayList<>();
                ImGroup imGroup = imGroupService.selectOne(new ImGroup(groupName + matchNo[matchNoSize] + oldGroupNum + "群", "IGBT_WEEKCP", registerId, 1, 2));
                target.add(imGroup.getHxGroupId());
                huanXinService.sendMessage("chatgroups", target, "txt", "您已经进入："+ groupName + matchNo[matchNoSize] + oldGroupNum + "群", "一周情侣工作人员");
                oldGroupNum++;
            }
            return ResultUtil.successResponse();
        }
        return ResultUtil.errorResponse(ResultEnums.NO_DATA_ERROR);
    }

    /**
     *  赋值
     * @param activity
     * @param map
     */
    public void setMap(Activity activity, Map<String, List<Activity>> map) {
        if (map.containsKey(DateUtils.dateString(activity.getCreateTime()))) {
            map.get(DateUtils.dateString(activity.getCreateTime())).add(activity);
        } else {
            List<Activity> list = new ArrayList<>();
            list.add(activity);
            map.put(DateUtils.dateString(activity.getCreateTime()),list);
        }
    }

    /**
     *  加群
     * @param groupId 群groupId
     * @param imUserId 加群成员的imUserId（逗号隔开可传多个用户,不超过60人）
     * @return
     */
    @PostMapping("/addGroup")
    @ApiOperation(value = "加群", notes = "传入：群groupId（groupId)),加群成员的imUserId（逗号隔开可传多个用户,不超过60人）（imUserId)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "groupId", value = "群groupId", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "imUserId", value = "加群成员的imUserId（逗号隔开可传多个用户,不超过60人）", required = true, dataType = "String", paramType = "query")
    })
    public ResponseEntity<Result> addGroup(String groupId,String imUserId) {
        //逗号隔开转成list
        List<String> list = Arrays.asList(imUserId.split(","));
        if (huanXinService.addMemberInGroup(list, groupId)) {
            AcUser acUser = new AcUser();
            String name = "";
            Integer listSize = list.size();
            for (Integer i = 0; i < listSize;i++
                 ) {
                acUser.setImUserId(list.get(i));
                name += acUserService.selectOne(acUser).getNickname();
                if (i != listSize-1)
                    name += ",";
            }
            List<String> groupList = new ArrayList<>();
            groupList.add(groupId);
            if (huanXinService.sendMessage("chatgroups", groupList, "txt", "欢迎"+ name + "加入本群！！！", "一周情侣工作人员"))
                return ResultUtil.successResponse();
        }
        return ResultUtil.errorResponse(ResultEnums.USER_ERROR);
    }

    /**
     *  退群
     * @param members
     * @param groupId
     * @return
     */
    @PostMapping("/removeOutGroup")
    @ApiOperation(value = "退群", notes = "传入：群groupId（groupId)),退群成员的imUserId（逗号隔开可传多个用户,不超过60人）（members))")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "groupId", value = "群groupId", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "members", value = "退群成员的imUserId（逗号隔开可传多个用户,不超过60人）", required = true, dataType = "String", paramType = "query")
    })
    public ResponseEntity<Result> removeOutGroup(String members,String groupId) {
        if (StringUtils.isEmpty(members) || StringUtils.isEmpty(groupId))
            return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
        if (huanXinService.removeOutGroup(members, groupId))
            return ResultUtil.successResponse();
        return ResultUtil.errorResponse(ResultEnums.ADDITION_FAILED);
    }

    /**
     *  群组消息推送
     * @param registerId 活动编号
     * @param msg 消息内容
     * @return
     */
    @PostMapping("/findAll")
    @ApiOperation(value = "群组消息推送", notes = "传入：活动编号（registerId)),消息内容（msg))")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "registerId", value = "活动编号", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "msg", value = "消息内容", required = true, dataType = "String", paramType = "query")
    })
    public ResponseEntity<Result> findAll(String registerId, String msg) {
        if (StringUtils.isEmpty(registerId))
            return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
        ImGroup imGroup = new ImGroup();
        imGroup.setBusinessId(registerId);
        imGroup.setStatus(1);
        imGroup.setBusinessType("IGBT_WEEKCP");
        List<String> groupList = imGroupService.findAllByRegisterId(imGroup);
//        List<String> groupList = new ArrayList<>();
//        groupList.add("78337674379267");
        if (groupList.size() > 0) {
            if (huanXinService.sendMessage("chatgroups", groupList, "txt", msg, "一周情侣工作人员"))
                return ResultUtil.successResponse(groupList);
        }

        return ResultUtil.errorResponse(ResultEnums.NO_DATA_ERROR);
    }

//    @PostMapping("/matchingBatchUpdate")
//    public ResponseEntity<Result> matchingBatchUpdate() {
//        List<Activity> list = new ArrayList<>();
//        list.add(new Activity("dc5778f8ce8243b20190411145148390","A:200"));
//        list.add(new Activity("5ab6b4b554834c22019041116030243","A:1000"));
//        return ResultUtil.successResponse(activityService.matchingBatchUpdate(list));
//    }

//    @PostMapping("/photoTest")
//    public ResponseEntity<Result> photoTest(HttpServletRequest request,HttpServletResponse response) throws IOException {
//        upload(request,"photoFileName");
//        //跨域返回
//        response.setHeader("Access-Control-Allow-Methods", "POST, GET, DELETE, OPTIONS, DELETE");//当判定为预检请求后，设定允许请求的方法
//        response.setHeader("Access-Control-Allow-Headers", "Content-Type, x-requested-with, Token"); //当判定为预检请求后，设定允许请求的头部类型
//        response.addHeader("Access-Control-Max-Age", "1");
//        response.addHeader("Access-Control-Allow-Origin", "*");
//        return ResultUtil.successResponse();
//    }

    /**
     *  上传图片
     * @param request
     * @return
     */
    public String upload(HttpServletRequest request, String fileName) throws IOException {
        List<MultipartFile> file1 = new ArrayList<MultipartFile>();
        if (request instanceof MultipartHttpServletRequest) {
            file1 = ((MultipartHttpServletRequest) request).getFiles(fileName);
        }
        return handleFile.saveFile(file1.get(0), "photo");
    }

}

