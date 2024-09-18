package com.soaringloong.jfrm.module.system.dal.mapper.dept;

import com.soaringloong.jfrm.framework.mybatis.core.mapper.BaseMapperX;
import com.soaringloong.jfrm.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.soaringloong.jfrm.module.system.controller.admin.dept.vo.DeptQueryReqVO;
import com.soaringloong.jfrm.module.system.dal.dataobject.dept.DeptDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.Collection;
import java.util.List;

@Mapper
public interface DeptMapper extends BaseMapperX<DeptDO> {

    default List<DeptDO> selectList(DeptQueryReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<DeptDO>()
                .likeIfPresent(DeptDO::getName, reqVO.getName())
                .eqIfPresent(DeptDO::getStatus, reqVO.getStatus()));
    }

    default Long selectCountByParentId(Long parentId) {
        return selectCount(DeptDO::getParentId, parentId);
    }

    default DeptDO selectByParentIdAndName(Long parentId, String name) {
        return selectOne(DeptDO::getParentId, parentId, DeptDO::getName, name);
    }

    default List<DeptDO> selectListByParentId(Collection<Long> parentIds) {
        return selectList(DeptDO::getParentId, parentIds);
    }

}
