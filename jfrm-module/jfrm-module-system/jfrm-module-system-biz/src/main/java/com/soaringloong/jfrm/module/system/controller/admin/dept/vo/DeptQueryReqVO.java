package com.soaringloong.jfrm.module.system.controller.admin.dept.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Schema(description = "管理后台 - 部门列表 Request VO")
@Getter
@Setter
@Accessors(chain = true)
public class DeptQueryReqVO {
    @Schema(description = "部门名称，模糊匹配", example = "中国")
    private String name;

    @Schema(description = "展示状态，参见 CommonStatusEnum 枚举类", example = "1")
    private Integer status;
}
