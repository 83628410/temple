<script setup lang="ts">
import LeftLayout from '@/views/layout/LeftLayout.vue'
import CommandPalette from '@/views/layout/components/CommandPalette/CommandPalette.vue'
import ChangeLang from '@/views/layout/components/ChangeLang/index.vue'
import { Fold, Expand } from '@element-plus/icons-vue'
import { useRoute } from 'vue-router'

const route = useRoute()
const isCollapse = ref(false)

const breadcrumbList = computed(() =>
  route.matched
    .filter(r => r.meta?.title)
    .map(r => ({ path: r.path, title: r.meta!.title }))
)
</script>

<template>
  <el-container class="h-screen">
    <LeftLayout :is-collapse="isCollapse" />
    <el-container>
      <el-header class="flex items-center justify-between border-b border-gray-200 bg-white">
        <div class="flex items-center gap-4">
          <el-icon class="cursor-pointer rounded p-1 hover:bg-gray-100 hover:text-primary" size="20" @click="isCollapse = !isCollapse">
            <Fold v-if="!isCollapse" />
            <Expand v-else />
          </el-icon>
          <el-breadcrumb separator="/">
            <el-breadcrumb-item
              v-for="(item, index) in breadcrumbList"
              :key="index"
              :to="index === breadcrumbList.length - 1 ? '' : { path: item.path }"
            >
              {{ item.title }}
            </el-breadcrumb-item>
          </el-breadcrumb>
        </div>
        <div class="flex items-center gap-4">
          <CommandPalette />
          <ChangeLang />
        </div>
      </el-header>
      <el-main class="bg-gray-50 p-4">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>