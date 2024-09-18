package com.soaringloong.jfrm.module.system.service.dept;

import com.soaringloong.jfrm.module.system.controller.admin.dept.vo.DeptQueryReqVO;
import com.soaringloong.jfrm.module.system.controller.admin.dept.vo.DeptSaveReqVO;
import com.soaringloong.jfrm.module.system.dal.dataobject.dept.DeptDO;
import com.tk.dep.collection.CollectionUtils;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface DeptService {
    Long createDept(DeptSaveReqVO createReqVO);
    void updateDept(DeptSaveReqVO updateReqVO);
    void deleteDept(Long id);
    List<DeptDO> getSysDeptList(DeptQueryReqVO reqVO);
    List<DeptDO> getSysDeptList(Collection<Long> ids);
    DeptDO getSysDept(Long id);
    @Transactional(readOnly = true)
    default Map<Long, DeptDO> getDeptMap(Collection<Long> ids) {
        List<DeptDO> list = getSysDeptList(ids);
        return CollectionUtils.convertMap(list, DeptDO::getId);
    }
    List<DeptDO> getChildDeptList(Long id);
    Set<Long> getChildDeptIdList(Long id);
    void validateSysDeptList(Collection<Long> ids);
}
