package com.ynw.system.dao;

import com.ynw.system.entity.ActiveTaskClock;
import com.ynw.system.util.MyMapper;

import java.util.List;

public interface ActiveTaskClockMapper extends MyMapper<ActiveTaskClock> {

    List<ActiveTaskClock> findAllByActivityId(String registerId);

}
