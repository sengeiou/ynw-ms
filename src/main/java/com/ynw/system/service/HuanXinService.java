package com.ynw.system.service;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.exceptions.ClientException;
import com.ynw.system.dao.ImGroupMapper;
import com.ynw.system.dao.ImGroupMemberMapper;
import com.ynw.system.entity.AcUser;
import com.ynw.system.entity.ImGroup;
import com.ynw.system.entity.ImGroupMember;
import com.ynw.system.entity.ImUser;
import com.ynw.system.enums.ResultEnums;
import com.ynw.system.exception.MyException;
import com.ynw.system.util.RedisService;
import com.ynw.system.util.SendSmsUtil;
import com.ynw.system.util.UUIDUtil;
import io.netty.util.internal.StringUtil;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class HuanXinService {
    private String org_name="1104181206084925";
    private String appname="4a2qrdav4f";
    @Resource
    private AcUserService acUserService;
    @Resource
    private RestTemplate restTemplate;
    @Resource
    private RedisService redisService;
    @Resource
    private ImGroupMapper imGroupMapper;
    @Resource
    private ImGroupMemberMapper imGroupMemberMapper;

    private HttpHeaders httpHeaders=new HttpHeaders();

    private  String body="{'grant_type':'client_credentials','client_id':'YXA6iw1mkPkZEeikVzd-jEn4RA','client_secret':'YXA6hK0v5PPAOWen-jxie6LznIJx5Vs'}";

    /**
     * 注册im用户并将其与用户进行绑定
     * @param phoneNumber
     * @return
     * @throws Exception
     */
    public String insertIm(String phoneNumber)  {
        AcUser user = new AcUser();
        user.setPhoneNumber(phoneNumber);
        AcUser acUser= acUserService.selectOne(user);//根据手机号
        if(null != acUser.getImUserId())//如果已经注册则不注册
            return acUser.getImUserId();
        //body
        JSONObject requestBody = JSONObject.parseObject(JSONObject.toJSONString(new ImUser(acUser.getNickname(),acUser.getPhoneNumber())));
        //HttpEntity
        HttpEntity<JSONObject> requestEntity = new HttpEntity<>(requestBody, httpHeaders);
        //post
        String url="http://a1.easemob.com/"+org_name+"/"+appname+"/users";
        try {
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, requestEntity, String.class);
        }catch(HttpClientErrorException.BadRequest badRequest){
            putUserNickname(acUser.getPhoneNumber(),acUser.getNickname());
            return updateImUser(acUser);
        }

        return updateImUser(acUser);

    }

    /**
     *  更新用户的imUserId
     * @param acUser
     * @return
     */
    private String updateImUser(AcUser acUser) {
        AcUser user=new AcUser();
        user.setAcUserId(acUser.getAcUserId());
        AcUser user1 = acUserService.findById(user);
        if (null == user1)
            throw new MyException(ResultEnums.NO_DATA_ERROR);
        user1.setImUserId(acUser.getPhoneNumber());
        acUserService.updateByPrimaryKey(user1);
        return acUser.getPhoneNumber();
    }

    /**
     * 获取环信的token
     * @return
     */
     public String getToken(){
         if(StringUtil.isNullOrEmpty((String) redisService.get("HuanxinToken"))) {
             JSONObject requestBody = JSONObject.parseObject(body);
             //HttpEntity
             HttpEntity<JSONObject> requestEntity = new HttpEntity<>(requestBody, httpHeaders);
             HttpEntity httpEntity = restTemplate.postForEntity("http://a1.easemob.com/" + org_name + "/" + appname + "/token", requestEntity, String.class);
             if (((ResponseEntity) httpEntity).getStatusCode().is2xxSuccessful()) {
                 String token = (String) httpEntity.getBody();
                 Map<String, Object> map = JSONObject.parseObject(token, Map.class);
                 Integer exp=(int)map.get("expires_in")-86400;//环信的过期时间为5184000秒,为60天 我们的过期时间提前一天
                 redisService.put("HuanxinToken", map.get("access_token"),exp);
                 return (String) map.get("access_token");
             } else if (((ResponseEntity) httpEntity).getStatusCode().is4xxClientError()) {
                 throw new MyException(ResultEnums.INCOMPLETE_INFORMATION);
             }
             return getToken();
         }
         return (String) redisService.get("HuanxinToken");
     }
    /**
     * 环信添加好友
     * @param acUserId 自己的用户名
     * @param friendId 要加的好友名
     * @return
     */
    public boolean addImFriends(String acUserId,String friendId){
        String acUserImId=acUserService.findById(new AcUser(acUserId)).getImUserId();
        String friendImId=acUserService.findById(new AcUser(friendId)).getImUserId();
        String url="http://a1.easemob.com/"+org_name+"/"+appname+"/users";
        url+="/"+acUserImId+"/contacts/users/"+friendImId;
        httpHeaders.set("Authorization","Bearer "+getToken());
        JSONObject requestBody =new JSONObject();
        //HttpEntity
        HttpEntity<JSONObject> requestEntity = new HttpEntity<>(requestBody, httpHeaders);
        try {


        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, requestEntity, String.class);
        }catch(HttpClientErrorException e){
        redisService.del("HuanxinToken");
        if (e instanceof HttpClientErrorException.Unauthorized)
        return addImFriends(acUserId,friendId);
        throw new MyException(ResultEnums.UNKNOWN_ERROR);
    }
            return true;
    }

    /**
     * 为用户移除好友
     * @param acUserId1
     * @param acUserId2
     * @return
     */
    public boolean removeImFriend(String acUserId1,String acUserId2){
        String url="http://a1.easemob.com/"+org_name+"/"+appname+"/users";
        url+="/"+acUserId1+"/contacts/users/"+acUserId2;
        httpHeaders.set("Authorization","Bearer "+getToken());
    //HttpEntity
        HttpEntity<MultiValueMap> requestEntity = new HttpEntity<>(null, httpHeaders);
        try {
            restTemplate.exchange(url, HttpMethod.DELETE, requestEntity,String.class);
        }catch(HttpClientErrorException e){
            redisService.del("HuanxinToken");
            if (e instanceof HttpClientErrorException.Unauthorized)
            return removeImFriend(acUserId1,acUserId2);
            throw new MyException(ResultEnums.UNKNOWN_ERROR);
        }
    return true;
}

    /**
     * 获取某个Im用户的好友列表
     * @param ImUserId
     * @return
     */
   public boolean getFriendList(String ImUserId){
       String url="http://a1.easemob.com/"+org_name+"/"+appname+"/users/"+ImUserId+"/contacts/users";
       httpHeaders.set("Authorization","Bearer "+getToken());
       JSONObject requestBody =new JSONObject();
       HttpEntity<JSONObject> requestEntity = new HttpEntity<>(requestBody, httpHeaders);
       try {
           ResponseEntity responseEntity = restTemplate.getForEntity(url, String.class, requestEntity);
       }catch(HttpClientErrorException e){
           redisService.del("HuanxinToken");
           if (e instanceof HttpClientErrorException.Unauthorized)
           return getFriendList(ImUserId);
           throw new MyException(ResultEnums.UNKNOWN_ERROR);
       }
       return true;
   }

    /**
     * 把批量用户加入群组黑名单
     * @param members
     * @param groupId
     * @return
     */
   public boolean addBlackList(List<String> members,String groupId){
       String url="http://a1.easemob.com/"+org_name+"/"+appname+"/chatgroups/"+groupId+"/blocks/users";

       HttpEntity<JSONObject> requestEntity=setContent(members);
       try {
           restTemplate.postForEntity(url, requestEntity, String.class);
       }catch (HttpClientErrorException e) {
           redisService.del("HuanxinToken");
           if (e instanceof HttpClientErrorException.Unauthorized)
          return addBlackList(members, groupId);
           throw e;
       }
       imGroupMemberMapper.updateBlackStatus(1, members,groupId);//把移除黑名单的人更改状态
       return  true;
   }

    private HttpEntity<JSONObject> setContent(List<String> members) {
        httpHeaders.set("Authorization","Bearer "+getToken());
        String content="{\n" +
                "  \"usernames\": "+members+"}";
        System.out.println(content);
        JSONObject requestBody =JSONObject.parseObject(content);
        return new HttpEntity<>(requestBody, httpHeaders);
    }

    private HttpEntity<JSONObject> setContent(String content) {
        httpHeaders.set("Authorization","Bearer "+getToken());
        JSONObject requestBody =JSONObject.parseObject(content);
        return new HttpEntity<>(requestBody, httpHeaders);
    }

    /**
     * 把批量用户从黑名单中删除
     * @param members
     * @param groupId
     * @return
     */
    public boolean removeBlackList(String members,String groupId){
        String url="http://a1.easemob.com/"+org_name+"/"+appname+"/chatgroups/"+groupId+"/blocks/users/"+members;
        httpHeaders.set("Authorization","Bearer "+getToken());
        //HttpEntity
        HttpEntity<MultiValueMap> requestEntity = new HttpEntity<MultiValueMap>(null, httpHeaders);
        try {
            restTemplate.exchange(url, HttpMethod.DELETE, requestEntity,String.class);
        }catch(HttpClientErrorException e){
            redisService.del("HuanxinToken");
            if (e instanceof HttpClientErrorException.Unauthorized)
            return removeBlackList(members,groupId);
            throw new MyException(ResultEnums.UNKNOWN_ERROR);

        }
        imGroupMemberMapper.updateBlackStatus(0, Arrays.asList(members.split(",")),groupId);//把移除黑名单的人更改状态
        return  true;
   }

    /**
     * 把某些用户加入到群组中来
     * @param members
     * @param groupId
     * @return
     */
   public boolean addMemberInGroup(List<String> members,String groupId){
       System.out.println(members.size()+"=========member=======");
       String url="http://a1.easemob.com/"+org_name+"/"+appname+"/chatgroups/"+groupId+"/users";

       try {
            restTemplate.postForEntity(url, setContent(members), String.class);
       }catch (HttpClientErrorException e) {
           redisService.del("HuanxinToken");
           if (e instanceof HttpClientErrorException.Unauthorized)
           addMemberInGroup(members, groupId);
           throw e;
       }
       ImGroup group = new ImGroup();
       group.setHxGroupId(groupId);
       ImGroup imGroup = imGroupMapper.selectOne(group);
       members.forEach(s -> imGroupMemberMapper.insertMember(new ImGroupMember(imGroup.getImGroupId(),s, 0)));//插入到群组成员表中去
       return  true;
   }
    /**
     * 把某些成员从群组中删除
     * @param members 多个成员,用逗号隔开
     * @param groupId 群组ID
     * @return
     */
   public boolean removeOutGroup(String members,String groupId){
       String url="http://a1.easemob.com/"+org_name+"/"+appname+"/chatgroups/"+groupId+"/users/"+members;
       httpHeaders.set("Authorization","Bearer "+getToken());
       //HttpEntity
       HttpEntity<MultiValueMap> requestEntity = new HttpEntity<MultiValueMap>(null, httpHeaders);
       try {
           restTemplate.exchange(url, HttpMethod.DELETE, requestEntity,String.class);
       }catch(HttpClientErrorException e){
           redisService.del("HuanxinToken");
           if (e instanceof HttpClientErrorException.Unauthorized)
           return removeOutGroup(members,groupId);
           throw e;
       }
       imGroupMemberMapper.deleteMemeberInGroup(members.split(","),groupId);
       return true;
   }
    /**
     * 删除群组的方法
     * @param groupId 环信groupId
     * @return
     */
   public boolean deleteGroup(String groupId){
       String url="http://a1.easemob.com/"+org_name+"/"+appname+"/chatgroups/"+groupId;

       httpHeaders.set("Authorization","Bearer "+getToken());
       //HttpEntity
       HttpEntity<MultiValueMap> requestEntity = new HttpEntity<MultiValueMap>(null, httpHeaders);
       try {
       restTemplate.exchange(url, HttpMethod.DELETE, requestEntity,String.class);
       }catch(HttpClientErrorException e){
          redisService.del("HuanxinToken");
           if (e instanceof HttpClientErrorException.Unauthorized)
          return deleteGroup(groupId);
           throw new MyException(ResultEnums.HUAN_XIN_GROUP_DISSOLVE);
      }
       ImGroup group = new ImGroup();
       group.setHxGroupId(groupId);
       ImGroup imGroup = imGroupMapper.selectOne(group);
       if (null == imGroup) {
           return false;
       }
       imGroup.setStatus(-1);
       imGroupMapper.updateByPrimaryKey(imGroup);//把群组的状态改变为已删除
       return true;
   }

    /**
     * 创建群组
     * @version 2019-3-11
     * @param ownerId 群主的ImUserId
     * @param groupName 群组的名称
     * @param huanXinDesc 群组的描述
     * @param pub 是否是公开群 现在暂时使用公开群
     * @param maxUsers 最大成员数量
     * @param approval 加入该群是否需要批准 现在暂时使用false
     * @param members 默认的成员
     * @param groupType 群组的类型 现在暂时使用的是一周情侣活动的key 为 IGBT_WEEKCP
     * @param registerId 群组的业务Id 暂时为当前活动的ID
     * @param useType 群的用途类型
     * @return
     */
   public boolean createChatGroup(String ownerId,String groupName,String huanXinDesc, boolean pub,Integer maxUsers,boolean approval,
                                  List<String> members,String groupType,String registerId, String matchNo, Integer useType){
       String url="http://a1.easemob.com/"+org_name+"/"+appname+"/chatgroups";
       httpHeaders.set("Authorization","Bearer "+getToken());
       String content="{\n" +
               "  \"groupname\": \""+groupName+"\",\n" +
               "  \"desc\": \""+huanXinDesc+"\",\n" +
               "  \"public\": "+pub+",\n" +
               "  \"maxusers\": "+maxUsers+",\n" +
               "  \"approval\": "+approval+",\n" +
               "  \"owner\": \""+ownerId+"\",\n" +
               "  \"members\":"+members+" }";
       JSONObject requestBody =JSONObject.parseObject(content);
       HttpEntity<JSONObject> requestEntity = new HttpEntity<>(requestBody, httpHeaders);
       ResponseEntity responseEntity=null;
       try {
           responseEntity= restTemplate.postForEntity(url, requestEntity, String.class);
       }catch (HttpClientErrorException e) {
            redisService.del("HuanxinToken");
           if (e instanceof HttpClientErrorException.Unauthorized)
           createChatGroup(ownerId, groupName, huanXinDesc, pub, maxUsers, approval, members,groupType,registerId, matchNo, useType);
           throw new MyException(ResultEnums.UNKNOWN_ERROR);
       }
       JSONObject resultJson=JSONObject.parseObject((String) responseEntity.getBody());
       String groupId= (String) ((JSONObject)resultJson.get("data")).get("groupid");
//       String groupId= UUIDUtil.getSJNum(14);
       ImGroup imGroup=new ImGroup(UUIDUtil.getEUUID(),
               groupId,groupName,huanXinDesc, pub?1:0,
               pub?"0":"1",pub?"0":"1",maxUsers,ownerId);
       imGroup.setBusinessId(registerId);
       imGroup.setBusinessType(groupType);
       imGroup.setMatchNo(matchNo);
       imGroup.setUseType(useType);
       imGroupMapper.insert(imGroup);//插入到群组表中去
       members.forEach(s -> imGroupMemberMapper.insertMember(new ImGroupMember(imGroup.getImGroupId(),s, 0)));//插入到群组成员表中去
       imGroupMemberMapper.insertMember(new ImGroupMember(imGroup.getImGroupId(),ownerId, 2));
       if (useType == 2) {
           try {
               for (String string: members
               ) {
                   if (!SendSmsUtil.SendSms(string))
                       throw new MyException(ResultEnums.SEND_SMS_CODE);
               }
           } catch (ClientException e) {
               e.printStackTrace();
               throw new MyException(ResultEnums.SEND_SMS_CODE);
           }
       }
               return true;
   }

    /**
     * 发送消息的方法
     * @param targetType 发送对象的类型 target_type 发送的目标类型；users：给用户发消息，chatgroups：给群发消息，chatrooms：给聊天室发消息
     * @param target 发送对象的ID target 发送的目标；注意这里需要用数组，数组长度建议不大于20，即使只有一个用户，也要用数组 ['u1']；给用户发送时数组元素是用户名，给群组发送时，数组元素是groupid
     * @param type 发送消息的类型 消息内容type 消息类型；txt:文本消息，img：图片消息，loc：位置消息，audio：语音消息，video：视频消息，file：文件消息
     * @param msg 发送消息的内容 msg
     * @param from 发送人的ID 表示消息发送者;无此字段Server会默认设置为“from”:“admin”，有from字段但值为空串(“”)时请求失败
     * @return
     */
    public boolean sendMessage(String targetType,List<String> target,String type,String msg,String from){
        String url="http://a1.easemob.com/"+org_name+"/"+appname+"/messages";
        httpHeaders.set("Authorization","Bearer "+getToken());
        String content="{\n" +
                "  \"target_type\": \""+targetType+"\",\n" +
                "  \"target\":"+ target+","+
                "  \"msg\": {\n" +
                "    \"type\": \""+type+"\",\n" +
                "    \"msg\": \""+msg+"\"\n" +
                "  },\n" +
                "\"from\":\""+from+"\"\n" +
                "}";
        try {
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, setContent(content), String.class);
        }catch(HttpClientErrorException e){
            redisService.del("HuanxinToken");
            if (e instanceof HttpClientErrorException.Unauthorized)
                return sendMessage(targetType,target,type,msg,from);
            throw new MyException(ResultEnums.UNKNOWN_ERROR);
        }
        return true;
    }

    /**
     * 修改用户的昵称
     * @param ImUserId
     * @param nickname
     * @return
     */
   public boolean putUserNickname(String ImUserId,String nickname){
       String url="http://a1.easemob.com/"+org_name+"/"+appname+"/users/"+ImUserId;
       Map<String,String> stringStringMap=new HashMap<>();
       stringStringMap.put("nickname",nickname);
       httpHeaders.set("Authorization","Bearer "+getToken());
       HttpEntity<Map> requestEntity = new HttpEntity<>(stringStringMap, httpHeaders);
       restTemplate.put(url,requestEntity);
       return true;
   }

}
