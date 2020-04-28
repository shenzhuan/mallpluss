<style lang="less" scoped>
  @import './reduction.less';

  .currentTitle {
    font-size: 13px;
    padding: 4px 0;
    background: #f8fbfb;
  }

  .currentTitle label {
    font-size: 14px;
    color: #000;
  }

</style>
<template>
  <div class="container">
    <div class="redu-box">
      <div class="redu-inset">
        <div class="currentTitle">${prefix}/<label> 创建${prefix}</label></div>
        <Card>
           <Form :label-width="150" ref="reduForm" :model="${changeClassName}" :rules="reduRule"
                style="border: 1px solid #dcdcdc;">
                  <div class="redu-title">活动设置</div>
                    <div class="inset-content">
                     <#if columns??>
                                    <#list columns as column>
                                        <FormItem label="<#if column.columnComment != ''>${column.columnComment}<#else>${column.changeColumnName}</#if>" prop="${column.changeColumnName}">
                                                 <Input v-model="${changeClassName}.${column.changeColumnName}" placeholder="请输入${column.columnComment}"  style="width: 350px;"></Input>
                                          </FormItem>
                                    </#list>
                                </#if>
                     </div>
           </Form>
        </Card>
      </div>
      <!--保存 取消 -->
      <div class="formBtn">
        <Button type="primary" class="comBtn" :loading="loading" @click.native="save('${className}Form')">
         保存
        </Button>
        <Button type="default" class="comBtn" @click="prev()">取消</Button>
      </div>
    </div>

  </div>
</template>

<script>
 import {create${className}, get${className}, update${className}} from '@/api/${moduleName}/${changeClassName}'
   const default${className}={
       name: ''
     };
 export default {
 	name: '${className}Add',
 	data() {
 		return {
 			loading: false,
 			${changeClassName}: {},
 			reduRule: {
 				// 对于单个字段来说，通常需要多个验证规则，这些规则以数组展示
 				name: [
 					{
 						required: true,
 						message: '名称不能为空',
 						trigger: 'blur'
 					}
 				]
 			},
 			lenght: 0,
 			currentIndex: 0
 		};
 	},
 	methods: {
 		// 保存
 		save(reduForm) {
 			create${className}(this.${changeClassName}).then(response => {
 				if (response.code == 200) {

 					this.$Message.success('保存成功');
 					this.$router.back();
 				} else {
 					this.$Message.error(response.msg);
 				}
 			});
 		},
 		backList() {
 			this.$router.push('/@/views/${moduleName}/${changeClassName}/index');
 		},
 		prev() {
 			this.$router.go(-1);
 		},

 		//清空数据
 		restore() {
 			this.showData = [];
 			this.reduForm = {};
 		}
 	},
 	activated() {
 		this.restore();
 	},

 	watch: {
 		'reduForm.name': function(val) {
 			var that = this;
 			that._data.lenght = val.length;
 		}
 	}
 };
 </script>
