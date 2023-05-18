<script setup lang="ts">
import { ref } from "vue";

const isPopupVisible = ref(false);

const showPopup = () => {
    isPopupVisible.value = true;
}

const hidePopup = () => {
    isPopupVisible.value = false;
}
</script>

<template>
    <!-- dialog form, dialog appear from the top, a dialog with scrollable content -->
   <div>
        <button @click="showPopup">打开弹出窗口</button>
        <div v-if="isPopupVisible" class="popup">
            <!-- a form for problem-->
            <form>
                <!-- a input for problem title -->
                <input type="text" placeholder="题目标题" />
                <!-- a input for problem description -->
                <input type="text" placeholder="题目描述" />
                <!-- a input for problem input -->
                <input type="text" placeholder="输入" />
                <!-- a input for problem output -->
                <input type="text" placeholder="输出" />
                <!-- a input for problem sample input -->
                <input type="text" placeholder="样例输入" />
                <!-- a input for problem sample output -->
                <input type="text" placeholder="样例输出" />
                <!-- a input for problem hint -->
                <input type="text" placeholder="提示" />
                <!-- a input for problem source -->
                <input type="text" placeholder="来源" />
                <!-- a input for problem time limit -->
                <input type="text" placeholder="时间限制" />
                <!-- a input for problem memory limit -->
                <input type="text" placeholder="内存限制" />
                <!-- a input for problem difficulty -->
                <input type="text" placeholder="难度" />
                <!-- a input for problem tag -->
                <input type="text" placeholder="标签" />
            </form>
            <!-- a button to submit the form -->
            <button @click="hidePopup">提交</button>
            <!-- a button to close the form -->
            <button @click="hidePopup">关闭</button>
        </div>
   </div>
</template>

<style scoped>
.popup {
    position: fixed;
    top: 25%;
    left: 25%;
    width: 50%;
    height: 50%;
    /* 背景色 */
    background-color: rgba(0, 0, 0, 0.5);
    /* 内容居中 */
    display: flex;
    justify-content: center;
    align-items: center;
    /* 内容垂直居中 */
    flex-direction: column;
    background-origin: padding-box;
    border: 2 solid #fff;
    box-shadow: 0 0 10px #fff;
}
</style>