<script setup lang="ts">
import { ref } from 'vue'
import variables from '@/styles/variables.module.scss'
import { useCounterStore } from '@/store/counter'
const counterStore = useCounterStore()



console.log(variables)
defineProps<{ msg: string }>()

const count = ref(0)
</script>

<template>
  <h1 :style="{ backgroundColor: variables.bgColor }">{{ msg }}</h1>
  <div>
    <el-button type="success"><i-ep-SuccessFilled />Success</el-button>
    <el-button type="info"><i-ep-InfoFilled />Info</el-button>
    <el-button type="warning"><i-ep-WarningFilled />Warning</el-button>
    <el-button type="danger"><i-ep-WarnTriangleFilled />Danger</el-button>
    <el-button type="primary"><svg-icon iconClass="block" />svg 本地图标</el-button>
  </div>

  <div class="card">
    <button type="button" @click="count++">count is {{ count }} {{ counterStore.count }} </button>
    <p>
      Edit
      <code>components/HelloWorld.vue</code> to test HMR
    </p>
  </div>

  <p>
    Check out
    <a href="https://vuejs.org/guide/quick-start.html#local" target="_blank"
      >create-vue</a
    >, the official Vue + Vite starter
  </p>
  <p>
    Learn more about IDE Support for Vue in the
    <a
      href="https://vuejs.org/guide/scaling-up/tooling.html#ide-support"
      target="_blank"
      >Vue Docs Scaling up Guide</a
    >.
  </p>
  <p class="read-the-docs">Click on the Vite and Vue logos to learn more</p>
  <div class=" text-blue">vue + vite </div>

</template>

<style scoped lang="scss">
.read-the-docs {
  color: #888;
}
.box {
  width: 100px;
  height: 100px;
  background-color: $bg-color;
}
</style>
