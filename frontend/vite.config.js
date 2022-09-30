import { defineConfig } from 'vite'

export default defineConfig({
    server: {
        proxy: {
            '/subscribe': {
                target: 'ws://localhost:8080',
                ws: true
            }
        }
    }
})