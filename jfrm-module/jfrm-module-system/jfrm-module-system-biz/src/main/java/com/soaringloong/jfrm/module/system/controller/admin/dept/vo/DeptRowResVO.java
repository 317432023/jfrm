package com.soaringloong.jfrm.module.system.controller.admin.dept.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 部门列表行信息 Response VO")
@Getter
@Setter
@Accessors(chain = true)
public class DeptRowResVO {
    @Schema(description = "部门编号", example = "1024")
    private Long id;

    @Schema(description = "部门名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "中国")
    private String name;

    @Schema(description = "父部门 ID", example = "1024")
    private Long parentId;

    @Schema(description = "显示顺序", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    private Integer sort;

    @Schema(description = "负责人的用户名", example = "zhangsan01")
    private String leader;

    @Schema(description = "联系电话", example = "15601691000")
    private String phone;

    @Schema(description = "邮箱", example = "yudao@iocoder.cn")
    private String email;

    @Schema(description = "状态,见 CommonStatusEnum 枚举", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer status;

    @JsonFormat(timezone = "GMT+8", locale = "zh_CN", pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED, example = "时间戳格式")
    private LocalDateTime createTime;
}
