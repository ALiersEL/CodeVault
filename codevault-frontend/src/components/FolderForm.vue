<script setup lang="ts">
import { NForm, NFormItem, NInput, NButton, NSpace, FormRules } from "naive-ui";
import { reactive, ref, defineEmits, VNodeRef, inject } from "vue";
import { postMapping } from "../api/request";
import router from "../router";

// 初始化，不为空
const formRef = ref<VNodeRef | null>(null);

type Folder = {
    name: string;
    parentFolderID: number;
};

const folder = reactive<Folder>({
    name: "",
    parentFolderID: 1,
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

const folderNames = inject("folderNames") as () => string[];
const getFolderContent = inject("getFolderContent") as () => void;

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

    console.log("提交");
    // 如果folder.name重复，提示错误
    const list = folderNames();
    if (list.includes(folder.name)) {
        console.log("文件夹名称重复");
        alert("文件夹名称重复");
        return;
    }


    const folderPath = router.currentRoute.value.query.path;
    // console.log("提交" + folderPath);
    postMapping("/folders", { parentPath: folderPath, folderName: folder.name }).then((res) => {
        if (res.status === 200) {
            console.log("success");
            // inject getFolderContent function from parent
            getFolderContent();
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
                <n-space>
                    <n-button round attr-type="button" @click="handleCancel">
                        取消
                    </n-button>
                    <n-button round attr-type="submit" @click="handleSubmit">
                        创建
                    </n-button>
                </n-space>
            </div>
        </n-form>
    </n-space>

</template>

<style scoped>
</style>