package com.qyhstech.core.validation.groups;

import jakarta.validation.GroupSequence;

/**
 * 校验分组 add
 */
@GroupSequence({GroupAdd.class, GroupEdit.class, GroupDelete.class})
public interface GroupAll {
}
