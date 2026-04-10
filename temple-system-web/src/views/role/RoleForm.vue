<template>
  <el-drawer v-model="visible" :title="isEdit ? '编辑角色' : '新增角色'" append-to-body size="600px" direction="rtl">
    <el-form ref="formRef" :model="form" :rules="rules" label-width="80px" label-position="right">
      <el-form-item label="角色名称" prop="name">
        <el-input v-model="form.name" placeholder="请输入角色名称" />
      </el-form-item>
      <el-form-item label="描述" prop="description">
        <el-input v-model="form.description" type="textarea" :rows="3" placeholder="请输入角色描述" />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-radio-group v-model="form.status">
          <el-radio :value="1">启用</el-radio>
          <el-radio :value="0">禁用</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="菜单分配" prop="menuIds">
        <el-tree ref="menuTreeRef" :data="menuTreeData" :props="{ label: 'label', children: 'children' }" node-key="id"
          show-checkbox :check-strictly="true" :default-checked-keys="checkedMenuIds" @check="handleMenuCheck"
          class="w-full" />
      </el-form-item>
    </el-form>
    <template #footer>
      <div style="flex: auto">
        <el-button @click="handleCancel">取消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="handleSubmit">
          确定
        </el-button>
      </div>
    </template>
  </el-drawer>
</template>

<script setup lang="ts">
import type { FormInstance, FormRules } from 'element-plus'
import { roleService, RoleDTO } from '@/api/role'
import { menuService, MenuData } from '@/api/menu'

interface MenuTree {
  id: number
  label: string
  children?: MenuTree[]
}

const props = defineProps<{
  modelValue: boolean
  isEdit: boolean
  formData: RoleDTO
}>()

const emit = defineEmits<{
  'update:modelValue': [value: boolean]
  'success': []
}>()

const visible = computed({
  get: () => props.modelValue,
  set: (val) => emit('update:modelValue', val)
})

const formRef = ref<FormInstance>()
const menuTreeRef = ref()
const form = ref<RoleDTO>({ ...props.formData })
const submitLoading = ref(false)
const menuTreeData = ref<MenuTree[]>([])
const checkedMenuIds = ref<number[]>([])

const rules: FormRules = {
  name: [
    { required: true, message: '请输入角色名称' },
    { min: 2, max: 50, message: '长度在 2 到 50 个字符' }
  ],
  status: [
    { required: true, message: '请选择状态' }
  ]
}

// 递归转换菜单数据为 Tree 格式
const convertToTree = (menus: MenuData[]): MenuTree[] => {
  return menus.map(menu => ({
    id: menu.id,
    label: menu.title,
    children: menu.children ? convertToTree(menu.children) : []
  }))
}

// 处理树形菜单数据结构
const processTreeData = (menuList: MenuData[]): MenuTree[] => {
  return convertToTree(menuList)
}

// 获取菜单列表
const getMenuList = async () => {
  try {
    const { code, data, msg } = await menuService.getList()
    if (code === 200) {
      menuTreeData.value = processTreeData(data || [])
    } else {
      ElMessage.error('获取菜单列表失败：' + msg)
    }
  } catch {
    ElMessage.error('获取菜单列表失败')
  }
}

// 获取已选中的菜单ID
const getCheckedMenuIds = () => {
  if (!menuTreeRef.value) return []
  // 只获取全选的节点ID
  return menuTreeRef.value.getCheckedKeys()
}

// 处理菜单选中事件
const handleMenuCheck = (data: MenuTree, checkedInfo: any) => {
  const isChecked = checkedInfo.checkedKeys.includes(data.id)

  if (isChecked && data.children && data.children.length > 0) {
    // 选中父节点时，自动选中所有子节点
    const allChildrenIds = getAllChildrenIds(data)
    menuTreeRef.value.setCheckedKeys([...checkedInfo.checkedKeys, ...allChildrenIds])
  }
}

// 递归获取所有子节点ID
const getAllChildrenIds = (node: MenuTree): number[] => {
  if (!node.children || node.children.length === 0) return []

  const childrenIds = node.children.map(child => child.id)
  const grandchildrenIds = node.children.flatMap(child => getAllChildrenIds(child))

  return [...childrenIds, ...grandchildrenIds]
}

watch(() => props.formData, (val) => {
  form.value = { ...val }
  // 设置已选中的菜单
  checkedMenuIds.value = val.menuIds || []
  // 对话框弹出时清除表单验证错误提示，并设置菜单选中状态
  nextTick(() => {
    formRef.value?.clearValidate()
    // 设置树形菜单的选中状态
    if (menuTreeRef.value && checkedMenuIds.value.length > 0) {
      menuTreeRef.value.setCheckedKeys(checkedMenuIds.value)
    }
  })
}, { deep: true, immediate: true })

const handleCancel = () => {
  visible.value = false
  formRef.value?.resetFields()
  // 清空树选中状态
  nextTick(() => {
    menuTreeRef.value?.setCheckedKeys([])
  })
}

const handleSubmit = async () => {
  if (!formRef.value) return

  try {
    await formRef.value.validate()
    submitLoading.value = true

    // 获取选中的菜单ID
    form.value.menuIds = getCheckedMenuIds()

    const { code, msg } = props.isEdit
      ? await roleService.update(form.value)
      : await roleService.save(form.value)

    if (code === 200) {
      ElMessage.success(props.isEdit ? '角色更新成功' : '角色创建成功')
      visible.value = false
      emit('success')
      formRef.value?.resetFields()
    } else {
      ElMessage.error((props.isEdit ? '更新失败：' : '创建失败：') + msg)
    }
  } catch {
    ElMessage.error('表单验证失败')
  } finally {
    submitLoading.value = false
  }
}

// 组件挂载时加载菜单列表
onMounted(() => {
  getMenuList()
})
</script>
