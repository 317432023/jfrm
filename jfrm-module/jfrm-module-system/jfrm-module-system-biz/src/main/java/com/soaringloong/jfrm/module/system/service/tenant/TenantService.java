package com.soaringloong.jfrm.module.system.service.tenant;

import com.soaringloong.jfrm.module.system.controller.admin.tenant.vo.TenantSaveReqVO;
import com.soaringloong.jfrm.module.system.dal.dataobject.tenant.TenantDO;

public interface TenantService {
    /**
     * 获得租户
     *
     * @param id 编号
     * @return 租户
     */
    TenantDO getTenant(Long id);
    /**
     * 校验租户是否合法
     *
     * @param id 租户编号
     */
    void validTenant(Long id);

    Long createTenant(TenantSaveReqVO saveReqVO);
}
