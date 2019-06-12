<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
      <#list propertyColumns as pc>
      <#if pc.persistable==true>
      <#if pc.numberValidRule==true>
      <el-form-item label="${pc.label}"  prop="${pc.property}">
        <el-input v-model.number="dataForm.${pc.property}" placeholder="${pc.label}"></el-input>
      </el-form-item>
      <#else>
      <el-form-item label="${pc.label}"  prop="${pc.property}">
        <el-input v-model="dataForm.${pc.property}" placeholder="${pc.label}"></el-input>
      </el-form-item>
      </#if>
      </#if>
      </#list>
      <!--
      <el-form-item label="角色" size="mini" prop="roleIdList">
        <el-checkbox-group v-model="dataForm.roleIdList">
          <el-checkbox v-for="role in roleList" :key="role.roleId" :label="role.roleId">{{ role.roleName }}</el-checkbox>
        </el-checkbox-group>
      </el-form-item>
      <el-form-item label="状态" size="mini" prop="status">
        <el-radio-group v-model="dataForm.status">
          <el-radio :label="0">禁用</el-radio>
          <el-radio :label="1">正常</el-radio>
        </el-radio-group>
      </el-form-item>
      -->
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">取消</el-button>
      <el-button type="primary" @click="dataFormSubmit()">确定</el-button>
    </span>
  </el-dialog>
</template>

<script>
  import { isEmail, isMobile } from '@/utils/validate'
  export default {
    data () {
      return {
        visible: false,
        dataForm: {
          <#list propertyColumns as pc>
          <#if pc.persistable==true>
              '${pc.property}': '',
          </#if>
		  </#list>
        },
        dataRule: { 
        <#-- <#if formRules?exists && formRules.size>0> <#if formRules??>使用这种方法也可以 -->
        <#if formRules??>
        <#assign  keys=formRules?keys/>
        <#list keys as key>
          ${key}: [
          	<#list formRules[key] as rule>
          	${rule}<#if rule?has_next>,</#if>
          	</#list> 
          ]<#if key?has_next>,</#if>
        </#list>
        </#if>
        }
      }
    },
    methods: {
      init (id) {
        this.dataForm.id = id || null
        this.visible = true
        if (this.dataForm.id) {
            this.$http({
            url: this.$http.adornUrl(`/${module}/${entitySimpleClassNameUncap}/get/<#noparse>${this.dataForm.id}</#noparse>`),
            method: 'get',
            params: this.$http.adornParams()
        }).then(({data}) => {
           if (data && data.success === true) {
              <#list propertyColumns as pc>
              <#if pc.persistable==true>
              this.dataForm.${pc.property} = data.data.${pc.property};
              </#if>
		      </#list>
            }
          })
        }//
      },
      // 表单提交
      dataFormSubmit () {
        this.$refs['dataForm'].validate((valid) => {
          if (valid) {
            this.$http({
              url: this.$http.adornUrl(`/${module}/${entitySimpleClassNameUncap}/<#noparse>${!this.dataForm.id ? 'create' : 'update'}</#noparse>`),
              method: 'post',
              data: this.$http.adornData({
              <#list propertyColumns as pc>
              <#if pc.persistable==true>
                '${pc.property}': this.dataForm.${pc.property},
              </#if>
		      </#list>
              })
            }).then(({data}) => {
              if (data && data.success === true) {
                this.$message({
                  message: '操作成功',
                  type: 'success',
                  duration: 1500,
                  onClose: () => {
                    this.visible = false
                    this.$emit('refreshDataList')
                  }
                })
              } else {
                this.$message.error(data.msg)
              }
            })
          }
        })
      }//dataFormSubmit
    }
  }
</script>
