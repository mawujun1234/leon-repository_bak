DELIMITER #修改分隔符
DROP PROCEDURE IF EXISTS  insert_sys_menu;
CREATE PROCEDURE insert_sys_menu(IN parent_id_in int)
begin 
	declare cnt int default 0;
	declare last_id int default 0;
	#set @cnt=0;
  select count(*) into cnt from sys_menu where name='${comment}' and url='${module}/${entitySimpleClassNameUncap}';
if cnt = 0 then
	INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES ( parent_id_in,'${comment}' , '${module}/${entitySimpleClassNameUncap}', null, 1, 'shoucang', 0);
	SET last_id=LAST_INSERT_ID();
	 SELECT concat('模块的id为:', last_id);
	INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES ( last_id, '查看', NULL, '${module}:${entitySimpleClassNameUncap}:list,${module}:${entitySimpleClassNameUncap}:get', 2, NULL, 0);
	INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES ( last_id, '新增', NULL, '${module}:${entitySimpleClassNameUncap}:create', 2, NULL, 0);
	INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES ( last_id, '修改', NULL, '${module}:${entitySimpleClassNameUncap}:update', 2, NULL, 0);
	INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES ( last_id, '删除', NULL, '${module}:${entitySimpleClassNameUncap}:remove', 2, NULL, 0);
	COMMIT;  
else
SELECT concat('${comment}菜单已经存在', '');
end if;
end;

#要把xxx替换成上级菜单的id，才能进行插入
call insert_sys_menu(${extraCfg.menu_parent_id_in});