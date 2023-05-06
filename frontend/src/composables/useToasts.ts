import { useNuxtApp } from "#app";
import { ToastOptions } from "vue3-toastify";

export const useToasts = (msg: string, options?: ToastOptions) => {
  if (msg === "" || msg === undefined) return;

  console.log(msg, options);

  const nuxtApp = useNuxtApp();

  if (nuxtApp.$toast) {
    nuxtApp.$toast(msg, options);
  }
};
