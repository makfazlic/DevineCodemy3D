<template>
  <vue-basic-alert
    ref="alert"
    :duration="200"
    :close-in="2000"
  />
  <div class="centered">
    <h1
      v-if="worlds.length > 0"
      id="world"
    >
      {{ worlds[currentWorld].name }}
    </h1>
    <h1
      v-else
      id="world"
    >
      Earth
    </h1>
  </div>


  <div id="worldDesc">
    <TextPopup
      v-if="worlds.length > 0"
      :title="worlds[currentWorld].name"
      :description="worlds[currentWorld].descriptionMessage"
      :callback="setCookie"
    />
  </div>

  <div class="content">
    <button
      class="arrow"
      :class="[isPrevActive ? 'arrowActive' : 'arrowNotActive']"
      @click="prev_page()"
    >
      <div>
        <scrollLeftArrow />
      </div>
    </button>

    <Splide
      v-if="playable_levels"
      ref="splide"
      class="splide"
      :options="{
        rewind: false,
        perPage: 5,
        wheel: true,
        wheelSleep: 200,
        arrows: false,
      }"
      aria-label="levels"
      @splide:move="onArrows"
      @splide:pagination:mounted="setSplideStart"
    >
      <SplideSlide
        v-for="level in playable_levels"
        :key="level.name"
        class="slide"
      >
        <router-link
          :to="'../views/Playground?levelNumber=' + level.levelNumber"
        >
          <div class="centered">
            <span class="column shadow">
              <button
                type="button"
                class="Level playable_level"
              >
                <div class="level_number">{{ level.levelNumber }}</div>
                <div class="level_name">{{ level.name }}</div>
                <img
                  class="background_image"
                  :src="level.thumbnailSrc"
                  alt="background_img"
                >
              </button>
            </span>
          </div>
        </router-link>
      </SplideSlide>

      <SplideSlide
        v-for="level in not_playable_levels"
        :key="level.name"
        class="slide"
        @click="displayAlert('warning', 'Locked Level', 'Complete previous levels to unlock this one!')"
      >
        <div class="centered">
          <span class="column shadow">
            <button
              type="button"
              class="Level not_playable_level"
            >
              <div class="level_number">{{ level.levelNumber }}</div>
              <img
                class="background_image"
                :src="level.thumbnailSrc"
                alt="background_img"
              >
              <div class="background">
                <locked />
              </div>
            </button>
          </span>
        </div>
      </SplideSlide>
    </Splide>

    <button
      class="arrow"
      :class="[isNextActive ? 'arrowActive' : 'arrowNotActive']"
      @click="next_page()"
    >
      <scrollRightArrow />
    </button>
  </div>
</template>


<script>
import "@splidejs/vue-splide/css/skyblue";

import TextPopup from "@/components/TextPopup";
import VueBasicAlert from 'vue-basic-alert';
import scrollLeftArrow from "@/assets/LevelsPage/backArrow.svg";
import scrollRightArrow from "@/assets/LevelsPage/frontArrow.svg";
import locked from "@/assets/LevelsPage/level_icon-locked.svg";

import {
  updateArrows,
  next_page,
  prev_page,
  setSplideStart,
  setCookie,
} from "@/js/LevelsPage/levels.js";

export default {
  name: "LevelsSplide",
  components: {
    TextPopup,
    VueBasicAlert,
    scrollLeftArrow,
    scrollRightArrow,
    locked,
  },
  props: {
    msg: {
      type: String,
      default: "Something went wrong!! Please contact the team for help. (Team page)",
    },
  },
  data() {
    return {
      isNextActive: true,
      isPrevActive: false,
      worlds: [],
      playable_levels: [],
      not_playable_levels: [],
      currentWorld: 0,
    };
  },
  async mounted() {
    await fetch(`${process.env.VUE_APP_CODELAND_BACKEND_HOST}/levels/worlds`,
        {
          method: "GET",
          credentials: "include"
        })
        .then((response) => {
          return response.json();
        })
        .then((worlds) => {
          this.worlds = worlds;
        });
    await fetch(`${process.env.VUE_APP_CODELAND_BACKEND_HOST}/levels?onlyinfo=true`,
        {
          method: "GET",
          credentials: "include"
        })
        .then((response) => {
          return response.json();
        })
        .then((levels) => {
          this.playable_levels = levels.first;
          this.not_playable_levels = levels.second;
          document.body.setAttribute("world", this.worlds[this.currentWorld].name)

        });


    // if cookie is not set, set it based on the played levels
    if (!this.$cookies.isKey('devine-codemy_worlds-played')) {
      let level_played = (this.playable_levels.length - 1) / 5; // 5 levels per world
      let world_played = 0;
      switch (level_played) {
        case 0:                       // the player has not played any level
          break;
        case 0 > level_played > 1:    // the player is playing the first world
          world_played = 1;
          break;
        case 1 > level_played > 2:    // the player is playing the second world
          world_played = 2;
          break;
        case 2 > level_played > 3:    // the player is playing the third world
          world_played = 3;
          break;
        default:
          break;
      }
      this.$cookies.set('devine-codemy_worlds-played', world_played, -1);
    }
  },
  methods: {
    updateArrows,
    next_page,
    prev_page,
    setSplideStart,
    onArrows(newIndex, prevIndex) {
      if (prevIndex + 2 < this.worlds[0].lastLevelNumber) {
        this.currentWorld = 0;
      } else if (prevIndex + 2 < this.worlds[1].lastLevelNumber) {
        this.currentWorld = 1;
      } else {
        this.currentWorld = 2;
      }
      this.$store.dispatch('setWorldTheme', this.worlds[this.currentWorld].name)
      this.updateArrows();
    },
    setCookie,
    displayAlert(type, title, message) {
      this.$refs.alert
          .showAlert(
              type, // There are 4 types of alert: success, info, warning, error
              message, // Message of the alert
              title, // Header of the alert
          )
    },

  },
};
</script>


<style scoped>

.centered {
  display: flex;
  justify-content: center;
  align-items: center;
}

.column {
  -webkit-border-radius: 10%;
  -moz-border-radius: 10%;
  border-radius: 10%;
}

.shadow {
  box-shadow: 5px 0px 20px 1px rgba(0, 0, 0, 0.8)
}

/* COLORS */
.level_number, .level_name {
  color: #191b19;
}

#world {
  margin-top: 0;
  width: 80%;
}

h1 {
  color: var(--accent1);
    font-size: min(10vw, 150px);
    margin-top: 3vh;
    position: relative;

    text-align: center;
    animation-name: wiggle;
    animation-duration: 10000ms;
    animation-delay: 2s;
    animation-iteration-count: infinite;
}


/* ###### */

#worldDesc {
  width: 100%;
  height: 7vh;
  display: flex;
  justify-content: center;
  align-items: center;
}

.Level {
  /*border: 0.5vw solid white;*/


  border: none;

  -webkit-border-radius: 10%;
  -moz-border-radius: 10%;
  border-radius: 10%;

  overflow: hidden;
  background-size: 100% 100%;
  width: 12vw;
  height: 12vw;
  transition: var(--transition-quick);
  position: relative;
}

.Level:hover {
  transform: scale(var(--scale-hover-big));
  transition: var(--transition-quick);
  cursor: pointer;
  z-index: 100;
    outline: 5px solid #191b19;
}


.level_number {
  position: absolute;
  width: 100%;
  height: 100%;
  top: 5%;
  left: 0;
  text-align: center;
  font-family: Nove, sans-serif;
  z-index: 2;
}

.Level .background {
  position: absolute;
  width: 100%;
  height: 100%;
  top: 0%;
  left: 0;
  z-index: 1;
}

.Level .background_image {
  position: absolute;
  width: 120%;
  height: 120%;
  top: -10%;
  left: -10%;
  /*top:0%;*/
  /*left:0;*/
  z-index: 0;
}

.playable_level .level_number {
  font-size: 8vw;
  color: white;
  text-shadow: black 4px 4px 10px;
}

.not_playable_level .level_number {
  font-size: 5vw;
  margin-top: 4vw;
  color: black;
  text-shadow: black 1px 1px 2px;
}

.playable_level .level_name {
  font-family: Nove, sans-serif;
  font-size: 1vw;
  position: absolute;
  width: 100%;
  height: 100%;
  top: 85%;
  left: 0;
  z-index: 2;
  color: white;
  text-shadow: black 4px 4px 10px;
}

.not_playable_level .level_name {
  display: none;
}

.not_playable_level .background_image {
  filter: blur(3px) brightness(0.8);
}

@keyframes wiggle {
  0% {
    transform: rotate(0deg);
  }
  18% {
    transform: rotate(3deg);
  }
  33% {
    transform: rotate(-3deg);
  }
  50% {
    transform: rotate(0deg);
  }
}

.content {
  display: grid;
  grid-template-columns: 12vw 1fr 12vw;
  align-items: center;
  width: 100vw;
}

button.arrow {
  background: none;
  border: none;
  place-items: center;
  text-align: center;
  width: 8vw;
  margin-left: auto;
  margin-right: auto;
}

button.arrow:hover {
  transform: scale(var(--scale-hover-small));
  cursor: pointer;
  transition: var(--transition-quick);
}

button.arrow.arrowNotActive {
  filter: brightness(40%);
}

.splide {
  padding-bottom: 2vh;
  width: 76vw;
  place-items: center;
  align-items: center;
}

.splide .slide {
  margin-top: 2vw;
  margin-bottom: 2vw;
}
</style>
