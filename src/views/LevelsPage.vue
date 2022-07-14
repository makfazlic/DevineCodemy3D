<template>
  <ArrowComponent
    :link="'/hub'"
    @click="alert('hi')"
  />
  <LevelsSplide />
  <SvgBackground :show-s-v-g-landscape="true" />
</template>

<script>
// @ is an alias to /src
import ArrowComponent from "@/components/BackArrow";
import LevelsSplide from "@/components/Levels/LevelsSplide.vue";
import SvgBackground from "@/components/SvgBackground";

export default {
  name: "LevelsPage",
  components: {
    SvgBackground,
    ArrowComponent,
    LevelsSplide,
  },
};
</script>