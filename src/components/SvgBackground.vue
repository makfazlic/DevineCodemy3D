<template>
  <div
    id="svgbackground"
    ref="svgbackground"
    :style="[`--leftG: ${leftG? leftG : 'var(--bg-contrast3)'}`,`--rightG: ${rightG? rightG : 'var(--bg-contrast2)'};`]"
    :show-solid-bg="`${showSolidColorBG}`"
  >
    <div
      v-show="showPurgatory"
      class="svgbackground-content"
    >
      <template v-if="showSVGLandscape">
        <img 
          id="landscape"
          alt="landscape_image"
          src="@/assets/backgrounds/Purgatory.png"
        >
        <img 
          v-for="i in density"
          :key="i"
          alt="landscape_image"
          class="bgSprite randPosition randSize"
          src="@/assets/backgrounds/bird1.png"
        >
        <img 
          v-for="i in density"
          :key="i"
          alt="landscape_image"
          class="bgSprite randPosition randSize"
          src="@/assets/backgrounds/bird2.png"
        >
        <img 
          v-for="i in density"
          :key="i"
          alt="landscape_image"
          class="bgSprite randPosition randSize"
          src="@/assets/backgrounds/bird3.png"
        >
        <img 
          v-for="i in density"
          :key="i"
          alt="landscape_image"
          class="bgSprite randPosition randSize"
          src="@/assets/backgrounds/bird4.png"
        >
        <img 
          v-for="i in density"
          :key="i"
          alt="landscape_image"
          class="bgSprite randPosition randSize"
          src="@/assets/backgrounds/bird5.png"
        >
        <img 
          v-for="i in density"
          :key="i"
          alt="landscape_image"
          class="bgSprite randPosition randSize"
          src="@/assets/backgrounds/bird6.png"
        >
      </template>
    </div>


    <div
      v-show="showParadise"
      class="svgbackground-content"
    >
      <template v-if="showSVGLandscape">
        <img 
          id="landscape"
          alt="landscape_image"
          src="@/assets/backgrounds/Paradise.png"
        >
        <img 
          v-for="i in density"
          :key="i"
          alt="landscape_image"
          class="bgSprite randPosition randSize"
          src="@/assets/backgrounds/p1.png"
        >
        <img
          v-for="i in density"
          :key="i"
          alt="landscape_image"
          class="bgSprite randPosition randSize"
          src="@/assets/backgrounds/p2.png"
        >
        <img
          v-for="i in density"
          :key="i"
          alt="landscape_image"
          class="bgSprite randPosition randSize"
          src="@/assets/backgrounds/p3.png"
        >
        <img 
          v-for="i in density"
          :key="i"
          alt="landscape_image"
          class="bgSprite randPosition randSize"
          src="@/assets/backgrounds/p4.png"
        >
      </template>
    </div>


    <div
      v-show="!showPurgatory && !showParadise"
      class="svgbackground-content"
    >
      <template v-if="showSVGLandscape">
        <img 
          id="landscape"
          alt="landscape_image"
          src="@/assets/backgrounds/Inferno.gif"
        >
        <img
          v-for="i in density"
          :key="i"
          alt="landscape_image"
          class="bgSprite randPosition randSize"
          src="@/assets/backgrounds/demon1.png"
        >
        <img
          v-for="i in density"
          :key="i"
          alt="landscape_image"
          class="bgSprite randPosition randSize"
          src="@/assets/backgrounds/demon2.png"
        >
        <img
          v-for="i in density"
          :key="i"
          alt="landscape_image"
          class="bgSprite randPosition randSize"
          src="@/assets/backgrounds/demon3.png"
        >
        <img
          v-for="i in density"
          :key="i"
          alt="landscape_image"
          class="bgSprite randPosition randSize"
          src="@/assets/backgrounds/demon4.png"
        >
        <img
          v-for="i in density"
          :key="i"
          alt="landscape_image"
          class="bgSprite randPosition randSize"
          src="@/assets/backgrounds/demon5.png"
        >
        <img 
          v-for="i in density"
          :key="i"
          alt="landscape_image"
          class="bgSprite randPosition randSize"
          src="@/assets/backgrounds/demon6.png"
        >
      </template>
    </div>
  </div>
</template>

<script>

let observer = null

export default {
  name: 'SvgBackground',
  props: {
    // Whether to show the SVG landscape or not
    showSVGLandscape: {
      type: Boolean,
      required: false,
      default: false
    },
    // Whether to show also the colored BG or not
    showSolidColorBG: {
      type: Boolean,
      required: false,
      default: true
    },

    // Overrides the left gradient from the color theme to a custom color
    leftG: {
      type: String,
      default: 'var(--bg-contrast2)',
      required: false
    },
    // Overrides the right gradient from the color theme to a custom color
    rightG: {
      type: String,
      default: 'var(--bg-contrast2)',
      required: false
    }
  },
  data() {
    return {
      showPurgatory: false,
      showParadise: false,

      widthRange: [1, 5],
      topRange:   [5, 70],
      leftRange:  [3,90],

      density:2
    }
  },
  beforeMount() {
    this.updateShownWorld()

    /*
    This event listener listens for class changes on the body.
    When the class on the body changes, recomputes which world to show */
    observer = new MutationObserver((mutationList) => {
      if (mutationList.some((mutation) => mutation.attributeName === "world")) {
        this.updateShownWorld()
      }
    })
    observer.observe(document.body, {attributes: true})
  },
  mounted() {
    // Randomizes the sizes of the sprites
    this.$refs.svgbackground.querySelectorAll(".randSize").forEach((el)=>{
      el.style.width = this.widthRange[0] + Math.random() * (this.widthRange[1] - this.widthRange[0]) + "vw"
    })
    // Randomizes the position of the sprites
    this.$refs.svgbackground.querySelectorAll(".randPosition").forEach((el)=>{
      el.style.left = this.leftRange[0] + Math.random() * (this.leftRange[1] - this.leftRange[0]) + "vw"
      el.style.top = this.topRange[0] + Math.random() * (this.topRange[1] - this.topRange[0]) + "vh"
    })

  },
  beforeUnmount() {
    observer.disconnect()
  },
  methods: {
    updateShownWorld() {
      this.showPurgatory = document.body.getAttribute('world') == 'Purgatory';
      this.showParadise = document.body.getAttribute('world') == 'Paradise'
      console.log("[+] Refreshed background")
    }
  }

}
</script>

<style scoped>
/* COLORS */
#svgbackground[show-solid-bg='true'] {
  background: linear-gradient(90deg, var(--leftG) 0%, var(--rightG) 100%);
}

/* ###### */

#svgbackground {
  width: 100vw;
  height: 100vh;
  position: fixed;
  top: 0;
  left: 0;
  z-index: -1;
  pointer-events: none;
}

#landscape {
  position: fixed;
  bottom: 0;
  left: 0;
  width: 100%;
}

.bgSprite {
  position: absolute;
}

</style>