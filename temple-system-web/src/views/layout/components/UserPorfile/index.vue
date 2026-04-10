<script lang="ts" setup>
import {useUserStore} from '@/store/user'
import {useRouter} from 'vue-router'
const router = useRouter()
const userStore = useUserStore()

const logout = () => {
  ElMessageBox.confirm('是否确认登出？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    userStore.logout().then(() => {
      router.push('/login')
    })
  })
}

</script>
<template>
  <div class="user-profile">
    <el-dropdown trigger="click">
      <span class="flex items-center justify-center gap-1 cursor-pointer">
        <img class="h-7 w-7 rounded-full" src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png" alt="Avatar">
        <span>Nickname</span>
      </span>
      <template #dropdown>
        <el-dropdown-menu divided>
          <el-dropdown-item @click="logout">退出登录</el-dropdown-item>
        </el-dropdown-menu>
      </template>
    </el-dropdown>
  </div>
</template>
<style lang="scss" scoped>
 
</style>
