// uno.config.ts
import {
  defineConfig
} from 'unocss'

export default defineConfig({
  shortcuts: {
    'flex-center': 'flex items-center justify-center',
  },
  rules: [
    ['flex-center', {
      display: 'flex',
      'align-items': 'center',
      'justify-content': 'center'
    }],
  ]
})