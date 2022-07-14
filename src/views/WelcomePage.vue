<template>
  <div class="home">
    <SvgBackground
      :show-solid-color-b-g="true"
      :show-s-v-g-landscape="true"
    />
    <div class="container">
      <div id="left_welcome">
        <img
          src="../assets/WelcomePage/dante.png"
          alt="logo"
          class="welcomelogo"
        >
        <div class="welcomeTitle">
          <div class="wrap">
            <div class="line">
              <div class="left">
                <div class="content">
                  <span class="spanSlow"><h1>Welcome</h1></span>
                </div>
              </div><!--
                        -->
              <div class="right">
                <div class="content">
                  <span class="spanSlow"><h1>Welcome</h1></span>
                </div>
              </div>
            </div>
            <div class="line">
              <div class="left">
                <div class="content">
                  <span class="spanSlow"><h1>To</h1></span>
                </div>
              </div><!--
                        -->
              <div class="right">
                <div class="content">
                  <span class="spanSlow"><h1>To</h1></span>
                </div>
              </div>
            </div>
            <div class="line">
              <div class="left">
                <div class="content">
                  <span class="spanFast"><h1>Devine</h1></span>
                </div>
              </div><!--
                        -->
              <div class="right">
                <div class="content">
                  <span class="spanFast"><h1>Devine</h1></span>
                </div>
              </div>
            </div>
            <div class="line">
              <div class="left">
                <div class="content">
                  <span class="spanSlow"><h1>Codemy</h1></span>
                </div>
              </div><!--
                            -->
              <div class="right">
                <div class="content">
                  <span class="spanSlow"><h1>Codemy</h1></span>
                </div>
              </div>
            </div>
          </div>
        </div>
        <router-link :to="'/hub'">
          <p id="playButton">
            Get Started
            <arrow
              id="arrow"
              style="color: #DC2626;"
            />
          </p>
        </router-link>
      </div>
      <div id="right_welcome">
        <transition
          name="slide-fade"
          mode="out-in"
        >
          <div 
            :key="activeSection.title"
          >
            <img
              alt="welcome_image"
              class="image"
              :src="activeSection.src"
            >
          </div>
        </transition>
      </div>
      <SvgBackground
        :show-solid-color-b-g="false"
        :show-s-v-g-landscape="true"
      />
    </div>
  </div>
</template>

<script>
import SvgBackground from "@/components/SvgBackground";

import {nextSection} from '../js/WelcomePage/welcome.js';


export default {
  name: "WelcomePage",
  components: {
    SvgBackground
  },
  data() {
    return {
      // Make sure to put images in 1:1 aspect ratio
      sections: [{
        title: "Section A",
        src: "/assets/covers/cover1.png"
      },
        {
          title: "Section B",
          src: "/assets/covers/cover2.png"
        },
        {
          title: "Section C",
          src: "/assets/covers/cover3.png"
        },
        {
          title: "Section D",
          src: "/assets/covers/cover4.png"
        }
      ],
      activeSectionIndex: 0,
    }
  },
  computed: {
    activeSection() {
      return this.sections[this.activeSectionIndex % this.sections.length]
    }
  },
  mounted() {
    setInterval(() => this.nextSection(), 5000);
        //add an evnetlistener for mouse
        this.$el.addEventListener('mousemove', this.onMouseMove);
        this.$el.addEventListener('resize', this.onResize);
  },
  methods: {
    nextSection,
    onMouseMove(e) {
      const spansSlow = document.querySelectorAll('.spanSlow');
      const spansFast = document.querySelectorAll('.spanFast');
      let width = window.innerWidth;
      let normalizedPosition = e.pageX / (width / 2) - 1;
      let speedSlow = 100 * normalizedPosition;
      let speedFast = 200 * normalizedPosition;
      spansSlow.forEach((span) => {
        span.style.transform = `translate(${speedSlow}px)`;
      });
      spansFast.forEach((span) => {
        span.style.transform = `translate(${speedFast}px)`
      })
    }
  },

};

</script>

<style scoped>

.welcomeTitle {
  z-index: 1;
  height: 10vh;
  display: flex;
  justify-content: center;
  align-items: center;
  padding-top: 10vh;
}

.left h1 {
  font-size: 4vw;
    color: var(--text);
}

.right h1 {
  font-size: 4vw;
    color: var(--text-contrast);
}

.line {
  width: 100vw;
}

.left, .right {
  width: 50vw;
  overflow: hidden;
  display: inline-block;
}

.left {
  width: 50vw;
  color: yellow;
  transform: skew(0deg, -15deg);
}

.right {
  float: right;
  width: 50vw;
  transform: skew(0deg, 15deg);
}

.left .content {
  width: 100vw;
  text-align: center;
}

.right .content {
  width: 100vw;
  text-align: center;
  transform: translate(-50vw);
}

.bottom_pic img {
  position: absolute;
  bottom: 0;
  width: 100vw;
  height: auto;
  z-index: 0;
}

span {
  display: inline-block;
  text-transform: uppercase;
  line-height: .8;
  transition: ease-out .6s;
}


#playButton {
  z-index: 1;
  font-family: "Nove", sans-serif;
  font-size: 3vh;
  color: #DC2626;
  margin: 2vh;
  transition-duration: 500ms;
  -webkit-text-stroke-width: 1px;
  -webkit-text-stroke-color: black;

}

#playButton:hover {
  z-index: 2;
  cursor: pointer;
  transform: scale(var(--scale-hover-small));
}

#playButton:active {
  filter: brightness(0.8);
}

#arrow {
  height: 2vh;
  margin: 0;
  margin-left: 0.1vw;
  fill: #DC2626;

}

#left_welcome {
  width: 35%;
}

#right_welcome {
  width: 35%;
}

.welcomelogo {
  width: 5vw;
  margin: 0 auto;
  margin-bottom: 13vh;
  margin-top: -17vh;
}

.container {
  display: flex;
  flex-direction: row;
  align-items: center;
  justify-content: center;
  text-align: center;
  position: relative;
  top: 25vh;
  margin: 0 auto;
}

.home {
  max-height: 100vh;
  height: 100vh;
  width: 100vw;
  max-width: 100vw;
  padding: 0;
  margin: 0;
}

.image {
  margin-top:-10vh;
  height: 60vh;
  border-radius: 5%;
}

.slide-fade-enter-active {
  transition: all .8s ease;
}

.slide-fade-leave-active {
  transition: all .8s cubic-bezier(1.0, 0.5, 0.8, 1.0);
}

.slide-fade-enter,
.slide-fade-leave-to

  /* .slide-fade-leave-active below version 2.1.8 */
{
  transform: translateX(10px);
  transform: scale(0.8);
  opacity: 0;
}
</style>
