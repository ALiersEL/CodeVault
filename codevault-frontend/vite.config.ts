import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import { Configuration } from 'webpack'
import MonacoWebpackPlugin from 'monaco-editor-webpack-plugin';
import { prismjsPlugin } from 'vite-plugin-prismjs';

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [
    vue(),
    prismjsPlugin({
      languages: 'all', // 语言
	    plugins: ['line-numbers','show-language','copy-to-clipboard','inline-color'],
	    theme: 'okaidia',// 主题
      css: true
    }),
  ],
  server: {
    port: 5172,
    proxy: { // 本地开发环境通过代理实现跨域，生产环境使用 nginx 转发
      // 正则表达式写法
      '^/api': {
        target: 'http://localhost:8765', // 后端服务实际地址
        ws: true,
        changeOrigin: true, //开启代理
        rewrite: (path) => path.replace(/^\/api/, '')
      }
    }
  },
});

const config: Configuration = {
  plugins: [
    new MonacoWebpackPlugin({
      features: ['!gotoSymbol'],
    })
  ]
}