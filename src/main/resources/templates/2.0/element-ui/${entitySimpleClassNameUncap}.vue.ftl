<template>
  <div class="mod-${entitySimpleClassNameUncap}">
    
    <el-form :inline="true" :model="dataForm" @keyup.enter.native="getDataList()">
      <#if cndable==true>
      <#list cndPropertys as pc>
      <#if pc.isDateProp==true>
      <el-form-item>
      <span class="demonstration">${pc.label}:</span>
      <el-date-picker
        v-model="dataForm.${pc.property}"
        type="daterange"
        align="right"
        unlink-panels
        value-format="yyyy-MM-dd"
        range-separator="至"
        start-placeholder="开始日期"
        end-placeholder="结束日期"
        :picker-options="pickerOptions">
      </el-date-picker>
      </el-form-item>
      <#elseif pc.isEnum==true>
      <el-form-item>
      <el-select v-model="dataForm.${pc.property}" clearable placeholder="${pc.label}">
        <el-option
          v-for="item in ${pc.property}_options"
          :key="item.value"
          :label="item.label"
          :value="item.value">
        </el-option>
      </el-select>
      </el-form-item>
      <#elseif pc.isFk==true>
      <el-form-item>
      <el-select v-model="dataForm.${pc.property}" clearable placeholder="${pc.label}">
        <el-option
          v-for="item in ${pc.property}_options"
          :key="item.value"
          :label="item.label"
          :value="item.value">
        </el-option>
      </el-select>
      </el-form-item>
      <#else>
      <el-form-item>
        <el-input v-model="dataForm.${pc.property}" placeholder="${pc.label}" clearable></el-input>
      </el-form-item>
      </#if>
      </#list>
      </#if>
      <el-form-item>
        <el-button v-if="isAuth('${module}:${entitySimpleClassNameUncap}:list')" @click="getDataList()">查询</el-button>
        <el-button v-if="isAuth('${module}:${entitySimpleClassNameUncap}:create')" type="primary" @click="addOrUpdateHandle()">新增</el-button>
        <el-button v-if="isAuth('${module}:${entitySimpleClassNameUncap}:remove')" type="danger" @click="deleteHandle()" :disabled="dataListSelections.length <= 0">批量删除</el-button>
      </el-form-item>
    </el-form>
    
    
    <el-table
      :data="dataList"
      border
      v-loading="dataListLoading"
      @selection-change="selectionChangeHandle"
      style="width: 100%;">
      <el-table-column
        type="selection"
        header-align="center"
        align="center"
        width="50">
      </el-table-column>
      <#list propertyColumns as pc>
      <#if pc.isEnum==true>
      <el-table-column
        prop="${pc.property}"
        header-align="center"
        align="center"
        label="${pc.label}">
        <template slot-scope="scope">
          <#if pc.enumValues??>
          <#assign  keys=pc.enumValues?keys/>
          <#list keys as key>
          <#if key?is_first>
            <el-tag v-if="scope.row.${pc.property} == '${key}'" size="small">${pc.enumValues[key]}</el-tag>
          <#else>
          	<el-tag v-else-if="scope.row.${pc.property} == '${key}'" size="small">${pc.enumValues[key]}</el-tag>
          </#if>
          </#list>
            <!--<el-tag v-else size="small"></el-tag>-->
          </#if>
        </template>
      </el-table-column>
      <#elseif pc.isId==true><#-- 如果是id的话 ，就不显示在表格上了-->
      <#else>
      <el-table-column
        prop="${pc.property}"
        header-align="center"
        align="center"
        label="${pc.label}">
      </el-table-column>
      </#if>
      </#list>
      <el-table-column
        fixed="right"
        header-align="center"
        align="center"
        width="150"
        label="操作">
        <template slot-scope="scope">
          <el-button v-if="isAuth('${module}:${entitySimpleClassNameUncap}:update')" type="text" size="small" @click="addOrUpdateHandle(scope.row.id)">修改</el-button>
          <el-button v-if="isAuth('${module}:${entitySimpleClassNameUncap}:remove')" type="text" size="small" @click="deleteHandle(scope.row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      @size-change="sizeChangeHandle"
      @current-change="currentChangeHandle"
      :current-page="pageIndex"
      :page-sizes="[10, 20, 50, 100]"
      :page-size="pageSize"
      :total="totalPage"
      layout="total, sizes, prev, pager, next, jumper">
    </el-pagination>
    <!-- 弹窗, 新增 / 修改 -->
    <add-or-update v-if="addOrUpdateVisible" ref="addOrUpdate" @refreshDataList="getDataList"></add-or-update>
  </div>
</template>

<script>
  import AddOrUpdate from './${entitySimpleClassNameUncap}-add-or-update'
  export default {
    data () {
      return {
        <#list cndPropertys as pc>
        <#if pc.isDateProp==true>
        pickerOptions: {
          shortcuts: [{
            text: '最近一周',
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 7);
              picker.$emit('pick', [start, end]);
            }
          }, {
            text: '最近一个月',
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
              picker.$emit('pick', [start, end]);
            }
          }, {
            text: '最近三个月',
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 90);
              picker.$emit('pick', [start, end]);
            }
          }]
        },
        <#elseif pc.isEnum==true>
        ${pc.property}_options: [//${pc.label}的下拉框数据
        <#if pc.enumValues??>
          <#assign  keys=pc.enumValues?keys/>
          <#list keys as key>
          {
            value: '${key}',
            label: '${pc.enumValues[key]}'
          }<#if key?has_next >,</#if>
          </#list>
        </#if>
        ],
        <#elseif pc.isFk==true && pc.cndable==true>
        ${pc.property}_options: [],
        </#if>
        </#list>
        dataForm: {
        <#list cndPropertys as pc>
          ${pc.property}: ''<#if pc?has_next >,</#if>
      	</#list>
        },
        dataList: [],
        pageIndex: 1,
        pageSize: 10,
        totalPage: 0,
        dataListLoading: false,
        dataListSelections: [],
        addOrUpdateVisible: false
      }
    },
    components: {
      AddOrUpdate
    },
    activated () {
      this.getDataList()
    },
    mounted() {
     <#list cndPropertys as pc>
     <#if pc.isFk==true && pc.cndable==true>
    	this.init_${pc.property}_options();
     </#if>
     </#list>
    },
    methods: {
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
      // 获取数据列表
      getDataList () {
      	if(!this.isAuth('${module}:${entitySimpleClassNameUncap}:list')){
          this.$notify.error({
              title: '错误',
              message: "沒有权限查询"
            });
          return;
        }
        this.dataListLoading = true
        this.$http({
          url: this.$http.adornUrl('/${module}/${entitySimpleClassNameUncap}/page'),
          method: 'get',
          params: this.$http.adornParams({
            'page': this.pageIndex,
            'limit': this.pageSize,
            <#list cndPropertys as pc>
	          '${pc.property}': this.dataForm.${pc.property}<#if pc?has_next>,</#if>
	      	</#list>
          })
        }).then(({data}) => {
          if (data && data.code === 0) {
            this.dataList = data.page.root;
            this.totalPage = data.page.total;
          } else {
            this.dataList = []
            this.totalPage = 0
            this.$notify.error({
              title: '错误',
              message: data.msg
            });
          }
          this.dataListLoading = false
        })
      },
      // 每页数
      sizeChangeHandle (val) {
        this.pageSize = val
        this.pageIndex = 1
        this.getDataList()
      },
      // 当前页
      currentChangeHandle (val) {
        this.pageIndex = val
        this.getDataList()
      },
      // 多选
      selectionChangeHandle (val) {
        this.dataListSelections = val
      },
      // 新增 / 修改
      addOrUpdateHandle (id) {
        this.addOrUpdateVisible = true
        this.$nextTick(() => {
          this.$refs.addOrUpdate.init(id)
        })
      },
      // 删除
      deleteHandle (id) {
        var ids = id ? [id] : this.dataListSelections.map(item => {
          return item.id
        })
        this.$confirm(`<#noparse>确定对[id=${ids.join(',')}]进行[${id ? '删除' : '批量删除'}]操作?</#noparse>`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$http({
            url: this.$http.adornUrl('/${module}/${entitySimpleClassNameUncap}/removeByIds'),
            method: 'post',
            data: this.$http.adornData(ids, false)
          }).then(({data}) => {
            if (data && data.code === 0) {
              this.$message({
                message: '操作成功',
                type: 'success',
                duration: 1500,
                onClose: () => {
                  this.getDataList()
                }
              })
            } else {
              this.$message.error(data.msg)
            }
          })
        }).catch(() => {})
      }
    }
  }
</script>
