import { defineStore } from 'pinia'
import * as AuthApi from '@/api/auth'

export const useUserStore = defineStore('user', () => {
    const token = ref('')
    const nickname = ref('')

    function setToken(value: string) {
        token.value = value
        localStorage.setItem('token', value)
    }


    /** 移除令牌 */
    function removeToken() {
        token.value = ''
        nickname.value = ''
        localStorage.removeItem('token')
        localStorage.removeItem('nickname')
    }
    /** 登出 */
    async function logout(): Promise<any> {
        await AuthApi.logout()
        removeToken()
    }

    return {
        isLogin: () => token.value !== '',
        token,
        nickname,
        setToken,
        removeToken,
        logout,
    }
}, { persist: true })
