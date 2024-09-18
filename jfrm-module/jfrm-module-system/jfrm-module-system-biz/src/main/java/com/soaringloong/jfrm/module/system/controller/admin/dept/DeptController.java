package com.soaringloong.jfrm.module.system.controller.admin.dept;

import com.comm.pojo.R;
import com.comm.pojo.enums.CommonStatusEnum;
import com.soaringloong.jfrm.module.system.controller.admin.dept.vo.*;
import com.soaringloong.jfrm.module.system.dal.dataobject.dept.DeptDO;
import com.soaringloong.jfrm.module.system.service.dept.DeptService;
import com.tk.dep.object.BeanUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

import static com.comm.pojo.R.success;

/**
 * <ul>
 *     <li>list - 列表</li>
 *     <li>create - 创建</li>
 *     <li>update - 更新</li>
 *     <li>delete - 删除</li>
 *     <li>get - 获取信息(一条)</li>
 * </ul>
 */
@Tag(name = "管理后台 - 部门")
@RestController
@RequestMapping("system/dept")
@Validated
public class DeptController {
    @Resource
    private DeptService deptService;

    @PostMapping("create")
    @Operation(summary = "创建部门")
    //@PreAuthorize("@ss.hasPermission('sys:dept:create')")
    public R<Long> createDept(@Valid @RequestBody DeptSaveReqVO createReqVO) {
        Long deptId = deptService.createDept(createReqVO);
        return success(deptId);
    }

    @PutMapping("update")
    @Operation(summary = "更新部门")
    //@PreAuthorize("@ss.hasPermission('sys:dept:update')")
    public R<Boolean> updateDept(@Valid @RequestBody DeptSaveReqVO updateReqVO) {
        deptService.updateDept(updateReqVO);
        return success(true);
    }

    @DeleteMapping("delete")
    @Operation(summary = "删除部门")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    //@PreAuthorize("@ss.hasPermission('sys:dept:delete')")
    public R<Boolean> deleteDept(@RequestParam("id") Long id) {
        deptService.deleteDept(id);
        return success(true);
    }

    @GetMapping("/list")
    @Operation(summary = "获取部门列表")
    //@PreAuthorize("@ss.hasPermission('sys:dept:query')")
    public R<List<DeptRowResVO>> getDeptList(DeptQueryReqVO reqVO) {
        List<DeptDO> list = deptService.getSysDeptList(reqVO);
        return success(BeanUtils.toBean(list, DeptRowResVO.class));
    }

    @GetMapping(value = {"/list-all-simple", "/simple-list"})
    @Operation(summary = "获取部门精简信息列表", description = "只包含被开启的部门，主要用于前端的下拉选项")
    public R<List<DeptSimpleResVO>> getSimpleDeptList() {
        List<DeptDO> list = deptService.getSysDeptList(
                new DeptQueryReqVO().setStatus(CommonStatusEnum.ENABLE.getStatus()));
        return success(BeanUtils.toBean(list, DeptSimpleResVO.class));
    }

    @GetMapping("/get")
    @Operation(summary = "获得部门信息")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    //@PreAuthorize("@ss.hasPermission('sys:dept:query')")
    public R<DeptFormResVO> getDept(@RequestParam("id") Long id) {
        DeptDO dept = deptService.getSysDept(id);
        return success(BeanUtils.toBean(dept, DeptFormResVO.class));
    }
}
