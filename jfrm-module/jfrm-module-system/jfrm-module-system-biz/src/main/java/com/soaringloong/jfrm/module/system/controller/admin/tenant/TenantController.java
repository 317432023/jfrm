package com.soaringloong.jfrm.module.system.controller.admin.tenant;

import com.soaringloong.jfrm.module.system.service.tenant.TenantService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <ul>
 *     <li>list - 列表</li>
 *     <li>create - 创建</li>
 *     <li>update - 更新</li>
 *     <li>delete - 删除</li>
 *     <li>get - 获取信息(一条)</li>
 * </ul>
 */
@Tag(name = "管理后台 - 租户")
@RestController
@RequestMapping("system/tenant")
@Validated
public class TenantController {

    @Resource
    private TenantService tenantService;

}
