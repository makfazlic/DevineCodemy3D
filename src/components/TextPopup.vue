<template>
  <button
    v-if="showButton"
    @click="show"
  >
    <worldinfo class="button-svg" />
  </button>

  <Transition
    id="outertransition"
    name="fade"
  >
    <div
      v-show="isShown"
      id="back"
      class="back"
    >
      <Transition
        id="innertransition"
        name="fade"
      >
        <div
          v-show="isShown"
          class="text-popup"
        >
          <h1 id="popup-title">
            {{ title }}
          </h1>
          <!-- eslint-disable vue/no-v-html -->
          <span
            class="textBody"
            v-html="description"
          />
          <!--eslint-enable-->
          <div class="button-box">
            <button
              id="accept"
              @click="hide();executeCallback()"
            >
              Ok
            </button>
          </div>
        </div>
      </Transition>
    </div>
  </Transition>
</template>

<script>
import worldinfo from "@/assets/world_info.svg";
import {show, hide, executeCallback} from "@/js/ProfilePage/textpopup.js";

export default {
  name: 'TextPopup',
  components: {
    worldinfo
  },
  props: {
    title: {
      type: String,
      required: true,
    },
    description: {
      type: String,
      required: true,
    },
    callback: {
          type: Function,
          default: () => {
            return {}
          }
        },
    autoOpen: {
      type: Boolean,
      required: false,
      default: false
    },
    showButton: {
      type: Boolean,
      default: true
    }
  },
  data() {
    return {
      isShown: false,
    }
  },
  mounted() {
    if (this.autoOpen) {
      this.show();
    }
  },
  methods: {
    show,
    hide,
    executeCallback,
  },

}
</script>

<style scoped>

button {
  border-radius: 10px;
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 1vh;
  border: none;
  transition: all .1s ease-in-out;
    background: linear-gradient(var(--accent1), var(--accent1-contrast));
    color: var(--text-contrast1);
}

.button-svg {
  height: 5vh;
}

button:hover {
  transform: scale(var(--scale-hover-small));
}

.text-popup {
  width: 80%;
  height: 80%;
  max-height: 80vh;
  display: grid;
  grid-template-rows: 1.5fr 3fr 1fr;
  grid-template-areas: 
    "title"
    "description"
    "button";
  border-radius: 2vh;
  overflow-y: hidden;
    background: linear-gradient(90deg, var(--bg) 0%, var(--bg-contrast2) 50%);
}

#outertransition .fade-enter-active {
  transition: all 0.1s cubic-bezier(1, 0.5, 0.8, 1);
}

#outertransition .fade-leave-active {
  transition: all 0.1s cubic-bezier(1, 0.5, 0.8, 1);
}

#outertransition .fade-leave-to {
  opacity: 0;
}

#outertransition .fade-enter-from {
  opacity: 0;
}


#innertransition .fade-enter-active {
  transition: all 0.3s cubic-bezier(1, 0.5, 0.8, 1);
}

#innertransition .fade-leave-active {
  transition: all 0.3s cubic-bezier(1, 0.5, 0.8, 1);
}

#innertransition .fade-leave-to {
  opacity: 0;
  transform: scale(0);

}

#innertransition .fade-enter-from {
  opacity: 0;
  transform: scale(0);
}


#popup-title {
  font-size: 6vh;
  margin-top: 5vh;
  text-align: center;
  grid-area: title;
    color: var(--accent1);

}

.textBody {
  grid-area: description;
  width: 70%;
  margin: 0 auto;
  height: 100%;
  font-size: 1em;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-direction: column;
}

.button-box {
  display: flex;
  height: 100%;
  width: 100%;
  justify-content: center;
  align-items: center;
  grid-area: button;
}

button#accept {
  height: 2vh;
  width: 20vw;
  min-height: 50px;
  min-width: 100px;
  border-radius: 10px;
  cursor: pointer;
  border: none;
  transition: all var(--transition-quick);
  font-family: "Nove", Helvetica, Arial, sans-serif;
  font-size: 2em;
}

button#accept:hover {
  transform: scale(var(--scale-hover-small));
  box-shadow: #191b19;
}

button#accept:active {
  filter: brightness(0.9);
  transform: scale(1);

}

.back {
  top: 0;
  right: 0;
  position: absolute;
  display: flex;
  align-items: center;
  justify-content: center;
  width: 100vw;
  height: 100vh;
  color: black;
  background-color: rgba(0, 0, 0, 0);
  z-index: 999999999999;
  backdrop-filter: blur(0px);
  -webkit-animation: darker 0.2s;
  animation: darker 0.2s;
  -webkit-animation-fill-mode: forwards;
  animation-fill-mode: forwards;
}

@keyframes darker {
  100% {
    background-color: rgba(0, 0, 0, 0.5);
    backdrop-filter: blur(2px);
  }
}

</style>