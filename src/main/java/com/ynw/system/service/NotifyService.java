package com.ynw.system.service;

import com.ynw.system.dao.AcUserImageMapper;
import com.ynw.system.dao.NotifyMapper;
import com.ynw.system.entity.AcUserImage;
import com.ynw.system.entity.Notify;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *  @author ChengZhi
 *  @version 2018-12/12
 */
@Service
@Transactional
public class NotifyService extends MyService<NotifyMapper, Notify> {
}
