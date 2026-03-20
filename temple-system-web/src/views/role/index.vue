<template>
  <div class="space-y-4">
    <el-card shadow="never">
      <template #header>
        <span class="text-lg font-medium">角色管理</span>
      </template>
      <div class="mb-4">
        <el-button type="primary" :icon="Plus" @click="handleAdd">
          新增角色
        </el-button>
      </div>
      <el-table v-loading="loading" :data="roleList" border>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="name" label="角色名称" min-width="150" />
        <el-table-column prop="description" label="描述" min-width="200" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'info'">
              {{ row.status === 1 ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180">
          <template #default="{ row }">
            {{ formatDate(row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column prop="updateTime" label="更新时间" width="180">
          <template #default="{ row }">
            {{ formatDate(row.updateTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" size="small" @click="handleEdit(row)">编辑</el-button>
            <el-button link type="danger" size="small" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-drawer
      v-model="drawerVisible"
      :title="isEdit ? '编辑角色' : '新增角色'"
      direction="rtl"
      size="450px"
      :close-on-click-modal="false"
    >
      <el-form
        ref="formRef"
        :model="roleForm"
        :rules="rules"
        label-width="80px"
        label-position="right"
      >
        <el-form-item label="角色名称" prop="name">
          <el-input v-model="roleForm.name" placeholder="请输入角色名称" />
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input
            v-model="roleForm.description"
            type="textarea"
            :rows="3"
            placeholder="请输入角色描述"
          />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="roleForm.status">
            <el-radio :value="1">启用</el-radio>
            <el-radio :value="0">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <div style="flex: auto">
          <el-button @click="drawerVisible = false">取消</el-button>
          <el-button type="primary" :loading="submitLoading" @click="handleSubmit">
            确定
          </el-button>
        </div>
      </template>
    </el-drawer>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, nextTick } from 'vue'
import { Plus } from '@element-plus/icons-vue'
import type { FormInstance, FormRules } from 'element-plus'
import { roleService, Role, RoleDTO } from '@/api/role'

const loading = ref(false)
const submitLoading = ref(false)
const roleList = ref<Role[]>([])
const drawerVisible = ref(false)
const isEdit = ref(false)
const formRef = ref<FormInstance>()

const roleForm = reactive<RoleDTO>({
  id: undefined,
  name: '',
  description: '',
  status: 1
})

const rules = reactive<FormRules>({
  name: [
    { required: true, message: '请输入角色名称', trigger: 'blur' },
    { min: 2, max: 50, message: '长度在 2 到 50 个字符', trigger: 'blur' }
  ],
  status: [
    { required: true, message: '请选择状态', trigger: 'change' }
  ]
})

const formatDate = (dateStr?: string) => {
  if (!dateStr) return '-'
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

const getRoleList = async () => {
  loading.value = true
  try {
    const { code, data, msg } = await roleService.getList()
    if (code === 200) {
      roleList.value = data || []
    } else {
      ElMessage.error('获取角色列表失败：' + msg)
    }
  } catch {
    ElMessage.error('获取角色列表失败')
  } finally {
    loading.value = false
  }
}

const handleAdd = () => {
  isEdit.value = false
  resetForm()
  drawerVisible.value = true
}

const handleEdit = (row: Role) => {
  isEdit.value = true
  roleForm.id = row.id
  roleForm.name = row.name
  roleForm.description = row.description
  roleForm.status = row.status
  drawerVisible.value = true
  nextTick(() => {
    formRef.value?.clearValidate()
  })
}

const resetForm = () => {
  roleForm.id = undefined
  roleForm.name = ''
  roleForm.description = ''
  roleForm.status = 1
  formRef.value?.resetFields()
}

const handleSubmit = async () => {
  if (!formRef.value) return
  
  await formRef.value.validate(async (valid) => {
    if (!valid) return
    
    submitLoading.value = true
    try {
      const api = isEdit.value ? roleService.update : roleService.save
      const { code, msg } = await api(roleForm)
      
      if (code === 200) {
        ElMessage.success(isEdit.value ? '角色更新成功' : '角色创建成功')
        drawerVisible.value = false
        getRoleList()
      } else {
        ElMessage.error((isEdit.value ? '更新失败：' : '创建失败：') + msg)
      }
    } catch {
      ElMessage.error(isEdit.value ? '更新失败' : '创建失败')
    } finally {
      submitLoading.value = false
    }
  })
}

const handleDelete = (row: Role) => {
  ElMessageBox.confirm(`确定要删除角色"${row.name}"吗？`, '删除确认', { type: 'warning' })
    .then(async () => {
      try {
        const { code, msg } = await roleService.delete(row.id)
        if (code === 200) {
          ElMessage.success('角色删除成功')
          getRoleList()
        } else {
          ElMessage.error('删除失败：' + msg)
        }
      } catch {
        ElMessage.error('删除失败')
      }
    })
    .catch(() => {})
}

onMounted(getRoleList)
</script>
