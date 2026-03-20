<template>
    <el-dropdown>
        <span class="el-dropdown-link">
            <text>{{currentLangLabel}}</text>
            <el-icon class="el-icon--right">
                <arrow-down />
            </el-icon>
        </span>
        <template #dropdown>
            <el-dropdown-menu>
                <el-dropdown-item v-for="item in langList" :key="item.value" :value="item.value"
                    @click="changeLang(item.value)">{{ item.label }}</el-dropdown-item>
            </el-dropdown-menu>
        </template>
    </el-dropdown>
</template>

<script lang="ts" setup>
import { ArrowDown } from '@element-plus/icons-vue'
import { setCurrentLang, langList, getCurrentLangLabel } from '@/lang/i18n'
const currentLangLabel = ref( getCurrentLangLabel() as string)

const changeLang = (lang: string) => {
    const loading = ElLoading.service({
        lock: true,
        text: '加载中请稍后',
        background: 'rgba(0, 0, 0, 0.7)',
    })
    setTimeout(() => {
        loading.close()
        setCurrentLang(lang)
    }, 1000)
}
</script>

<style scoped>
.example-showcase .el-dropdown-link {
    cursor: pointer;
    color: var(--el-color-primary);
    display: flex;
    align-items: center;
}
</style>