<template>
  <router-link :to="link">
    <backArrow id="back_arrow" />
  </router-link>
</template>

<script>
import backArrow from '@/assets/backArrowText.svg';

export default {
  name: "ArrowComponent",
  components: {
    backArrow: backArrow,
  },
  props: {
    link: {
      type: String,
      default: "/",
    },
  }
}
</script>

<style scoped>

#back_arrow {
  position: absolute;
  left: 2vw;
  top: 3vh;
  width: 8vh;
  z-index: 9999999; /* Stay on top */
  transition: all var(--transition-quick);
}

#back_arrow:hover {
  cursor: pointer;
  transform: scale(var(--scale-hover-small));
}

#back_arrow:active {
  filter: brightness(0.8);
}

</style>