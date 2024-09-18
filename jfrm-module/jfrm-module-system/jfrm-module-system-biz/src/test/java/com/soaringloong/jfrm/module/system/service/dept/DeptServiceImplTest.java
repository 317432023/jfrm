package com.soaringloong.jfrm.module.system.service.dept;

import com.comm.pojo.enums.CommonStatusEnum;
import com.soaringloong.jfrm.framework.test.core.BaseDbUnitTest;
import com.soaringloong.jfrm.module.system.controller.admin.dept.vo.DeptQueryReqVO;
import com.soaringloong.jfrm.module.system.controller.admin.dept.vo.DeptSaveReqVO;
import com.soaringloong.jfrm.module.system.dal.dataobject.dept.DeptDO;
import com.soaringloong.jfrm.module.system.dal.mapper.dept.DeptMapper;
import com.tk.dep.object.ObjectUtils;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Import;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static com.soaringloong.jfrm.framework.test.util.AssertUtils.assertPojoEquals;
import static com.soaringloong.jfrm.framework.test.util.AssertUtils.assertSystemException;
import static com.soaringloong.jfrm.framework.test.util.RandomUtils.*;
import static com.soaringloong.jfrm.module.system.enums.ErrorCodeConstants.*;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.*;

@Import(DeptServiceImpl.class)
public class DeptServiceImplTest extends BaseDbUnitTest {
    @Resource
    private DeptServiceImpl deptService;
    @Resource
    private DeptMapper deptMapper;

    @Test
    public void testCreateDept() {
        // 准备参数
        DeptSaveReqVO reqVO = randomPojo(DeptSaveReqVO.class, o -> {
            o.setId(null); // 防止 id 被设置
            o.setParentId(DeptDO.PARENT_ID_ROOT);
            o.setStatus(randomCommonStatus());
        });

        // 调用
        Long deptId = deptService.createDept(reqVO);
        // 断言
        assertNotNull(deptId);
        // 校验记录的属性是否正确
        DeptDO deptDO = deptMapper.selectById(deptId);
        assertPojoEquals(reqVO, deptDO, "deptId");
    }

    @Test
    public void testUpdateDept() {
        // mock 数据
        DeptDO dbSysDeptDO = randomPojo(DeptDO.class, o -> o.setStatus(randomCommonStatus()));
        deptMapper.insert(dbSysDeptDO);// @Sql: 先插入出一条存在的数据
        // 准备参数
        DeptSaveReqVO reqVO = randomPojo(DeptSaveReqVO.class, o -> {
            // 设置更新的 ID
            o.setParentId(DeptDO.PARENT_ID_ROOT);
            o.setId(dbSysDeptDO.getId());
            o.setStatus(randomCommonStatus());
        });

        // 调用
        deptService.updateDept(reqVO);
        // 校验是否更新正确
        DeptDO deptDO = deptMapper.selectById(reqVO.getId()); // 获取最新的
        assertPojoEquals(reqVO, deptDO);
    }

    @Test
    public void testDeleteDept_success() {
        // mock 数据
        DeptDO dbSysDeptDO = randomPojo(DeptDO.class);
        deptMapper.insert(dbSysDeptDO);// @Sql: 先插入出一条存在的数据
        // 准备参数
        Long id = dbSysDeptDO.getId();

        // 调用
        deptService.deleteDept(id);
        // 校验数据不存在了
        assertNull(deptMapper.selectById(id));
    }

    @Test
    public void testDeleteDept_exitsChildren() {
        // mock 数据
        DeptDO parentDept = randomPojo(DeptDO.class);
        deptMapper.insert(parentDept);// @Sql: 先插入出一条存在的数据
        // 准备参数
        DeptDO childrenSysDeptDO = randomPojo(DeptDO.class, o -> {
            o.setParentId(parentDept.getId());
            o.setStatus(randomCommonStatus());
        });
        // 插入子部门
        deptMapper.insert(childrenSysDeptDO);

        // 调用, 并断言异常
        assertSystemException(() -> deptService.deleteDept(parentDept.getId()), DEPT_EXITS_CHILDREN);
    }

    @Test
    public void testValidateDeptExists_notFound() {
        // 准备参数
        Long id = randomLongId();

        // 调用, 并断言异常
        assertSystemException(() -> deptService.validateDeptExists(id), DEPT_NOT_FOUND);
    }

    @Test
    public void testValidateParentDept_parentError() {
        // 准备参数
        Long id = randomLongId();

        // 调用, 并断言异常
        assertSystemException(() -> deptService.validateParentDept(id, id),
                DEPT_PARENT_ERROR);
    }

    @Test
    public void testValidateParentDept_parentIsChild() {
        // mock 数据（父节点）
        DeptDO parentDept = randomPojo(DeptDO.class);
        deptMapper.insert(parentDept);
        // mock 数据（子节点）
        DeptDO childDept = randomPojo(DeptDO.class, o -> {
            o.setParentId(parentDept.getId());
        });
        deptMapper.insert(childDept);

        // 准备参数
        Long id = parentDept.getId();
        Long parentId = childDept.getId();

        // 调用, 并断言异常
        assertSystemException(() -> deptService.validateParentDept(id, parentId), DEPT_PARENT_IS_CHILD);
    }

    @Test
    public void testValidateNameUnique_duplicate() {
        // mock 数据
        DeptDO deptDO = randomPojo(DeptDO.class);
        deptMapper.insert(deptDO);

        // 准备参数
        Long id = randomLongId();
        Long parentId = deptDO.getParentId();
        String name = deptDO.getName();

        // 调用, 并断言异常
        assertSystemException(() -> deptService.validateDeptNameUnique(id, parentId, name),
                DEPT_NAME_DUPLICATE);
    }

    @Test
    public void testGetDept() {
        // mock 数据
        DeptDO deptDO = randomPojo(DeptDO.class, o -> o.setParentId(0L));
        deptMapper.insert(deptDO);
        // 准备参数
        Long id = deptDO.getId();
        // 调用
        DeptDO dbDept = deptService.getSysDept(id);
        // 断言
        assertEquals(deptDO, dbDept);
    }

    @Test
    public void testGetDeptList_ids() {
        // mock 数据
        DeptDO deptDO01 = randomPojo(DeptDO.class);
        deptMapper.insert(deptDO01);
        DeptDO deptDO02 = randomPojo(DeptDO.class);
        deptMapper.insert(deptDO02);
        // 准备参数
        List<Long> ids = Arrays.asList(deptDO01.getId(), deptDO02.getId());

        // 调用
        List<DeptDO> deptDOList = deptService.getSysDeptList(ids);
        // 断言
        assertEquals(2, deptDOList.size());
        assertEquals(deptDO01, deptDOList.get(0));
        assertEquals(deptDO02, deptDOList.get(1));
    }

    @Test
    public void testGetDeptList_reqVO() {
        // mock 数据
        DeptDO dept = randomPojo(DeptDO.class, o -> { // 等会查询到
            o.setName("开发部");
            o.setStatus(CommonStatusEnum.ENABLE.getStatus());
        });
        deptMapper.insert(dept);
        // 测试 name 不匹配
        deptMapper.insert(ObjectUtils.cloneIgnoreFields(dept, o -> o.setName("发"), "deptId"));
        // 测试 status 不匹配
        deptMapper.insert(ObjectUtils.cloneIgnoreFields(dept, o -> o.setStatus(CommonStatusEnum.DISABLE.getStatus()), "deptId"));
        // 准备参数
        DeptQueryReqVO reqVO = new DeptQueryReqVO();
        reqVO.setName("开");
        reqVO.setStatus(CommonStatusEnum.ENABLE.getStatus());

        // 调用
        List<DeptDO> sysSysDeptDOS = deptService.getSysDeptList(reqVO);
        // 断言
        assertEquals(1, sysSysDeptDOS.size());
        assertPojoEquals(dept, sysSysDeptDOS.get(0));
    }

    @Test
    public void testGetChildDeptList() {
        // mock 数据（1 级别子节点）
        DeptDO dept1 = randomPojo(DeptDO.class, o -> o.setName("1"));
        deptMapper.insert(dept1);
        DeptDO dept2 = randomPojo(DeptDO.class, o -> o.setName("2"));
        deptMapper.insert(dept2);
        // mock 数据（2 级子节点）
        DeptDO dept1a = randomPojo(DeptDO.class, o -> o.setName("1-a").setParentId(dept1.getId()));
        deptMapper.insert(dept1a);
        DeptDO dept2a = randomPojo(DeptDO.class, o -> o.setName("2-a").setParentId(dept2.getId()));
        deptMapper.insert(dept2a);
        // 准备参数
        Long id = dept1.getParentId();

        // 调用
        List<DeptDO> result = deptService.getChildDeptList(id);
        // 断言
        assertEquals(result.size(), 2);
        assertPojoEquals(dept1, result.get(0));
        assertPojoEquals(dept1a, result.get(1));
    }

    @Test
    public void testGetChildDeptListFromCache() {
        // mock 数据（1 级别子节点）
        DeptDO dept1 = randomPojo(DeptDO.class, o -> o.setName("1"));
        deptMapper.insert(dept1);
        DeptDO dept2 = randomPojo(DeptDO.class, o -> o.setName("2"));
        deptMapper.insert(dept2);
        // mock 数据（2 级子节点）
        DeptDO dept1a = randomPojo(DeptDO.class, o -> o.setName("1-a").setParentId(dept1.getId()));
        deptMapper.insert(dept1a);
        DeptDO dept2a = randomPojo(DeptDO.class, o -> o.setName("2-a").setParentId(dept2.getId()));
        deptMapper.insert(dept2a);
        // 准备参数
        Long id = dept1.getParentId();

        // 调用
        Set<Long> result = deptService.getChildDeptIdList(id);
        // 断言
        assertEquals(result.size(), 2);
        assertTrue(result.contains(dept1.getId()));
        assertTrue(result.contains(dept1a.getId()));
    }

    @Test
    public void testValidateDeptList_success() {
        // mock 数据
        DeptDO deptDO = randomPojo(DeptDO.class).setStatus(CommonStatusEnum.ENABLE.getStatus());
        deptMapper.insert(deptDO);
        // 准备参数
        List<Long> ids = singletonList(deptDO.getId());

        // 调用，无需断言
        deptService.validateSysDeptList(ids);
    }

    @Test
    public void testValidateDeptList_notFound() {
        // 准备参数
        List<Long> ids = singletonList(randomLongId());

        // 调用, 并断言异常
        assertSystemException(() -> deptService.validateSysDeptList(ids), DEPT_NOT_FOUND);
    }

    @Test
    public void testValidateDeptList_notEnable() {
        // mock 数据
        DeptDO deptDO = randomPojo(DeptDO.class).setStatus(CommonStatusEnum.DISABLE.getStatus());
        deptMapper.insert(deptDO);
        // 准备参数
        List<Long> ids = singletonList(deptDO.getId());

        // 调用, 并断言异常
        assertSystemException(() -> deptService.validateSysDeptList(ids), DEPT_NOT_ENABLE, deptDO.getName());
    }
}
