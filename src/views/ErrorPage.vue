<template>
  <ArrowComponent :link="'/hub'" />
  <div id="error-text">
    Error
  </div>
  <div
    v-if="!isNaN(status_code)"
    id="error-desc"
  >
    Unfortunately you have run into a {{ status_code }} error!
  </div>
  <div
    v-else
    id="error-desc"
  >
    Unfortunately you have run into an unknown error!
  </div>
  <SvgBackground />
</template>

<script>

import ArrowComponent from "@/components/BackArrow";
import SvgBackground from "@/components/SvgBackground";

export default {
  name: "ErrorPage",
  components: {
    ArrowComponent,
    SvgBackground
  },
  data() {
    return {
      status_code: this.$route.query.statuscode
    }
  }
}
</script>

<style scoped>

#error-text {
  color: white;
  font-size: 100px;
  display: flex;
  justify-content: center;

  margin: 0;
  position: absolute;
  top: 30%;
  left: 50%;
  -ms-transform: translate(-50%, -50%);
  transform: translate(-50%, -50%);
}

#error-desc {
  color: white;
  font-size: 30px;
  display: flex;
  justify-content: center;

  margin: 0;
  position: absolute;
  top: 40%;
  left: 50%;
  -ms-transform: translate(-50%, -50%);
  transform: translate(-50%, -50%);
}

</style>