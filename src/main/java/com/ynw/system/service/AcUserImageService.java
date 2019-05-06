package com.ynw.system.service;

import com.ynw.system.dao.AcUserImageMapper;
import com.ynw.system.entity.AcUserImage;
import com.ynw.system.entity.PushBody;
import io.swagger.models.auth.In;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 *  @author ChengZhi
 *  @version 2018-12/13
 */
@Service
@Transactional
public class AcUserImageService extends MyService<AcUserImageMapper, AcUserImage> {

    @Autowired
    private PushService pushService;

    /**
     *  审核照片
     * @param image
     * @param content 推送审核失败理由
     * @return
     */
    public Integer updateAcUserImageById(AcUserImage image, String content) {
        Integer code = this.update(image);
        if (code >0 && StringUtils.isNotEmpty(content)) {
            PushBody body = new PushBody("PMT_TEXT","PMG_SYSTEM","PMS_SINGLE","照片审核失败",
                    "您的照片审核不通过（"+content+"),请从新选择符合的照片！！！");
            pushService.insertMgBody(null, null, true, body, "PMBT_PLATFORM_TO_REMIND", image.getAcUserId());
        }
        return code;
    }

}
