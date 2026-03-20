<script setup lang="ts">
import usePermissionStore from "@/store/permission";
import { computed, ref } from "vue";
import { useRoute } from "vue-router";
import { Menu } from '@element-plus/icons-vue'

const props = defineProps<{
  isCollapse: boolean
}>()

const permissionStore = usePermissionStore()
const route = useRoute()

const routes = computed(() => permissionStore.routes)
const currentRoutePath = ref(route.path)
</script>

<template>
  <el-aside class="layout-aside" :width="props.isCollapse ? '64px' : '210px'">
    <div class="layout-logo">
      <span v-if="!props.isCollapse">Temple Admin</span>
      <el-icon v-else size="20"><Menu /></el-icon>
    </div>
    <el-scrollbar class="layout-menu-scrollbar">
      <el-menu
        class="layout-menu"
        :default-active="currentRoutePath"
        :router="true"
        :collapse="props.isCollapse"
        :collapse-transition="false"
        background-color="#ffffff"
        text-color="rgb(78, 89, 105)"
        active-text-color="#000000"
      >
        <template v-for="item in routes" :key="item.path">
          <el-sub-menu :index="item.path" v-if="item.meta && !item.meta?.hidden">
            <template #title>
              <el-icon size="18"><component :is="item.meta?.icon"/></el-icon>
              <span>{{ item.meta?.title }}</span>
            </template>
            <template v-for="child in item.children" :key="child.path">
              <el-menu-item :index="child.path" v-if="child.meta && !child.meta?.hidden && !child.children">
                {{ child.meta?.title }}
              </el-menu-item>
              <el-sub-menu :index="child.path" v-else-if="child.meta && !child.meta?.hidden">
                <template #title>{{ child.meta?.title }}</template>
                <template v-for="subChild in child.children" :key="subChild.path">
                  <el-menu-item :index="subChild.path" v-if="subChild.meta && !subChild.meta?.hidden">
                    {{ subChild.meta?.title }}
                  </el-menu-item>
                </template>
              </el-sub-menu>
            </template>
          </el-sub-menu>
        </template>
      </el-menu>
    </el-scrollbar>
  </el-aside>
</template>

<style lang="scss" scoped>
.layout-aside {
  display: flex;
  flex-direction: column;
  background-color: #ffffff;
  border-right: 1px solid #e4e7ed;
  transition: width 0.28s;
}

.layout-logo {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 60px;
  color: rgb(78, 89, 105);
  font-size: 18px;
  font-weight: bold;
  border-bottom: 1px solid #e4e7ed;
}

.layout-menu-scrollbar {
  flex: 1;
  overflow: hidden;
}

.layout-menu {
  border-right: none;
}

:deep(.el-menu-item:hover),
:deep(.el-sub-menu__title:hover) {
  background-color: #f5f7fa !important;
}

:deep(.el-menu-item.is-active) {
  background-color: #f5f7fa !important;
  font-weight: 600;
}
</style>
