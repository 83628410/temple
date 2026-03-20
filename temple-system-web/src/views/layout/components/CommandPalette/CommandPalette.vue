<script lang="ts" setup>
import { Search } from '@element-plus/icons-vue'
import { useCommandPalette } from './useCommandPalette'

const {
    visible,
    keyword,
    list,
    open,
    close,
    handleInputKeydown,
    removeHistory,
    onSearch,
    onGo,
    activeIndex,
    resultEmpty,
    inputRef
} = useCommandPalette()
</script>
<template>
    <div>
        <div class="command-palette" role="button" tabindex="0" aria-label="打开搜索面板" @click="open"
            @keydown.enter.prevent="open" @keydown.space.prevent="open">
            <div class="command-palette-left">
                <i-ep-Search style="width: 18px; height: 18px; margin-right: 8px" />
                <span class="command-palette-text">搜索菜单</span>
            </div>
            <kbd class="command-palette-kbd">Ctrl K</kbd>
        </div>
        <el-dialog v-model="visible" width="500px" :close-on-click-modal="true" :show-close="false"
            :append-to-body="true" style="background-color:var(--el-bg-color-page);padding: 0;" @close="close">
            <div class="dialog">
                <el-input class="p-3 h-18" ref="inputRef" v-model="keyword" clearable placeholder="搜索菜单" :prefix-icon="Search"
                    @keydown="handleInputKeydown" @input="onSearch">
                </el-input>
                <div class="dialog-results p-3 p-t-0">
                    <div v-if="list.length === 0" class="flex-center">
                        暂无搜索结果
                    </div>
                    <div v-if="resultEmpty && list.length > 0" class="flex items-center justify-left">
                        搜索历史
                    </div>
                    <ul class="dialog-results-list">
                        <li class="dialog-results-item flex items-center justify-between" v-for="(item, index) in list"
                            :key="item.path" @click="onGo(item)" :class="{ 'active': index === activeIndex }">
                            <div class="flex-center title">
                                <i-ep-Clock v-if="resultEmpty" style="width: 18px; height: 18px; margin-right: 8px" />
                                {{ item.title }}
                            </div>
                            <div class="flex-center menu cursor-pointer" v-if="resultEmpty"
                                @click.stop="removeHistory(item)">
                                <i-ep-Delete style="width: 18px; height: 18px;" />
                            </div>

                        </li>
                    </ul>
                </div>
                <div class="dialog-results-footer p-4">
                    <ul class="dialog-results-command">
                        <li class="command-key">
                            <i-ep-ArrowDown style="width: 18px; height: 18px; margin-right: 8px" />
                            <span class="command-text">选择</span>
                        </li>
                        <li class="command-key">
                            <i-ep-ArrowUpBold style="width: 18px; height: 18px; margin-right: 8px" />
                            <i-ep-ArrowDownBold style="width: 18px; height: 18px; margin-right: 8px" />
                            <span class="command-text">切换</span>
                        </li>
                        <li class="command-key">
                            <i-ep-BottomLeft style="width: 18px; height: 18px; margin-right: 8px" />
                            <span class="command-text">关闭</span>
                        </li>
                    </ul>

                </div>
            </div>
        </el-dialog>
    </div>
</template>
<style lang="scss" scoped>
.command-palette {
    display: flex;
    gap: 10px;
    align-items: center;
    justify-content: space-between;
    height: 32px;
    padding: 0 12px;
    -webkit-user-select: none;
    user-select: none;
    background: var(--el-fill-color-light);
    border: 2px solid var(--el-border-color-lighter);
    border-radius: 999px;

    &:hover {
        border: 2px solid var(--el-color-primary-light-5);
        background-color: var(--el-color-info-light-7);
    }

    &-left {
        display: flex;
        align-items: center;
        justify-content: center;
    }

    &-text,
    &-kbd {
        font-size: 12px;
        color: var(--el-text-color-regular);
    }

    &-kbd {
        background-color: var(--el-color-white);
        border-radius: 999px;
        padding: 0 8px;
    }

}


.dialog-wrap {
    background-color: #000;
}

.dialog {
    &-results {
        max-height: 600px;
        overflow-y: auto;
    }

    &-results-list {
        padding: 0;
        margin: 0;
        list-style: none;
    }

    &-results-item {
        margin: 1rem 0;
        padding: 1rem;
        cursor: pointer;
        background-color: var(--el-color-white);
        border-radius: 5px;

        .title {
            font-size: 14px;
            color: var(--el-text-color-regular);
        }

        .menu {
            width: 30px;
            height: 30px;
            border-radius: 999px;
        }

        .menu:hover {
            background-color: var(--el-color-primary-light-7);
        }
    }

    &-results-item:hover {
        background-color: var(--el-color-primary-light-9);
    }

    &-results-item.active {
        background-color: var(--el-color-primary-light-5);
    }

    &-results-command {
        display: flex;
        gap: 20px;
        align-items: center;
        justify-content: left;
        list-style: none;
        padding: 0;
        margin: 0;
    }

}

.dialog-results-footer {
    background-color: var(--el-color-white);
    border-top: 1px solid var(--el-border-color-lighter);
    box-shadow: 0 -2px 4px rgba(0, 0, 0, 0.1);
}

.command-key {
    display: flex;
    align-items: center;
    justify-content: left;
}
</style>
