--要把xxx替换成上级菜单的id，才能进行插入
INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES ( xxx, '查看', NULL, '${module}:${entitySimpleClassNameUncap}:list,${module}:${entitySimpleClassNameUncap}:get', 2, NULL, 0);
INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES ( xxx, '新增', NULL, '${module}:${entitySimpleClassNameUncap}:create', 2, NULL, 0);
INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES ( xxx, '修改', NULL, '${module}:${entitySimpleClassNameUncap}:update', 2, NULL, 0);
INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES ( xxx, '删除', NULL, '${module}:${entitySimpleClassNameUncap}:remove', 2, NULL, 0);