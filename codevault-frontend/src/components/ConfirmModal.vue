<script setup lang="ts">
import { defineEmits, computed } from "vue";
import { NModal, useMessage } from "naive-ui";

const message = useMessage();

const props = defineProps({
  showConfirmModal: {
    type: Boolean,
    required: true
  }
});

const emit = defineEmits(["update:showConfirmModal"]);

const showConfirmModal = computed({
  get: () => props.showConfirmModal,
  set: (value: boolean) => emit("update:showConfirmModal", value)
});

const submitCallback = () => {
  message.success("确认");
};

const cancelCallback = () => {
  message.info("取消");
};

</script>

<template>
    <div>
        <n-modal
          v-model:show="showConfirmModal"
          preset="dialog"
          title="确认"
          content="你确认?"
          positive-text="确认"
          negative-text="取消"
          @positive-click="submitCallback"
          @negative-click="cancelCallback"
        />
    </div>
</template>

<style scoped>
</style>
