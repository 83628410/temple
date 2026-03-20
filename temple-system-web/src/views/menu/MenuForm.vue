<template>
  <el-drawer v-model="visible" :title="isEdit ? '编辑菜单' : '新增菜单'" size="600px" direction="rtl">
    <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
      <el-form-item label="菜单名称" prop="name">
        <el-input v-model="form.name" placeholder="请输入菜单名称" />
      </el-form-item>
      <el-form-item label="菜单类型" prop="type">
        <el-radio-group v-model="form.type">
          <el-radio label="C">目录</el-radio>
          <el-radio label="M">菜单</el-radio>
          <el-radio label="B">按钮</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="父菜单" prop="parentId">
        <el-tree-select v-model="selectedParentId" :data="treeData" :default-expanded-keys="[0]" :check-strictly="true"
          :props="{ label: 'label', children: 'children' }" node-key="id" placeholder="请选择父菜单" clearable
          class="w-full" @change="handleParentChange" />
      </el-form-item>
      <el-form-item label="权限标识" prop="permission">
        <el-input v-model="form.permission" placeholder="请输入权限标识（如：system:menu:view）" />
      </el-form-item>
      <el-form-item label="菜单路径" prop="path">
        <el-input v-model="form.path" placeholder="请输入菜单路径" />
      </el-form-item>
      <el-form-item label="组件路径" prop="component">
        <el-input v-model="form.component" placeholder="请输入组件路径" />
      </el-form-item>
      <el-form-item label="菜单图标" prop="icon">
        <IconSelect v-model="form.icon" />
      </el-form-item>
      <el-form-item label="排序" prop="orderNum">
        <el-input-number v-model="form.orderNum" :min="0" :step="1" />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-space>
          <el-switch v-model="form.status" :active-value="1" :inactive-value="0" />
          <span class="text-gray-500 text-sm">{{ form.status === 1 ? '显示（菜单可见）' : '隐藏（菜单不可见）' }}</span>
        </el-space>
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="handleCancel">取消</el-button>
      <el-button type="primary" @click="handleSubmit">确定</el-button>
    </template>
  </el-drawer>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue'
import type { FormInstance, FormRules } from 'element-plus'
import { menuService, MenuData, MenuSaveRequest } from '@/api/menu'
import IconSelect from './IconSelect.vue'

interface Tree {
  id: number
  label: string
  children?: Tree[]
  original?: MenuData
}

const props = defineProps<{
  modelValue: boolean
  isEdit: boolean
  formData: MenuSaveRequest
  treeData: Tree[]
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
const form = ref<MenuSaveRequest>({ ...props.formData })
const selectedParentId = ref<number | undefined>(undefined)

const rules: FormRules = {
  name: [{ required: true, message: '请输入菜单名称', trigger: 'blur' }],
  type: [{ required: true, message: '请选择菜单类型', trigger: 'change' }],
  path: [{ required: true, message: '请输入菜单路径', trigger: 'blur' }],
  component: [{ required: true, message: '请输入组件路径', trigger: 'blur' }]
}

watch(() => props.formData, (val) => {
  form.value = { ...val }
  selectedParentId.value = val.parentId || undefined
}, { deep: true, immediate: true })

const handleParentChange = (value: number | undefined) => {
  form.value.parentId = value || 0
}

const handleCancel = () => {
  visible.value = false
  formRef.value?.resetFields()
}

const handleSubmit = async () => {
  if (!formRef.value) return
  try {
    await formRef.value.validate()
    const { code, msg } = props.isEdit
      ? await menuService.update(form.value)
      : await menuService.save(form.value)
    if (code === 200) {
      ElMessage.success(props.isEdit ? '菜单更新成功' : '菜单新增成功')
      visible.value = false
      emit('success')
      formRef.value?.resetFields()
    } else {
      ElMessage.error('操作失败：' + msg)
    }
  } catch {
    ElMessage.error('表单验证失败')
  }
}
</script>
