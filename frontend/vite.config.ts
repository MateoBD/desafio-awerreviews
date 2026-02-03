import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// https://vite.dev/config/
export default defineConfig({
  base: '/desafio-awer/',
  plugins: [react()],
  server: {
    proxy: {
      '/desafio-awer/api': {
        target: 'http://localhost:8080',
        changeOrigin: true,
      }
    }
  }
})
