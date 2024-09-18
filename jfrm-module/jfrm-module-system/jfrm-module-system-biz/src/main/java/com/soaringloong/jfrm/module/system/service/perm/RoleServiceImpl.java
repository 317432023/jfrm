package com.soaringloong.jfrm.module.system.service.perm;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class RoleServiceImpl implements RoleService {
}
