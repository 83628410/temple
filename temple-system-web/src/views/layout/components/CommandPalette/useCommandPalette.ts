import { LocationQueryRaw } from "vue-router";

const resultsTest =
    [

        {
            title: '仪表盘',
            path: '/dashboard',
            name: 'dashboard',
            icon: 'homepage'
        },
        {
            title: '用户管理',
            path: '/system/user',
            name: 'user',
            icon: 'user'
        },
        {
            title: '角色管理',
            path: '/system/role',
            name: 'role',
            icon: 'role'
        },
        {
            title: '菜单管理',
            path: '/system/menu',
            name: 'menu',
            icon: 'menu'
        },
        {
            title: '日志管理',
            path: '/system/log',
            name: 'log',
            icon: 'document'
        }

    ]


/** 搜索项类型 */
interface SearchItem {
    title: string;
    path: string;
    name?: string;
    icon?: string;
    redirect?: string;
    params?: LocationQueryRaw;
}
const HISTORY_KEY = 'commandPaletteHistory'
const HISTORY_MAX = 3
export function useCommandPalette() {
    const results = ref<SearchItem[]>(resultsTest);
    const visible = ref(false)
    const keyword = ref('')
    const activeIndex = ref(0)
    const history = ref<SearchItem[]>([]);
    const list = ref<SearchItem[]>([])
    const inputRef = ref<HTMLInputElement>()
    const resultEmpty = ref(false)

    function loadList() {

    }
    /** 加载历史记录 */
    function loadHistory() {
        try {
            history.value = JSON.parse(localStorage.getItem(HISTORY_KEY) || '[]')
        } catch (error) {
            history.value = []
        }
    }
    /** 点击跳转*/
    function onGo(item: SearchItem) {
        addHistory(item)
        close()
    }
    /** 搜索 */
    function onSearch() {
        if (keyword.value.length === 0) {
            list.value = history.value
            resultEmpty.value = true
        } else {
            list.value = results.value.filter(i => i.title.includes(keyword.value))
            resultEmpty.value = false
        }
    }
    /** 保存历史记录 */
    function saveHistory() {
        try {
            localStorage.setItem(HISTORY_KEY, JSON.stringify(history.value))
        } catch (error) {
            console.error('保存命令面板历史记录失败', error)
        }
    }
    /** 添加历史记录 */
    function addHistory(item: SearchItem) {
        // 限制历史记录数量
        if (history.value.length >= HISTORY_MAX) {
            history.value.pop()
        }
        // 去掉重复项
        const index = history.value.findIndex(i => i.path === item.path)
        if (index !== -1) {
            history.value.splice(index, 1)
        }
        history.value.unshift(item)
        saveHistory()
    }
    /** 删除历史记录 */
    function removeHistory(item: SearchItem) {
        const index = history.value.findIndex(i => i.path === item.path)
        if (index !== -1) {
            history.value.splice(index, 1)
        }
        saveHistory()
    }
    /** 清空历史记录 */
    function clearHistory() {
        history.value = []
        saveHistory()
    }

    /** 打开命令面板 */
    function open() {
        keyword.value = ''
        visible.value = true
        list.value = history.value
        resultEmpty.value = true
        setTimeout(() => {
            inputRef.value?.focus()
        }, 100)
    }
    /** 关闭命令面板 */
    function close() {
        visible.value = false
    }
    /** 移动选中项 */
    function moveActive(direction: "up" | "down") {
        if (list.value.length === 0) {
            return
        }
        if (direction === "up") {
            activeIndex.value = activeIndex.value <= 0 ? list.value.length - 1 : activeIndex.value - 1
        } else {
            activeIndex.value = activeIndex.value >= list.value.length - 1 ? 0 : activeIndex.value + 1
        }
    }
    /** 处理 Ctrl+K 打开/关闭 命令面板 */
    function handleCtrlKKeydown(e: KeyboardEvent) {
        if ((e.ctrlKey || e.metaKey) && e.key.toLowerCase() === 'k') {
            // 阻止默认事件
            e.preventDefault()
            if (!visible.value) {
                open()
            } else {
                close()
            }
        }
    }
    /** 处理输入框输入事件 */
    function handleInputKeydown(e: KeyboardEvent | Event) {
        // 仅处理键盘事件
        if (!(e instanceof KeyboardEvent)) return;
        const key = e.key.toLowerCase()
        if (key == "arrowup") {
            e.preventDefault()
            // 上箭头键 上移选中项
            moveActive("up")
            return
        }
        if (key == "arrowdown") {
            e.preventDefault()
            // 下箭头键 下移选中项
            moveActive("down")
            return
        }

        if (e.key.toLowerCase() === 'escape') {
            e.preventDefault()
            close()
        }
        if (e.key.toLowerCase() === 'enter') {
            e.preventDefault()
            if (activeIndex.value !== -1) {
                onGo(list.value[activeIndex.value])
            }
        }
    }
    /** 初始化 */
    onMounted(() => {
        loadHistory()
        loadList()
        // 监听 Ctrl+K 打开命令面板
        document.addEventListener('keydown', handleCtrlKKeydown)
    })
    /** 组件卸载时移除事件监听 */
    onBeforeUnmount(() => {
        document.removeEventListener('keydown', handleCtrlKKeydown)
    })
    return {
        list,
        visible,
        keyword,
        // 结果为空
        resultEmpty,
        // 选中项索引
        activeIndex,
        open,
        close,
        /** 处理输入框输入事件 */
        handleInputKeydown,
        /** 移动选中项 */
        moveActive,
        /** 处理 Go 事件 */
        onGo,
        /** 搜索 */
        onSearch,
        /** 清空历史记录 */
        clearHistory,
        /** 删除历史记录 */
        removeHistory,
        /** 输入框引用 */
        inputRef
    };
}
