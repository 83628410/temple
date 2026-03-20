<script setup lang="ts">
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import type { FormInstance, FormRules } from 'element-plus'
import { useUserStore } from '@/store/user'
import { loginService } from '@/api/auth'

const router = useRouter()
const userStore = useUserStore()

const formRef = ref<FormInstance>()
const loading = ref(false)

const loginForm = reactive({
  username: 'admin',
  password: 'admin123'
})

const rules = reactive<FormRules>({
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '用户名长度在 3 到 20 个字符', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 20, message: '密码长度在 6 到 20 个字符', trigger: 'blur' }
  ]
})

const handleLogin = async () => {
  if (!formRef.value) return

  await formRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      try {
        // 调用登录接口
        const response = await loginService.login(loginForm)
        // 登录成功
        const {token} = response.data
        userStore.setToken(token)
        ElMessage.success('登录成功')
        router.push('/dashboard')
      } catch (error) {
        // 错误已在响应拦截器中处理
      } finally {
        loading.value = false
      }
    }
  })
}

const resetForm = () => {
  formRef.value?.resetFields()
}
</script>

<template>
  <div class="login-container">
    <div class="login-box">
      <div class="login-header">
        <h2>系统登录</h2>
      </div>
      <el-form ref="formRef" :model="loginForm" :rules="rules" class="login-form" size="large">
        <el-form-item prop="username">
          <el-input v-model="loginForm.username" placeholder="请输入用户名" prefix-icon="User" clearable />
        </el-form-item>
        <el-form-item prop="password">
          <el-input v-model="loginForm.password" type="password" placeholder="请输入密码" prefix-icon="Lock" show-password
            @keyup.enter="handleLogin" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" class="login-btn" :loading="loading" @click="handleLogin">
            登 录
          </el-button>
        </el-form-item>
        <el-form-item>
          <el-button type="default" @click="resetForm">
            重置
          </el-button>
        </el-form-item>
      </el-form>
      <div class="login-tips">
        <p>提示：用户名 admin，密码 admin123</p>
      </div>
    </div>
  </div>
</template>

<style lang="scss" scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.login-box {
  width: 400px;
  padding: 40px;
  background: #fff;
  border-radius: 10px;
  box-shadow: 0 10px 50px rgba(0, 0, 0, 0.1);
}

.login-header {
  margin-bottom: 30px;
  text-align: center;

  h2 {
    margin: 0;
    font-size: 28px;
    font-weight: 600;
    color: #333;
  }
}

.login-form {
  .el-form-item {
    margin-bottom: 25px;
  }
}

.login-btn {
  width: 100%;
}

.login-tips {
  margin-top: 20px;
  text-align: center;

  p {
    margin: 0;
    font-size: 12px;
    color: #999;
  }
}
</style>
