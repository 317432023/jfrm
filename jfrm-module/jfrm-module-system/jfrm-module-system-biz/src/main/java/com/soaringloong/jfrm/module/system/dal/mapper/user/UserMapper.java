package com.soaringloong.jfrm.module.system.dal.mapper.user;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.soaringloong.jfrm.module.system.dal.dataobject.user.UserDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<UserDO> {
}
