<script setup lang="ts">
import { defineEmits, computed } from "vue";
import { NModal, useMessage } from "naive-ui";

const message = useMessage();

const props = defineProps({
  showConfirmModal: {
    type: Boolean,
    required: true
  },
  promptMessage: {
    type: String,
    required: true
  }
});

const emit = defineEmits(["update:showConfirmModal", "confirmed", "canceled"]);


const showConfirmModal = computed({
  get: () => props.showConfirmModal,
  set: (value: boolean) => emit("update:showConfirmModal", value)
});

const submitCallback = () => {
  emit("confirmed");
  message.success("确认");
};

const cancelCallback = () => {
  emit("canceled");
  message.info("取消");
};

</script>

<template>
    <div>
        <n-modal
          v-model:show="showConfirmModal"
          preset="dialog"
          title="确认"
          :content="promptMessage"
          positive-text="确认"
          negative-text="取消"
          @positive-click="submitCallback"
          @negative-click="cancelCallback"
        />
    </div>
</template>

<style scoped>
</style>
