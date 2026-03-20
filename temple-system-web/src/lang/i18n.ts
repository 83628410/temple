import { App } from 'vue'
import { createI18n } from 'vue-i18n'
import zh from './zh.json'
import en from './en.json'

export const langList = [
    {
        value: 'zh-cn',
        label: '中文'
    },
    {
        value: 'en',
        label: '英文'
    },
    {
        value: 'de',
        label: '德语'
    },
    {
        value: 'fr',
        label: '法语'
    },
    {
        value: 'it',
        label: '意大利语'
    },
]

export const getCurrentLang = (): string => {
    const lang = localStorage.getItem('lang')
    return lang || 'zh-cn'
}
export const getCurrentLangLabel = () => {
    const lang = getCurrentLang()
    const item = langList.find((item) => item.value === lang)
    return item?.label || lang
}
const i18n = createI18n({
    legacy: false,
    locale: getCurrentLang(),
    messages: {
        zh: { ...zh },
        en: { ...en }
    }
})

export const setCurrentLang = (lang: string) => {
    localStorage.setItem('lang', lang)
    i18n.global.locale.value = lang as 'zh' | 'en'
    window.location.reload()
}
export function setupI18n(app: App) {
    app.use(i18n)
}