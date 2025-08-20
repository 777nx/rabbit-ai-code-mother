<template>
  <div id="userResetPasswordPage">
    <h2 class="title">兔子 AI 应用生成 - 重置密码</h2>
    <div class="desc">不写一行代码，生成完整应用</div>
    <a-form :model="formState" name="basic" autocomplete="off" @finish="handleSubmit">
      <a-form-item
        name="userEmail"
        :rules="[
          { required: true, message: '请输入邮箱' },
          { type: 'email', message: '请输入正确的邮箱格式' },
        ]"
      >
        <a-input v-model:value="formState.userEmail" placeholder="请输入邮箱" />
      </a-form-item>
      <a-form-item
        name="code"
        :rules="[
          { required: true, message: '请输入验证码' },
          { len: 6, message: '验证码长度为 6 位' },
        ]"
      >
        <a-input v-model:value="formState.code" placeholder="请输入验证码">
          <template #suffix>
            <a-button type="primary" size="small" :disabled="countdown > 0" :loading="loading" @click="doSendEmail">
              {{ buttonText }}
            </a-button>
          </template>
        </a-input>
      </a-form-item>
      <a-form-item
        name="newPassword"
        :rules="[
          { required: true, message: '请输入新密码' },
          { min: 8, message: '密码不能小于 8 位' },
          { max: 16, message: '密码不能大于 16 位' },
        ]"
      >
        <a-input-password
          v-model:value="formState.newPassword"
          placeholder="请输入新密码"
          autocomplete
        />
      </a-form-item>
      <a-form-item
        name="checkPassword"
        :rules="[
          { required: true, message: '请确认密码' },
          { min: 8, message: '密码不能小于 8 位' },
          { max: 16, message: '密码不能大于 16 位' },
          { validator: validateCheckPassword },
        ]"
      >
        <a-input-password
          v-model:value="formState.checkPassword"
          placeholder="请确认密码"
          autocomplete
        />
      </a-form-item>
      <div class="tips">
        <a-row justify="space-between">
          <a-col>
            没有账号？
            <RouterLink to="/user/register">去注册</RouterLink>
          </a-col>
          <a-col>
            已有账号？
            <RouterLink to="/user/login">去登录</RouterLink>
          </a-col>
        </a-row>
      </div>
      <a-form-item>
        <a-button type="primary" html-type="submit" style="width: 100%">重置密码</a-button>
      </a-form-item>
    </a-form>
  </div>
</template>

<script setup lang="ts">
import { useRouter } from 'vue-router'
import { userResetPassword } from '@/api/userController'
import { sendVerificationCode } from '@/api/emailController'
import { message } from 'ant-design-vue'
import { computed, onBeforeUnmount, reactive } from 'vue'
import { ref } from 'vue'

const router = useRouter()

const formState = reactive<API.UserResetPasswordRequest>({
  userEmail: '',
  code: '',
  newPassword: '',
  checkPassword: '',
})

const loading = ref<boolean>(false)
const countdown = ref(0)
let timer: NodeJS.Timeout | null = null

// 按钮文本
const buttonText = computed(() => {
  return countdown.value > 0 ? `${countdown.value}秒后重试` : '发送验证码'
})

/**
 * 发送验证码
 */
const doSendEmail = async () => {
  loading.value = true
  // 校验
  if (!formState.userEmail) {
    message.error('请输入邮箱')
    loading.value = false
    return
  }
  try {
    const res = await sendVerificationCode({
      email: formState.userEmail,
    })
    if (res.data.code === 0) {
      message.success('验证码已发送，请注意查收')
      countdown.value = 60
      timer = setInterval(() => {
        countdown.value--
        if (countdown.value <= 0) {
          clearInterval(timer!)
        }
      }, 1000)
    } else {
      message.error('发送验证码失败：' + res.data.message)
    }
  } catch (error: any) {
    message.error('发送验证码失败：' + error.message)
  } finally {
    loading.value = false
  }
}

// 组件卸载前清除定时器
onBeforeUnmount(() => {
  if (timer) clearInterval(timer)
})

/**
 * 验证确认密码
 * @param rule
 * @param value
 * @param callback
 */
const validateCheckPassword = (rule: unknown, value: string, callback: (error?: Error) => void) => {
  if (value && value !== formState.newPassword) {
    callback(new Error('两次输入密码不一致'))
  } else {
    callback()
  }
}

/**
 * 提交表单
 * @param values
 */
const handleSubmit = async (values: API.UserResetPasswordRequest) => {
  try {
    const res = await userResetPassword(values)
    if (res.data.code === 0) {
      message.success('密码重置成功')
      router.push({
        path: '/user/login',
        replace: true,
      })
    } else {
      message.error('密码重置失败，' + res.data.message)
    }
  } catch (error: any) {
    message.error('密码重置失败：' + error.message)
  }
}
</script>

<style scoped>
#userResetPasswordPage {
  background: white;
  max-width: 720px;
  padding: 24px;
  margin: 24px auto;
}

.title {
  text-align: center;
  margin-bottom: 16px;
}

.desc {
  text-align: center;
  color: #bbb;
  margin-bottom: 16px;
}

.tips {
  margin-bottom: 16px;
  color: #bbb;
  font-size: 13px;
  text-align: right;
}
</style>
