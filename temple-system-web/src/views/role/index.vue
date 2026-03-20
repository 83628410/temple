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
        <el-table-column prop="description" label="角色描述" min-width="200" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'info'">{{ row.status === 1 ? '启用' : '禁用' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" min-width="180">
          <template #default="{ row }">
            {{ formatDateTime(row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column prop="updateTime" label="更新时间" min-width="180">
          <template #default="{ row }">
            {{ formatDateTime(row.updateTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" size="small" @click="handleEdit(row)">编辑</el-button>
            <el-button link type="danger" size="small" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 新增/编辑角色抽屉 -->
    <el-drawer v-model="drawerVisible" :title="isEdit ? '编辑角色' : '新增角色'" size="500px" direction="rtl">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="角色名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入角色名称" />
        </el-form-item>
        <el-form-item label="角色描述" prop="description">
          <el-input v-model="form.description" type="textarea" :rows="3" placeholder="请输入角色描述" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-space>
            <el-switch v-model="form.status" :active-value="1" :inactive-value="0" />
            <span class="text-gray-500 text-sm">{{ form.status === 1 ? '启用' : '禁用' }}</span>
          </el-space>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="handleCancel">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-drawer>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, nextTick } from 'vue'
import { Plus } from '@element-plus/icons-vue'
import type { FormInstance, FormRules } from 'element-plus'
import { roleService, RoleData, RoleDTO } from '@/api/role'

const loading = ref(false)
const roleList = ref<RoleData[]>([])
const drawerVisible = ref(false)
const isEdit = ref(false)
const formRef = ref<FormInstance>()

const form = ref<RoleDTO>({
  id: undefined,
  name: '',
  description: '',
  status: 1
})

const rules: FormRules = {
  name: [
    { required: true, message: '请输入角色名称', trigger: 'blur' },
    { min: 2, max: 50, message: '角色名称长度应在2-50个字符之间', trigger: 'blur' }
  ],
  description: [
    { max: 200, message: '角色描述最多200个字符', trigger: 'blur' }
  ]
}

// 获取角色列表
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

// 重置表单
const resetForm = () => {
  form.value = {
    id: undefined,
    name: '',
    description: '',
    status: 1
  }
}

// 处理新增角色
const handleAdd = () => {
  isEdit.value = false
  drawerVisible.value = true
  // 先清除验证状态，再重置表单数据
  nextTick(() => {
    formRef.value?.clearValidate()
    resetForm()
  })
}

// 处理编辑角色
const handleEdit = (row: RoleData) => {
  isEdit.value = true
  drawerVisible.value = true
  // 先清除验证状态，再设置表单数据
  nextTick(() => {
    formRef.value?.clearValidate()
    form.value = {
      id: row.id,
      name: row.name,
      description: row.description,
      status: row.status
    }
  })
}

// 处理取消
const handleCancel = () => {
  drawerVisible.value = false
  formRef.value?.resetFields()
}

// 处理提交
const handleSubmit = async () => {
  if (!formRef.value) return
  try {
    await formRef.value.validate()
    const { code, msg } = isEdit.value
      ? await roleService.update(form.value)
      : await roleService.save(form.value)
    if (code === 200) {
      ElMessage.success(isEdit.value ? '角色更新成功' : '角色新增成功')
      drawerVisible.value = false
      getRoleList()
      formRef.value?.resetFields()
    } else {
      ElMessage.error('操作失败：' + msg)
    }
  } catch {
    ElMessage.error('表单验证失败')
  }
}

// 处理删除角色
const handleDelete = (row: RoleData) => {
  ElMessageBox.confirm('确定要删除该角色吗？', '删除确认', { type: 'warning' }).then(async () => {
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
  }).catch(() => {})
}

// 格式化日期时间
const formatDateTime = (dateStr: string | undefined): string => {
  if (!dateStr) return '-'
  const date = new Date(dateStr)
  if (isNaN(date.getTime())) return dateStr
  const year = date.getFullYear()
  const month = String(date.getMonth() + 1).padStart(2, '0')
  const day = String(date.getDate()).padStart(2, '0')
  const hours = String(date.getHours()).padStart(2, '0')
  const minutes = String(date.getMinutes()).padStart(2, '0')
  const seconds = String(date.getSeconds()).padStart(2, '0')
  return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`
}

// 页面挂载时获取角色列表
onMounted(getRoleList)
</script>
