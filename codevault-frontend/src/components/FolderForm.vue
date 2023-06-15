<script setup lang="ts">
import { NForm, NFormItem, NInput, NButton, NSpace, FormRules } from "naive-ui";
import { reactive, ref, defineEmits, VNodeRef } from "vue";

// 初始化，不为空
const formRef = ref<VNodeRef | null>(null);

type Folder = {
    name: string;
    parentFolderID: number;
};

const folder = reactive<Folder>({
    name: "",
    parentFolderID: 0,
});

const rules: FormRules = {
    name: [
        { required: true, message: "请输入文件夹名称" },
        { min: 1, max: 20, message: "长度在 1 到 20 个字符", trigger: "blur" },
    ],
};

const emit = defineEmits(["cancel"]);
const handleCancel = () => {
    emit("cancel");
};

const handleSubmit = (e: MouseEvent) => {
    e.preventDefault();
    console.log(formRef.value)
    // 验证
    formRef.value?.validate((errors: any) => {
        console.log("success")
        if (!errors) {
            console.log(folder);
            emit("cancel");
        }
        else {
            console.log("error");
        }
    });
}
</script>

<template>
    <n-space>
        <n-form
        ref="formRef"
        :model="folder"
        :rules="rules"
        label-placement="top"
        :label-width="160"
        :style="{
            maxWidth: '500px',
        }"
        >
            <n-form-item label="名称" path="name">
                <n-input v-model:value="folder.name" placeholder="文件夹名称" autofocus style="width: 500px;" @keydown.enter.prevent />
            </n-form-item>
            <div style="display: flex; justify-content: flex-end">
                <n-button round attr-type="button" @click="handleCancel">
                    取消
                </n-button>
                <n-button round attr-type="submit" @click="handleSubmit">
                    创建
                </n-button>
            </div>
        </n-form>
    </n-space>

</template>

<style scoped>
</style>