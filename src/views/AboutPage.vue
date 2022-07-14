<template>
  <div class="grid_about">
    <ArrowComponent :link="'/hub'" />
    <div class="centered">
      <div class="content">
        <HubButton
          class="game"
          :label="'The Game'"
          :redirect-path="'./gameinf'"
          :bg-image-path="'../../assets/HubPage/About.png'"
          :gradient-color="'#fff200'"
        />
        <HubButton
          class="links"
          :label="'Useful Links'"
          :redirect-path="'./links'"
          :bg-image-path="'../../assets/HubPage/About.png'"
          :gradient-color="'#00ffd1'"
        />
        <HubButton
          class="team"
          :label="'Our Team'"
          :redirect-path="'./team'"
          :bg-image-path="'../../assets/HubPage/About.png'"
          :gradient-color="'#fc02ed'"
        />
      </div>
    </div>
  </div>
  <SvgBackground />
</template>

<script>
import ArrowComponent from "@/components/BackArrow";
import HubButton from "@/components/Hub/HubButton";
import SvgBackground from "@/components/SvgBackground";

export default {
  name: "AboutPage",
  components: {
    ArrowComponent,
    HubButton,
    SvgBackground
  }
}
</script>

<style scoped>

body {
  max-height: 100vh;
  max-width: 100vw;
}

.grid_about {
  height: 100vh;
  width: 100vw;
  max-height: 100vh;
  max-width: 100vw;
  display: grid;
  /*grid-auto-columns: 1fr;*/
  grid-template-columns: 5% 1.9fr 7%;
  grid-template-rows: 7% 1.8fr 7%;
  /*gap: 0 0;*/
  grid-template-areas:
    ". . ."
    ". centered ."
    ". . .";
}

.centered {
  display: flex;
  justify-content: center;
  align-items: center;
  grid-area: centered;
}

.content {
  height: 100%;
  width: 80%;
  display: grid;
  /*grid-auto-columns: 1fr;*/
  grid-template-columns: 1fr;
  grid-template-rows: 1fr 1fr 1fr;
  gap: 20px 20px;
  grid-template-areas:
    "game"
    "links"
    "team";
}

.game {
  grid-area: game;
}

.links {
  grid-area: links;
}

.team {
  grid-area: team;
}

</style>