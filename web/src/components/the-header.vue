<template>
  <a-layout-header class="header">
    <div class="logo">
      <router-link to="/welcome" style="color: white; font-size: 18px">
        甲蛙12306
      </router-link>
    </div>
    <div style="float: right; color: white;">
      您好：{{member.mobile}} &nbsp;&nbsp;
      <router-link to="/login" style="color: white;">
        退出登录
      </router-link>
    </div>

  </a-layout-header>
</template>

<script>
import {defineComponent, ref} from 'vue';
import store from "@/store";



export default defineComponent({
  name: "the-header-view",
  setup() {
      let member = store.state.member;



    return {
      selectedKeys1: ref(['2']),
      member
    };
  },
});
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>

</style>
