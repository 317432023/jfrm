package com.soaringloong.jfrm.module.system.controller.admin.tenant.vo;

import cn.hutool.core.util.ObjectUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * 保存租户请求VO
 */
@Schema(description = "管理后台 - 租户创建/修改 Request VO")
@Data
@Accessors(chain = true)
public class TenantSaveReqVO {
    @Schema(description = "租户ID", example = "1024")
    private Long id;

    @Schema(description = "租户名", requiredMode = Schema.RequiredMode.REQUIRED, example = "示例租户名")
    @NotNull(message = "租户名不能为空")
    private String name;

    @Schema(description = "联系人", requiredMode = Schema.RequiredMode.REQUIRED, example = "示例联系人")
    @NotNull(message = "联系人不能为空")
    private String contactName;

    @Schema(description = "联系手机", example = "18888888888")
    private String contactMobile;

    @Schema(description = "租户状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "租户状态")
    private Integer status;

    // ========== 仅【创建】时，需要传递的字段 ==========

    @Schema(description = "用户账号", requiredMode = Schema.RequiredMode.REQUIRED, example = "yudao")
    @Pattern(regexp = "^[a-zA-Z0-9]{4,30}$", message = "用户账号由 数字、字母 组成")
    @Size(min = 4, max = 30, message = "用户账号长度为 4-30 个字符")
    private String username;

    @Schema(description = "密码", requiredMode = Schema.RequiredMode.REQUIRED, example = "123456")
    @Size(min = 4, max = 16, message = "密码长度为 4-16 位")
    private String password;

    @AssertTrue(message = "用户账号、密码不能为空")
    @JsonIgnore
    public boolean isUsernameValid() {
        return id != null // 修改时，不需要传递
                || (ObjectUtil.isAllNotEmpty(username, password)); // 新增时，必须都传递 username、password
    }
}
