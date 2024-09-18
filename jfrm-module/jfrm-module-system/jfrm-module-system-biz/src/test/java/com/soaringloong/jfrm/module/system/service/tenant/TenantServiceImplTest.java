package com.soaringloong.jfrm.module.system.service.tenant;

import com.soaringloong.jfrm.framework.test.core.BaseDbUnitTest;
import com.soaringloong.jfrm.framework.test.util.AssertUtils;
import com.soaringloong.jfrm.framework.test.util.RandomUtils;
import com.soaringloong.jfrm.module.system.controller.admin.tenant.vo.TenantSaveReqVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Import;

import javax.annotation.Resource;

import static com.soaringloong.jfrm.framework.test.util.RandomUtils.randomLongId;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Import(TenantServiceImpl.class)
public class TenantServiceImplTest extends BaseDbUnitTest {
    @Resource
    private TenantService tenantService;

    @BeforeEach
    public void before() {

    }

    @Test
    public void testValidTenant_notExists() {
        AssertUtils.assertSystemException(() -> tenantService.validTenant(randomLongId()), 404, "租户不存在");
    }

    @Test
    public void testCreateTenant() {
        // 准备参数
        TenantSaveReqVO saveReqVO = RandomUtils.randomPojo(TenantSaveReqVO.class, o -> {
            o.setContactName("Java框架");
            o.setContactMobile("18888888888");
            o.setStatus(RandomUtils.randomCommonStatus());
            o.setUsername("JavaFramework");
            o.setPassword("admin123");
        }).setId(null); // 设置为 null，方便后面校验
        Long tenantId = tenantService.createTenant(saveReqVO);
        // 断言
        assertTrue(tenantId != null && tenantId > 0);
    }

}
