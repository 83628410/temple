<template>
  <div class="space-y-4">
    <el-card shadow="never">
      <template #header>
        <span class="text-lg font-medium">用户管理</span>
      </template>
      <div class="mb-4">
        <el-button plain type="primary" :icon="Plus" @click="handleAdd">
          新增用户
        </el-button>
      </div>
      <el-table v-loading="loading" :data="userList" row-key="id" border>
        <el-table-column prop="username" label="用户名" min-width="120" />
        <el-table-column prop="nickname" label="昵称" min-width="120" />
        <el-table-column prop="email" label="邮箱" min-width="180" />
        <el-table-column prop="phone" label="手机号" min-width="140" />
        <el-table-column prop="status" label="状态" width="80" align="center">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'danger'">
              {{ row.status === 1 ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" min-width="180">
          <template #default="{ row }">
            {{ formatDate(row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="160" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" size="small" @click="handleEdit(row)">编辑</el-button>
            <el-button link type="danger" size="small" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    <UserForm v-model="dialogVisible" :is-edit="isEdit" :form-data="userForm" @success="getUserList" />
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { Plus } from '@element-plus/icons-vue'
import { userService, UserData, UserSaveRequest } from '@/api/user'
import UserForm from './UserForm.vue'

const loading = ref(false)
const userList = ref<UserData[]>([])
const dialogVisible = ref(false)
const isEdit = ref(false)

const userForm = ref<UserSaveRequest>({
  id: 0,
  username: '',
  password: '',
  nickname: '',
  email: '',
  phone: '',
  status: 1
})

// 格式化日期
const formatDate = (dateStr?: string) => {
  if (!dateStr) return ''
  const date = new Date(dateStr)
  return date.toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit',
    second: '2-digit'
  })
}

// 获取用户列表
const getUserList = async () => {
  loading.value = true
  try {
    const { code, data, msg } = await userService.getList()
    if (code === 200) {
      userList.value = data || []
    } else {
      ElMessage.error('获取用户列表失败：' + msg)
    }
  } catch {
    ElMessage.error('获取用户列表失败')
  } finally {
    loading.value = false
  }
}

// 处理新增用户
const handleAdd = () => {
  isEdit.value = false
  resetForm()
  dialogVisible.value = true
}

// 处理编辑用户
const handleEdit = (row: UserData) => {
  isEdit.value = true
  userForm.value = {
    id: row.id,
    username: row.username,
    nickname: row.nickname,
    email: row.email,
    phone: row.phone,
    status: row.status,
    roleIds: row.roles ? row.roles.map(role => role.id) : []
  }
  dialogVisible.value = true
}

// 重置表单
const resetForm = () => {
  userForm.value = {
    id: 0,
    username: '',
    password: '',
    nickname: '',
    email: '',
    phone: '',
    status: 1
  }
}

// 处理删除用户
const handleDelete = (row: UserData) => {
  ElMessageBox.confirm('确定要删除该用户吗？', '删除确认', { type: 'warning' }).then(async () => {
    try {
      const { code, msg } = await userService.delete(row.id)
      if (code === 200) {
        ElMessage.success('用户删除成功')
        getUserList()
      } else {
        ElMessage.error('删除失败：' + msg)
      }
    } catch {
      ElMessage.error('删除失败')
    }
  }).catch(() => {})
}

// 页面挂载时获取用户列表
onMounted(getUserList)
</script>
