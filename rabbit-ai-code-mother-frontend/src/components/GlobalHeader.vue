<template>
  <a-layout-header class="header">
    <a-row :wrap="false">
      <!-- 左侧：Logo和标题 -->
      <a-col flex="200px">
        <RouterLink to="/">
          <div class="header-left">
            <img class="logo" src="@/assets/logo.png" alt="Logo" />
            <h1 class="site-title">兔子应用生成</h1>
          </div>
        </RouterLink>
      </a-col>
      <!-- 中间：导航菜单 -->
      <a-col flex="auto">
        <a-menu
          v-model:selectedKeys="selectedKeys"
          mode="horizontal"
          :items="menuItems"
          @click="handleMenuClick"
        />
      </a-col>
      <!-- 右侧：用户操作区域 -->
      <a-col>
        <div class="user-login-status">
          <div v-if="loginUserStore.loginUser.id">
            <a-dropdown>
              <a-space>
                <a-avatar :src="loginUserStore.loginUser?.userAvatar">
                  {{ loginUserStore.loginUser?.userName?.charAt(0) || 'U' }}
                </a-avatar>
                {{ loginUserStore.loginUser.userName ?? '无名' }}
              </a-space>
              <template #overlay>
                <a-menu>
                  <a-menu-item key="info" :disabled="editingName">
                    <a-space>
                      <a-avatar :src="loginUserStore.loginUser?.userAvatar">
                        {{ loginUserStore.loginUser?.userName?.charAt(0) || 'U' }}
                      </a-avatar>
                      <div>
                        <div v-if="!editingName" style="display: flex; align-items: center;">
                          <span>{{ loginUserStore.loginUser.userName ?? '无名' }}</span>
                          <EditOutlined @click="doEdit" style="margin-left: 8px; cursor: pointer;" />
                        </div>
                        <div v-else style="display: flex; align-items: center;">
                          <a-input v-model:value="newUserName" style="width: 120px;" @pressEnter="saveUserName" />
                          <SaveOutlined @click="saveUserName" style="margin-left: 8px; cursor: pointer;" />
                          <CloseOutlined @click="cancelEdit" style="margin-left: 8px; cursor: pointer;" />
                        </div>
                        <div style="font-size: 12px; color: #999;">
                          ID: {{ loginUserStore.loginUser.id }}
                          <CopyOutlined @click="copyUserId" style="cursor: pointer; margin-left: 4px;" />
                        </div>
                      </div>
                    </a-space>
                  </a-menu-item>
                  <a-menu-item @click="doLogout">
                    <LogoutOutlined />
                    退出登录
                  </a-menu-item>
                </a-menu>
              </template>
            </a-dropdown>
          </div>
          <div v-else>
            <a-button type="primary" href="/user/login">登录</a-button>
          </div>
        </div>
      </a-col>
    </a-row>
  </a-layout-header>
</template>

<script setup lang="ts">
import { computed, h, ref } from 'vue'
import { useRouter } from 'vue-router'
import { type MenuProps, message } from 'ant-design-vue'
import { useLoginUserStore } from '@/stores/loginUser.ts'
import {
  LogoutOutlined,
  HomeOutlined,
  CopyOutlined,
  EditOutlined,
  SaveOutlined,
  CloseOutlined,
} from '@ant-design/icons-vue'
import { userLogout, updateUser } from '@/api/userController.ts'

// 获取登录用户状态
const loginUserStore = useLoginUserStore()

const router = useRouter()
// 当前选中菜单
const selectedKeys = ref<string[]>(['/'])
const editingName = ref(false)
const newUserName = ref('')

// 监听路由变化，更新当前选中菜单
router.afterEach((to, from, next) => {
  selectedKeys.value = [to.path]
})

// 菜单配置项
const originItems = [
  {
    key: '/',
    icon: () => h(HomeOutlined),
    label: '主页',
    title: '主页',
  },
  {
    key: '/admin/userManage',
    label: '用户管理',
    title: '用户管理',
  },
  {
    key: '/admin/appManage',
    label: '应用管理',
    title: '应用管理',
  },
  {
    key: '/admin/chatManage',
    label: '对话管理',
    title: '对话管理',
  },
  {
    key: 'others',
    label: h('a', { href: 'https://www.777nx.cn', target: '_blank' }, '贝果研究家'),
    title: '贝果研究家',
  },
]

// 过滤菜单项
const filterMenus = (menus = [] as MenuProps['items']) => {
  return menus?.filter((menu) => {
    const menuKey = menu?.key as string
    if (menuKey?.startsWith('/admin')) {
      const loginUser = loginUserStore.loginUser
      if (!loginUser || loginUser.userRole !== 'admin') {
        return false
      }
    }
    return true
  })
}

// 展示在菜单的路由数组
const menuItems = computed<MenuProps['items']>(() => filterMenus(originItems))

// 处理菜单点击
const handleMenuClick: MenuProps['onClick'] = (e) => {
  const key = e.key as string
  selectedKeys.value = [key]
  // 跳转到对应页面
  if (key.startsWith('/')) {
    router.push(key)
  }
}

// 编辑用户名
const doEdit = () => {
  newUserName.value = loginUserStore.loginUser.userName ?? ''
  editingName.value = true
}

// 保存用户名
const saveUserName = async () => {
  if (!newUserName.value.trim()) {
    message.error('用户名不能为空')
    return
  }
  const res = await updateUser({
    userName: newUserName.value,
    id: loginUserStore.loginUser.id,
  })
  if (res.data.code === 0) {
    message.success('用户名更新成功')
    await loginUserStore.fetchLoginUser()
    editingName.value = false
  } else {
    message.error('用户名更新失败: ' + res.data.message)
    editingName.value = false
  }
}

const cancelEdit = () => {
  editingName.value = false
  newUserName.value = loginUserStore.loginUser.userName ?? ''
}

const copyUserId = () => {
  if (loginUserStore.loginUser.id) {
    navigator.clipboard.writeText(loginUserStore.loginUser.id.toString())
    message.success('用户ID已复制')
  }
}

// 退出登录
const doLogout = async () => {
  const res = await userLogout()
  if (res.data.code === 0) {
    loginUserStore.setLoginUser({
      userName: '未登录',
    })
    message.success('退出登录成功')
    await router.push('/user/login')
  } else {
    message.error('退出登录失败' + res.data.message)
  }
}
</script>

<style scoped>
.header {
  background: #fff;
  padding: 0 24px;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 12px;
}

.logo {
  height: 48px;
  width: 48px;
}

.site-title {
  margin: 0;
  font-size: 18px;
  color: #1890ff;
}

.ant-menu-horizontal {
  border-bottom: none !important;
}
</style>
