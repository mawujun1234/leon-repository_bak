<#assign uploadable = false>
<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
      <#list propertyColumns as pc>
      <#assign disabled = ""/>
      <#if pc.disabled==true>
      <#assign disabled = "disabled"/>
      </#if><#-- 把子段设置为只读-->
      <#if pc.persistable==true><#-- 只有具有持久化的才会作为表单 -->
      <#if pc.numberValidRule==true><#-- 如果是数字，那验证规则有点不一样-->
      <el-form-item label="${pc.label}"  prop="${pc.property}">
        <el-input v-model.number="dataForm.${pc.property}" placeholder="${pc.label}" ${disabled}></el-input>
      </el-form-item>
      <#elseif pc.uploadable==true>
      <el-form-item label="${pc.label}"  prop="${pc.property}">
        <el-upload
          class="upload-demo"
          :limit="1"
          :action="upload_action"
          :headers="${pc.property}_headers"
          :on-preview="handlePreview"
          :on-remove="handleRemove"
          :before-remove="beforeRemove"
          :on-exceed="handleExceed"
          :before-upload="beforeUpload"
          :on-success="handleSuccess"
          :file-list="${pc.property}_fileList"  ${disabled}
          >
          <el-button size="small" type="primary">点击上传</el-button>
        </el-upload>
      </el-form-item>
      <#elseif pc.isEnum==true>
      <el-form-item label="${pc.label}" size="mini" prop="${pc.property}">
        <el-radio-group v-model="dataForm.${pc.property}"  ${disabled}>
          <#if pc.enumValues??>
          <#assign  keys=pc.enumValues?keys/>
          <#list keys as key>
            <el-radio label="${key}">${pc.enumValues[key]}</el-radio>
          </#list>
          </#if>
        </el-radio-group>
      </el-form-item> 
      <#elseif pc.isFk==true && pc.cndable==true>
      <el-form-item label="${pc.label}" size="mini" prop="${pc.property}">
      <el-select v-model="dataForm.${pc.property}" clearable placeholder="${pc.label}">
        <el-option
          v-for="item in ${pc.property}_options"
          :key="item.value"
          :label="item.label"
          :value="item.value">
        </el-option>
      </el-select>
      </el-form-item> 
      <#elseif pc.isDateProp==true>
      <el-form-item label="${pc.label}"  prop="${pc.property}">
        <el-date-picker
          v-model="dataForm.${pc.property}"
          align="right"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="${pc.label}"
          :picker-options="pickerOptions"  ${disabled}>
        </el-date-picker>
      </el-form-item>
      <#else>
      <#if pc.isId==true || pc.isFk==true><#-- 如果是id和外键的话，就隐藏显示。前面已经判断过是外键同时是查询条件的时候是显示下拉框的 -->
      <el-form-item label="${pc.label}"  prop="${pc.property}" style="display:none;">
        <el-input v-model="dataForm.${pc.property}" placeholder="${pc.label}"  ${disabled} ></el-input>
      </el-form-item>
      <#else>
      <el-form-item label="${pc.label}"  prop="${pc.property}">
        <el-input v-model="dataForm.${pc.property}" placeholder="${pc.label}"  ${disabled}></el-input>
      </el-form-item>
      </#if><#-- 如果是id的话，就隐藏显示 -->
      </#if>
      </#if>
      </#list>
      <!--
      <el-form-item label="角色" size="mini" prop="roleIdList">
        <el-checkbox-group v-model="dataForm.roleIdList">
          <el-checkbox v-for="role in roleList" :key="role.roleId" :label="role.roleId">{{ role.roleName }}</el-checkbox>
        </el-checkbox-group>
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
  //import { isEmail, isMobile } from '@/utils/validate'
  export default {
    data () {
      return {
        visible: false,
        <#list propertyColumns as pc>
        <#if pc.uploadable==true>
        '${pc.property}_fileList': [],//用于${pc.label}（${pc.property}）的文件上传
        <#elseif pc.isDateProp==true>
        pickerOptions: {
          disabledDate(time) {
            return time.getTime() > Date.now();
          },
          shortcuts: [{
            text: '今天',
            onClick(picker) {
              picker.$emit('pick', new Date());
            }
          }, {
            text: '昨天',
            onClick(picker) {
              const date = new Date();
              date.setTime(date.getTime() - 3600 * 1000 * 24);
              picker.$emit('pick', date);
            }
          }, {
            text: '一周前',
            onClick(picker) {
              const date = new Date();
              date.setTime(date.getTime() - 3600 * 1000 * 24 * 7);
              picker.$emit('pick', date);
            }
          }]
        },
        <#elseif pc.isFk==true && pc.cndable==true>
        ${pc.property}_options: [],
        </#if>
		</#list>
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
    mounted() {
     <#list cndPropertys as pc>
     <#if pc.isFk==true && pc.cndable==true>
    	this.init_${pc.property}_options();
     </#if>
     </#list>
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
		      
		      <#list propertyColumns as pc>
	          <#if pc.uploadable==true>
	          <#assign uploadable = true>
	          //初始化上传文件列表
	          if(this.dataForm.${pc.property}){
                let name=this.getOrginalFileName(this.dataForm.${pc.property});
                this.${pc.property}_fileList=[];
                this.${pc.property}_fileList.push({name:name,url:this.dataForm.${pc.property},property:'${pc.property}'});
              }
	          </#if>
			  </#list>
            }
          })
        }//
      },
      <#list cndPropertys as pc>
      <#if pc.isFk==true && pc.cndable==true>
      init_${pc.property}_options(){
      	this.$http({
          url: this.$http.adornUrl('/${pc.fk_module}/${pc.fk_entitySimpleClassNameUncap}/listAll'),
          method: 'get'
        }).then(({data}) => {
          if (data && data.code === 0) {
            this.${pc.property}_options = data.data;
          } else {
            this.$notify.error({
              title: '错误',
              message: "初始化下拉框'${pc.label}'的数据失败!"+data.msg
            });
          }
          this.dataListLoading = false
        })
      },
      </#if>
      </#list>
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
      <#if uploadable==true>
      ,handlePreview(file) {//公用，用于的文件上传
        let vm=this;
        this.$http.download('/sys/document/download',file.name,{fullName:file.url},function(result){
          if(result.success==false){
            vm.$message.error(result.msg)
          }
        });
        //window.open(this.$http.adornUrl(`/sys/document/preview/`+file.url));预览文件的接口
      },
      handleExceed(files, fileList) {//公用，用于的文件上传
        this.$message.warning(`当前限制选择 1 个文件，<#noparse>本次选择了 ${files.length} 个文件，共选择了 ${files.length + fileList.length} 个文件</#noparse>`);
      },
      beforeUpload(file){//公用，用于的文件上传
      	//使用 before-upload 限制用户上传的图片格式和大小
      	//const isJPG = file.type === 'image/jpeg';
      	//const isLt2M = file.size / 1024 / 1024 < 2;
      	//if (!isJPG) {
        //  this.$message.error('上传头像图片只能是 JPG 格式!');
        //}
        //if (!isLt2M) {
        //  this.$message.error('上传头像图片大小不能超过 2MB!');
        //}
        //return isJPG && isLt2M;
        return true;
      },
      handleSuccess(response, file, fileList){//公用，用于的文件上传
        if(response.success==true){
          this.$message({
            message: '上传成功',
            type: 'success'
          });
          this.dataForm[response.property]=response.fullName;
          file.url=response.fullName;
          file.property=response.property;
        } else {
          this.$message.error("上传失败:"+response.msg)
        }
      },
      beforeRemove(file, fileList) {//公用，用于的文件上传
        return this.$confirm(`<#noparse>确定移除 ${ file.name }？</#noparse>`);
      },
      handleRemove(file, fileList) {//公用，用于的文件上传
         this.dataForm[file.property]=null;
      }
      </#if>
    },
    computed: {
    <#if uploadable==true>
      <#list propertyColumns as pc>
      <#if pc.uploadable==true>
      ${pc.property}_headers: function () {//用于${pc.property}的文件上传
        return {"token":this.$cookie.get("token"),group:"/${entitySimpleClassNameUncap}/${pc.property}",property:"${pc.property}"};
      },
      </#if>
	  </#list>
      upload_action:function(){//公用，用于的文件上传
        return this.$http.adornUrl(`/sys/document/upload`);
      }
    </#if>
    }
  }
</script>
