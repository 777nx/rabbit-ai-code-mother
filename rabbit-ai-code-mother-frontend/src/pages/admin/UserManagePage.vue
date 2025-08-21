<template>
  <div id="userManagePage">
    <!-- 搜索表单 -->
    <a-form layout="inline" :model="searchParams" @finish="doSearch">
      <a-form-item label="账号">
        <a-input v-model:value="searchParams.userAccount" placeholder="输入账号" />
      </a-form-item>
      <a-form-item label="邮箱">
        <a-input v-model:value="searchParams.userEmail" placeholder="输入邮箱" />
      </a-form-item>
      <a-form-item label="用户名">
        <a-input v-model:value="searchParams.userName" placeholder="输入用户名" />
      </a-form-item>
      <a-form-item>
        <a-button type="primary" html-type="submit">搜索</a-button>
      </a-form-item>
    </a-form>
    <a-divider />
    <!-- 表格 -->
    <a-table
      :columns="columns"
      :data-source="data"
      :pagination="pagination"
      @change="doTableChange"
    >
      <template #bodyCell="{ column, record }">
        <template v-if="column.dataIndex === 'userAvatar'">
          <div v-if="editableData[record.id]">
            <a-input v-model:value="editableData[record.id].userAvatar" placeholder="输入头像URL" />
          </div>
          <div v-else>
            <a-avatar :src="record.userAvatar">
              {{ record.userName?.charAt(0) || 'U' }}
            </a-avatar>
          </div>
        </template>
        <template v-else-if="column.dataIndex === 'userRole'">
          <div v-if="editableData[record.id]">
            <a-select v-model:value="editableData[record.id].userRole" style="width: 120px">
              <a-select-option value="user">普通用户</a-select-option>
              <a-select-option value="admin">管理员</a-select-option>
            </a-select>
          </div>
          <div v-else>
            <a-tag :color="record.userRole === 'admin' ? 'green' : 'blue'">
              {{ record.userRole === 'admin' ? '管理员' : '普通用户' }}
            </a-tag>
          </div>
        </template>
        <template v-else-if="column.dataIndex === 'createTime'">
          {{ dayjs(record.createTime).format('YYYY-MM-DD HH:mm:ss') }}
        </template>
        <template v-else-if="column.key === 'action'">
          <div v-if="editableData[record.id]">
            <a-space>
              <a-button type="link" @click="save(record.id)">保存</a-button>
              <a-button type="link" @click="cancel(record.id)">取消</a-button>
            </a-space>
          </div>
          <div v-else>
            <a-button type="link" @click="edit(record.id)">编辑</a-button>
            <a-button danger @click="doDelete(record.id)">删除</a-button>
          </div>
        </template>
        <template v-else-if="column.dataIndex === 'userName'">
          <div v-if="editableData[record.id]">
            <a-input v-model:value="editableData[record.id].userName" />
          </div>
          <div v-else>
            {{ record.userName }}
          </div>
        </template>
        <template v-else-if="column.dataIndex === 'userProfile'">
          <div v-if="editableData[record.id]">
            <a-input v-model:value="editableData[record.id].userProfile" />
          </div>
          <div v-else>
            {{ record.userProfile }}
          </div>
        </template>
      </template>
    </a-table>
  </div>
</template>
<script lang="ts" setup>
import { computed, onMounted, reactive, ref } from 'vue'
import { deleteUser, listUserVoByPage, updateUser } from '@/api/userController.ts'
import { message } from 'ant-design-vue'
import dayjs from 'dayjs'
import { cloneDeep } from 'lodash'

// 展示的列
const columns = [
  {
    title: 'id',
    dataIndex: 'id',
  },
  {
    title: '账号',
    dataIndex: 'userAccount',
  },
  {
    title: '邮箱',
    dataIndex: 'userEmail',
  },
  {
    title: '用户名',
    dataIndex: 'userName',
  },
  {
    title: '头像',
    dataIndex: 'userAvatar',
  },
  {
    title: '简介',
    dataIndex: 'userProfile',
  },
  {
    title: '用户角色',
    dataIndex: 'userRole',
  },
  {
    title: '创建时间',
    dataIndex: 'createTime',
  },
  {
    title: '操作',
    key: 'action',
  },
]

// 展示的数据
const data = ref<API.UserVO[]>([])
const total = ref(0)

// 搜索条件
const searchParams = reactive<API.UserQueryRequest>({
  pageNum: 1,
  pageSize: 10,
})

// 获取数据
const fetchData = async () => {
  const res = await listUserVoByPage({
    ...searchParams,
  })
  if (res.data.data) {
    data.value = res.data.data.records ?? []
    total.value = res.data.data.totalRow ?? 0
  } else {
    message.error('获取数据失败：' + res.data.message)
  }
}

// 分页参数
const pagination = computed(() => {
  return {
    current: searchParams.pageNum ?? 1,
    pageSize: searchParams.pageSize ?? 10,
    total: Number(total.value), // 注意：total 需要是 number 类型
    showSizeChanger: true,
    showTotal: (total: number) => `共 ${total} 条`,
  }
})

// 表格分页变化时的操作
const doTableChange = (page: { current: number; pageSize: number }) => {
  searchParams.pageNum = page.current
  searchParams.pageSize = page.pageSize
  fetchData()
}

// 搜索数据
const doSearch = async () => {
  // 重置页码
  searchParams.pageNum = 1
  fetchData()
}

// 删除数据
const doDelete = async (id: number) => {
  if (!id) {
    return
  }
  const res = await deleteUser({ id })
  if (res.data.code === 0) {
    message.success('删除成功')
    // 刷新数据
    fetchData()
  } else {
    message.error('删除失败：' + res.data.message)
  }
}

// 编辑表单
const editableData = reactive<Record<string, API.UserVO>>({})

// 编辑操作
const edit = (key: number) => {
  editableData[key] = cloneDeep(data.value.filter((item) => key === item.id)[0])
}

// 保存操作
const save = async (key: number) => {
  const editedUser = editableData[key]
  if (!editedUser) {
    message.error('编辑失败：用户数据不存在')
    return
  }
  const res = await updateUser(editedUser)
  if (res.data.code === 0) {
    message.success('更新成功')
    Object.assign(data.value.filter((item) => key === item.id)[0], editedUser)
    delete editableData[key]
  } else {
    message.error('更新失败：' + res.data.message)
  }
}

// 取消操作
const cancel = (key: number) => {
  delete editableData[key]
}

// 页面加载时请求一次
onMounted(() => {
  fetchData()
})
</script>

<style scoped>
#userManagePage {
  padding: 24px;
  background: white;
  margin-top: 16px;
}
</style>
