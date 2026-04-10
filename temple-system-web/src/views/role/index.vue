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

    <RoleForm
      v-model="drawerVisible"
      :is-edit="isEdit"
      :form-data="roleForm"
      @success="getRoleList"
    />
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { Plus } from '@element-plus/icons-vue'
import RoleForm from './RoleForm.vue'
import { roleService, Role, RoleDTO } from '@/api/role'

const loading = ref(false)
const roleList = ref<Role[]>([])
const drawerVisible = ref(false)
const isEdit = ref(false)

const roleForm = reactive<RoleDTO>({
  id: undefined,
  name: '',
  description: '',
  status: 1
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
  roleForm.id = undefined
  roleForm.name = ''
  roleForm.description = ''
  roleForm.status = 1
  drawerVisible.value = true
}

const handleEdit = async (row: Role) => {
  isEdit.value = true
  // 获取完整的角色信息（包含menus）
  try {
    const { code, data, msg } = await roleService.getById(row.id)
    if (code === 200) {
      roleForm.id = data.id
      roleForm.name = data.name
      roleForm.description = data.description
      roleForm.status = data.status
      roleForm.menuIds = data.menus?.map(menu => menu.id) || []
      drawerVisible.value = true
    } else {
      ElMessage.error('获取角色信息失败：' + msg)
    }
  } catch {
    ElMessage.error('获取角色信息失败')
  }
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
