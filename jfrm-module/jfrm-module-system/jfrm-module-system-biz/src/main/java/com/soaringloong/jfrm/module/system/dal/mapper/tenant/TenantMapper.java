package com.soaringloong.jfrm.module.system.dal.mapper.tenant;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.soaringloong.jfrm.module.system.dal.dataobject.tenant.TenantDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TenantMapper extends BaseMapper<TenantDO> {
}
