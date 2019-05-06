package com.ynw.system.dao;

import com.ynw.system.entity.AcUserLabelClassify;
import com.ynw.system.util.MyMapper;

import java.util.List;

public interface AcUserLabelClassifyMapper extends MyMapper<AcUserLabelClassify> {

    List<AcUserLabelClassify> findAll();

    List<AcUserLabelClassify> findBySortMax();

}
