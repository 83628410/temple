import { defineStore } from 'pinia'

export const useUserStore = defineStore('user', ()=>{
    const token = ref('')
    const nickname = ref('')
    
    const setToken = (value: string) => {
        token.value = value
        localStorage.setItem('token', value)
    }
    
    
    const removeToken = ()=>{
        token.value = ''
        nickname.value = ''
        localStorage.removeItem('token')
        localStorage.removeItem('nickname')
    }
    
    return {
        isLogin:()=>token.value !== '',
        token, 
        nickname, 
        setToken,
        removeToken
    }   
}, { persist: true })
