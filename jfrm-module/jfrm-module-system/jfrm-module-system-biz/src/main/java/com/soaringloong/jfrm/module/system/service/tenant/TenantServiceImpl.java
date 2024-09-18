package com.soaringloong.jfrm.module.system.service.tenant;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.comm.pojo.SystemException;
import com.comm.pojo.enums.CommonStatusEnum;
import com.soaringloong.jfrm.module.system.controller.admin.tenant.vo.TenantSaveReqVO;
import com.soaringloong.jfrm.module.system.dal.dataobject.tenant.TenantDO;
import com.soaringloong.jfrm.module.system.dal.mapper.tenant.TenantMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;

@Service
@Transactional(rollbackFor = Exception.class)
public class TenantServiceImpl implements TenantService {
    @Resource
    private TenantMapper tenantMapper;

    @Override
    public TenantDO getTenant(Long id) {
        return tenantMapper.selectById(id);
    }

    @Override
    public void validTenant(Long id) {
        TenantDO tenant = getTenant(id);
        if (tenant == null) {
            throw new SystemException(404, "租户不存在");
        }
        if (tenant.getStatus().equals(CommonStatusEnum.DISABLE.getStatus())) {
            throw new SystemException(500, tenant.getName() + "已禁用");
        }
        if (LocalDateTime.now().isAfter(tenant.getExpireTime())) {
            throw new SystemException(500, tenant.getName() + "已过期");
        }
    }

    private void validTenantNameDuplicate(String name, Long id) {
        TenantDO tenant = tenantMapper.selectOne(Wrappers.<TenantDO>lambdaQuery().eq(TenantDO::getName, name));
        if (tenant == null) {
            return;
        }
        // 如果 id 为空，说明不用比较是否为相同名字的租户
        if (!tenant.getId().equals(id)) {
            throw new SystemException(400, "已经存在租户："+name);
        }
    }

    @Override
    public Long createTenant(TenantSaveReqVO createReqVO) {
        // 校验租户名称是否重复
        validTenantNameDuplicate(createReqVO.getName(), null);
        // 创建租户
        TenantDO tenant = BeanUtil.toBean(createReqVO, TenantDO.class);
        tenantMapper.insert(tenant);

        // todo 创建租户角色


        // todo 创建租户用户


        return tenant.getId();
    }
}
