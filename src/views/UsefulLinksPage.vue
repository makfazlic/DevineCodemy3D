<template>
  <div class="grid_links">
    <ArrowComponent :link="'/about'" />
    <div class="centered">
      <div class="content">
        <h1>Useful Links</h1>
        <button
          id="top"
          @click="docFunction()"
        >
          <h2>Documentation</h2>
          <img 
            alt="notion_image" 
            src="../assets/UsefulLinkPage/Notion_app_logo.png"
          >
        </button>
        <button
          id="bottom"
          @click="repoFunction()"
        >
          <h2>GitLab Repository</h2>
          <img 
            alt="gitLab_image" 
            src="../assets/UsefulLinkPage/GitLab_logo.svg.png"
          >
        </button>
      </div>
    </div>
  </div>
  <SvgBackground />
</template>

<script>
import ArrowComponent from "@/components/BackArrow";
import {repoFunction, docFunction} from "../js/UsefulLinksPage/usefulLinks.js";
import SvgBackground from "@/components/SvgBackground";

export default {
  name: "UsefulLinksPage",
  components: {
    ArrowComponent,
    SvgBackground
  },
  methods: {
    repoFunction,
    docFunction
  }
}
</script>

<style scoped>

.grid_links {
  height: 100vh;
  width: 99vw;
  display: grid;
  /*grid-auto-columns: 1fr;*/
  grid-template-columns: 5% 1.9fr 7%;
  grid-template-rows: 7% 1.8fr 7%;
  /*gap: 0 0;*/
  grid-template-areas:
    "arrow . ."
    ". centered ."
    ". . .";
}

.centered {
  /*max-height: 100vh;*/
  /*max-width: 100vw;*/
  display: flex;
  justify-content: center;
  align-items: center;
  grid-area: centered;
}

/*.center {*/
/*  !*max-height: 100vh;*!*/
/*  !*max-width: 100vw;*!*/
/*  display: flex;*/
/*  justify-content: center;*/
/*  align-items: center;*/
/*  grid-area: name;*/
/*}*/

.content {
  height: 100%;
  width: 80%;
  display: grid;
  /*grid-auto-columns: 1fr;*/
  grid-template-columns: 3fr;
  grid-template-rows: 0.1fr 1fr 1fr;

  grid-template-areas:
    "title"
    "doc"
    "repo";
}

button {
  transition: all .1s ease-in-out;
  background-color: Transparent;
  border-color: Transparent;
  height: 100%;
  width: 100%;
  display: flex;
  justify-content: space-around;
  align-items: center;
}

button:hover {
  transform: scale(1.2);
}

#top {
  grid-area: doc;
}

#bottom {
  grid-area: repo;
}

img {
  grid-area: img;
  max-width: 18vw;
  min-height: 1vh;
}

h2 {
  font-size: 3vw;
  grid-area: name;
}

h1 {
  grid-area: title;
  display: flex;
  justify-content: space-around;
  align-items: center;
  font-size: 4vw;
  margin-bottom: 40px;
}

</style>