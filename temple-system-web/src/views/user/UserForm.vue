<template>
  <el-drawer append-to-body v-model="visible" :title="isEdit ? '编辑用户' : '新增用户'" size="600px" direction="rtl">
    <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
      <el-form-item label="用户名" prop="username">
        <el-input v-model="form.username" placeholder="请输入用户名" :disabled="isEdit" />
      </el-form-item>
      <el-form-item label="密码" prop="password" v-if="!isEdit">
        <el-input v-model="form.password" placeholder="请输入密码" type="password" show-password />
      </el-form-item>
      <el-form-item label="昵称" prop="nickname">
        <el-input v-model="form.nickname" placeholder="请输入昵称" />
      </el-form-item>
      <el-form-item label="邮箱" prop="email">
        <el-input v-model="form.email" placeholder="请输入邮箱" />
      </el-form-item>
      <el-form-item label="手机号" prop="phone">
        <el-input v-model="form.phone" placeholder="请输入手机号" />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-space>
          <el-switch v-model="form.status" :active-value="1" :inactive-value="0" />
          <span class="text-gray-500 text-sm">{{ form.status === 1 ? '启用' : '禁用' }}</span>
        </el-space>
      </el-form-item>
      <el-form-item label="角色" prop="roleIds">
        <el-select v-model="form.roleIds" multiple placeholder="请选择角色" class="w-full">
          <el-option
            v-for="role in roleList"
            :key="role.id"
            :label="role.name"
            :value="role.id"
          />
        </el-select>
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="handleCancel">取消</el-button>
      <el-button type="primary" @click="handleSubmit">确定</el-button>
    </template>
  </el-drawer>
</template>

<script setup lang="ts">
import type { FormInstance, FormRules } from 'element-plus'
import { userService, UserSaveRequest } from '@/api/user'
import { roleService, Role } from '@/api/role'

const props = defineProps<{
  modelValue: boolean
  isEdit: boolean
  formData: UserSaveRequest
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
const form = ref<UserSaveRequest>({ ...props.formData, roleIds: props.formData.roleIds || [] })
const roleList = ref<Role[]>([])

// 获取角色列表
const getRoleList = async () => {
  try {
    const { code, data } = await roleService.getList()
    if (code === 200) {
      roleList.value = data || []
    }
  } catch {
    ElMessage.error('获取角色列表失败')
  }
}

// 组件挂载时获取角色列表
onMounted(getRoleList)

// 验证规则
const rules: FormRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '用户名长度在 3 到 20 个字符', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 20, message: '密码长度在 6 到 20 个字符', trigger: 'blur' }
  ],
  nickname: [
    { required: true, message: '请输入昵称', trigger: 'blur' }
  ],
  email: [
    { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
  ],
  phone: [
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }
  ]
}

watch(() => props.formData, (val) => {
  form.value = { ...val, roleIds: val.roleIds || [] }
  // 对话框弹出时清除表单验证错误提示
  nextTick(() => {
    formRef.value?.clearValidate()
  })
}, { deep: true, immediate: true })

const handleCancel = () => {
  visible.value = false
  formRef.value?.resetFields()
}

const handleSubmit = async () => {
  if (!formRef.value) return

  try {
    await formRef.value.validate()
    
    const { code, msg } = props.isEdit
      ? await userService.update(form.value)
      : await userService.save(form.value)
    
    if (code === 200) {
      ElMessage.success(props.isEdit ? '用户更新成功' : '用户新增成功')
      visible.value = false
      emit('success')
      formRef.value?.resetFields()
    } else {
      ElMessage.error('操作失败：' + msg)
    }
  } catch {
    // 验证失败
  }
}
</script>
