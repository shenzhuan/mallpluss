<template>
	<div class="container">
		<div class="currentTitle">${prefix}</div>
		<Card>
		 <div class="header" slot="title">
            <div class="header-box line-bottom">
              <Select size="small" placeholder="请选择性别" v-model="listQuery.sex" style="width:150px;margin-right: 5px;">
                <Option v-for="item in sexList" :value="item.value" :key="item.value">{{ item.label }}</Option>
              </Select>
              <Input
                size="small"
                placeholder="请输入手机号查询"
                :maxlength="11"
                v-model="listQuery.name"
                style="width: 200px;margin-right: 5px;"
              />
              <Button size="small" type="primary" @click="search">查询</Button>

            	<router-link :to="{ name: 'add${className}' }">
            						<Button type="primary" size="small" v-if="hasRole(101)" class="comBtn" style="margin-left:0">+ 新增</Button>
            					</router-link>
            </div>
          </div>
			<div class="wrapper">

				<div class="item2">
					<!-- 表格  stripe斑马纹-->
					<Table :data="list" :columns="tableColumns1" style="margin-right:16px;" stripe border></Table>
					<!-- 分页 -->
					<div style="margin: 10px;overflow: hidden">
						<div style="float: right;">
							<Page
								:total="total"
								:page-size="pageSize"
								:current="current"
								@on-change="changepage"
								show-sizer
								:page-size-opts="[10, 15, 20]"
								@on-page-size-change="pageSizeFun"
								show-elevator
								show-total
								style="margin-right:16px;"
							></Page>
						</div>
					</div>
				</div>
			</div>
		</Card>


	</div>
</template>

<script>
 import {fetchList, delete${className}} from '@/api/${moduleName}/${changeClassName}'
    import {formatDate} from '@/utils/date';
export default {
	name: '${changeClassName}List',
	data() {
		return {
		   sexList: [
            {
              value: "3",
              label: "全部性别"
            },
            {
              value: "1",
              label: "男"
            },
            {
              value: "2",
              label: "女"
            }
         ],

			tableData1: [],
			pageSize: 10, //每页显示多少条
			total: 0, //总条数
			current: 1, //当前页
			// 临时数据存放
			list: [],
			//临时存放id
			tempId: '',
			// 商品ID
			goodID: [],
			 listQuery: {
                         name: null,
                         pageNum: 1,
                         pageSize: 10
                  },
			// 表头
			tableColumns1: [
			 <#if columns??>
                                <#list columns as column>
                                    <#if column.columnShow = 'true'>
                                         {
                                    			title: '<#if column.columnComment != ''>${column.columnComment}<#else>${column.changeColumnName}</#if>',
                                    			key: '${column.changeColumnName}',
                                    			align: 'center',
                                    			width: 100
                                    	},

                                    </#if>
                                </#list>
                            </#if>


				{
					title: '状态',
					key: 'status',
					align: 'center',
					width: 150,
					render: (h, params) => {
						let status = params.row.status;
						return h('div', [
							status == '0'
								? h(
										'Button',
										{
											props: {
												type: 'primary',
												size: 'small'
											},
											style: {}
										},
										'已启用'
								  )
								: status == '1'
								? h(
										'Button',
										{
											props: {
												type: 'error',
												size: 'small'
											}
										},
										'未启用'
								  )
								: null
						]);
					}
				},
				{
					title: '操作',
					key: 'mark',
					align: 'center',

					render: (h, params) => {
						let status = params.row.status;
						return h(
							'div',
							{
								style: {
									display: 'flex',
									flexDirection: 'row',
									alignItems: 'center',
									justifyContent: 'center',
									color: '#0083b0',
									'font-size': '13px'
								}
							},
							[
								h(
									'div',
									{
										class: '',
										style: {
											'border-right': this.hasRole(4010402) ? '1px solid #0083b0' : 'none',
											'padding-right': '10px',
											cursor: 'pointer',
											display: this.hasRole(40104) ? 'block' : 'none'
										},
										on: {
											click: () => {
												this.editGroup(params.row.id);
											}
										}
									},
									'编辑'
								),
								h(
									'div',
									{
										class: '',
										style: {
											'border-right': '1px solid #0083b0',
											padding: '0 10px',
											cursor: 'pointer',
											display: this.hasRole(4010402) ? 'block' : 'none'
										},
										on: {
											click: index => {
												this.updateStatus(params, index);
											}
										}
									},
									params.row.status == 0 ? '禁用' : '启用'
								),

								h(
									'div',
									{
										class: '',
										style: {
											padding: '0 10px',
											cursor: 'pointer',
											display: this.hasRole(4010401) ? 'block' : 'none'
										},
										on: {
											click: index => {
													this.$Modal.confirm({
														title: '确认提示',
														content: '<p>是否确认删除这个活动</p>',
														onOk: () => {
															this.removeDate(params, index);
														},
														onCancel: () => {}
													});

											}
										}
									},
									'删除'
								)
							]
						);
					}
				}
			]
		};
	},
	created() {
		this.switchFun(); //再次调用获取列表数据方法
	},

	methods: {
		//分页
        		changepage(index) {
        			this.current = index;
        			this.listQuery.pageSize=index;
        			this.switchFun();
        			console.log(this.list);
        		},
        		pageSizeFun(data) {
        			console.log(data);
        			this.listQuery.pageSize = data;
        			this.switchFun();
        		},

		// 弹出框接口
		updateStatus(params, index) {
			this.tempId = this.list[params.index].id;
			let tempStatus = this.list[params.index].status ? 0 : 1;
			this.stateChange(params, index);
		},

		// 修改状态
		stateChange(params, index) {
			this.tempId = this.list[params.index].id;
			let tempStatus = this.list[params.index].status ? 0 : 1;
			updateStatus({
					id: this.tempId,
					status: tempStatus
				})
				.then(res => {
					console.log(res, '222');
					console.log(tempStatus, '444');
					if (res.code == 200) {
						this.switchFun();
						this.$Message.success(res.message);
					} else {
						this.$Message.error(res.message);
					}
				})
				.catch(error => {
					console.log(error);
				});
		},

		//删除
		removeDate(params, index) {
			delete${className}(params.row.id)
				.then(res => {
					if (res.code == 200) {
						this.$Message.success('删除成功');
						this.switchFun();
					} else {
						this.$Message.info(res.message);
					}
				})
				.catch(err => {
					this.$Message.error('删除失败');
				});
		},

		// 编辑

		editGroup(index) {
			console.log(index);
			this.$router.push({
				path: '/@/views/${moduleName}/${changeClassName}/components/edit',
				query: { id: index }
			});
		},

		// 获取数据的接口函数
		switchFun(index) {
			fetchList(this.listQuery).then(response => {
				this.listLoading = false;
				this.list = response.data.records;
				this.total = response.data.total;
				this.totalPage = response.data.pages;
				this.pageSize = response.data.size;
			});
		},
		  // 搜索
        search() {
          this.listQuery.pageNum = 1;
          this.switchFun();
        },
	},
	activated() {
		console.log('memberLevelList');
		this.switchFun(); //再次调用获取列表数据方法
	}
};
</script>
<style scoped>
.container {
	width: 100%;
	height: 100%;
}

.container .title {
	height: 30px;
	line-height: 30px;
	text-indent: 20px;
	font-size: 16px;
}

.container .wrapper {
	/*margin: 0px 17px 0 17px;*/
	box-sizing: border-box;
	background: #fff;
	padding: 15px 0 50px 0;
	margin: -16px;
}

.container .tab {
	height: 1000px;
	background: #fff;
	padding-top: 16px;
	border-left: 1px solid #c9c9c9;
	border-right: 1px solid #c9c9c9;
}

.container .item1 {
	border-bottom: 1px solid #c9c9c9;
	padding-bottom: 10px;
	padding-left: 10px;
}

.container .comBtn {
	margin: 0 30px;
}

.container .fullBtn {
	margin-left: 230px;
}

.container .item2 {
	margin-top: 16px;
	margin-left: 10px;
}
.ivu-card-body {
	padding: 0px;
}
.currentTitle {
	font-size: 13px;
	color: #000;
	padding: 4px 0;
	background: #f8fbfb;
}
</style>
