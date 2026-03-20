<template>
  <div class="space-y-4">
    <el-card shadow="never">
      <template #header>
        <span class="text-lg font-medium">菜单管理</span>
      </template>
      <div class="mb-4">
        <el-button type="primary" :icon="Plus" @click="handleAdd">
          新增菜单
        </el-button>
      </div>
      <el-table v-loading="loading" :data="menuList" row-key="id" border
        :tree-props="{ children: 'children', hasChildren: 'hasChildren' }">
        <el-table-column prop="name" label="菜单名称" min-width="180" />
        <el-table-column prop="path" label="路径" min-width="180" />
        <el-table-column prop="component" label="组件" min-width="200" />
        <el-table-column prop="icon" label="图标" width="100">
          <template #default="{ row }">
            <el-icon v-if="row.icon"><component :is="row.icon" /></el-icon>
          </template>
        </el-table-column>
        <el-table-column prop="type" label="类型" width="100">
          <template #default="{ row }">
            <el-tag :type="row.type === 'M' ? 'primary' : 'success'">{{ row.type === 'M' ? '菜单' : '按钮' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="orderNum" label="排序" width="80" />
        <el-table-column prop="permission" label="权限标识" min-width="200" />
        <el-table-column prop="status" label="状态" width="80">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'info'">{{ row.status === 1 ? '显示' : '隐藏' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" size="small" @click="handleAddChild(row)">新增</el-button>
            <el-button link type="primary" size="small" @click="handleEdit(row)">编辑</el-button>
            <el-button link type="danger" size="small" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    <MenuForm v-model="dialogVisible" :is-edit="isEdit" :form-data="menuForm" :tree-data="treeMenuData"
      @success="getMenuList" />
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { Plus } from '@element-plus/icons-vue'
import { menuService, MenuData, MenuSaveRequest } from '@/api/menu'
import MenuForm from './MenuForm.vue'

interface Tree {
  id: number
  label: string
  children?: Tree[]
  original?: MenuData
}

const loading = ref(false)
const menuList = ref<MenuData[]>([])
const dialogVisible = ref(false)
const isEdit = ref(false)
const treeMenuData = ref<Tree[]>([])

const menuForm = ref<MenuSaveRequest>({
  id: 0, name: '', path: '', component: '', icon: '', parentId: 0, orderNum: 0, status: 1, permission: '',
  type: ''
})

// 递归转换菜单数据为 Tree 格式
const convertToTree = (menus: MenuData[]): Tree[] => {
  return menus.map(menu => ({
    id: menu.id,
    label: menu.name,
    children: menu.children ? convertToTree(menu.children) : [],
    original: menu
  }))
}

// 处理树形菜单数据结构
const processTreeData = (menuList: MenuData[]): Tree[] => {
  const treeData = convertToTree(menuList)
  return [{
    id: 0,
    label: '根菜单',
    children: treeData,
    original: { id: 0, name: '根菜单', path: '/', component: '', icon: '', orderNum: 0, type: 'C', parentId: -1, permission: '', status: 1 }
  }]
}

// 获取菜单列表
const getMenuList = async () => {
  loading.value = true
  try {
    const { code, data, msg } = await menuService.getList()
    if (code === 200) {
      menuList.value = data || []
      treeMenuData.value = processTreeData(data || [])
    } else {
      ElMessage.error('获取菜单列表失败：' + msg)
    }
  } catch {
    ElMessage.error('获取菜单列表失败')
  } finally {
    loading.value = false
  }
}

// 处理新增菜单
const handleAdd = () => {
  isEdit.value = false
  resetForm()
  dialogVisible.value = true
}

// 处理新增子菜单
const handleAddChild = (row: MenuData) => {
  isEdit.value = false
  resetForm()
  menuForm.value.parentId = row.id
  dialogVisible.value = true
}

// 处理编辑菜单
const handleEdit = (row: MenuData) => {
  isEdit.value = true
  menuForm.value = { ...row }
  dialogVisible.value = true
}

// 重置表单
const resetForm = () => {
  menuForm.value = { id: 0, name: '', path: '', component: '', icon: '', type: 'M', parentId: 0, orderNum: 0, status: 1, permission: '' }
}

// 处理删除菜单
const handleDelete = (row: MenuData) => {
  ElMessageBox.confirm('确定要删除该菜单吗？', '删除确认', { type: 'warning' }).then(async () => {
    try {
      const { code, msg } = await menuService.delete(row.id)
      if (code === 200) {
        ElMessage.success('菜单删除成功')
        getMenuList()
      } else {
        ElMessage.error('删除失败：' + msg)
      }
    } catch {
      ElMessage.error('删除失败')
    }
  }).catch(() => {})
}

// 页面挂载时获取菜单列表
onMounted(getMenuList)
</script>