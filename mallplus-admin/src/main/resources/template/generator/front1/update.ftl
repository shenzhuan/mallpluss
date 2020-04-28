<style lang="less" scoped>
  @import "./reduction.less";
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
        <div class="currentTitle">
          ${prefix}/
          <label>编辑${prefix}</label>
        </div>
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
        <Button type="primary" :loading="loading" class="comBtn" @click.native="save(${changeClassName})">
          保存
        </Button>
        <Button type="default" class="comBtn" @click="prev()">取消</Button>
      </div>
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
  name: '${className}Update',
  	data() {
  		return {
  			loading: false,
  			lenght: 0,
  			id:0,
  			currentIndex: 0,
  			${changeClassName}: {},
  			reduRule: {
  				name: [
  					{
  						required: true,
  						message: '名称不能为空',
  						trigger: 'blur'
  					}
  				]
  			}
  		};
  	},
  	created() {
  		this.restore();
  		this.id = this.$route.query.id;
  		if (this.id) {
  			this.init();
  		}
  	},
  	methods: {
  		// 获取商品的规格
  		getDetail(id) {
  			let _this = this;
  			get${className}(id).then(res => {
  				if (res.code == 200) {
  					_this.${changeClassName} = res.data;
  				}
  			});
  		},

  		save(reduForm) {
  			update${className}(this.$route.query.id, this.${changeClassName}).then(response => {
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
  		init: function() {
  			this.getDetail(this.id);
  		},
  		restore() {
  			this.showData = [];
  		},
  		activated() {
  			this.restore();
  			this.id = this.$route.query.id;
  			if (this.id) {
  				this.init();
  			}
  		},

  	}
  };
  </script>
