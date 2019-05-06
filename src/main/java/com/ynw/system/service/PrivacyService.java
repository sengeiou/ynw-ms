package com.ynw.system.service;

import com.ynw.system.dao.AcUserImageMapper;
import com.ynw.system.dao.PrivacyMapper;
import com.ynw.system.entity.AcUserImage;
import com.ynw.system.entity.Privacy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *  @author ChengZhi
 *  @version 2018-12/13
 */
@Service
@Transactional
public class PrivacyService extends MyService<PrivacyMapper, Privacy> {
}
