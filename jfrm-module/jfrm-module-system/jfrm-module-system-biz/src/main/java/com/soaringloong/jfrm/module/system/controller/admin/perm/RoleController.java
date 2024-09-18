package com.soaringloong.jfrm.module.system.controller.admin.perm;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <ul>
 *     <li>list - 列表</li>
 *     <li>create - 创建</li>
 *     <li>update - 更新</li>
 *     <li>delete - 删除</li>
 *     <li>get - 获取信息(一条)</li>
 * </ul>
 */
@Tag(name = "管理后台 - 权限组")
@RestController
@RequestMapping("system/role")
@Validated
public class RoleController {

    /*@GetMapping("/page")
    @Operation(summary = "获得角色分页")
    @PreAuthorize("@ss.hasPermission('system:role:query')")
    public CommonResult<PageResult<RoleRespVO>> getRolePage(RolePageReqVO pageReqVO) {
        PageResult<RoleDO> pageResult = roleService.getRolePage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, RoleRespVO.class));
    }*/
}
