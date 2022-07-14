<template>
  <div class="grid">
    <ArrowComponent :link="'/about'" />
    <div class="centered">
      <div class="content">
        <h2>What is Devine Codemy?</h2>
        <p>
          <iframe 
            title="README"
            src="../../README.md"
            height="400"
            width="95%"
          />
          <br>
          The Divine Codemy is an educational Web Application that teaches coding to unexperienced developers.
          Its goal is to educate about coding with an uncommon approach to make it fun and easier to understand.
          It takes inspiration from the macOS game Swift Playground.
          The project spans from March 10th, 2022 to June 4th, 2022 (86 days) as required from the course
          Software Atelier 4: Software Engineering Project SP 2022, part of BSc at USI (Università della Svizzera
          Italiana).
        </p>
      </div>
    </div>
  </div>
  <SvgBackground />
</template>

<script>
import ArrowComponent from "@/components/BackArrow";
import SvgBackground from "@/components/SvgBackground";

export default {
  name: "GameInformationPage",
  components: {
    ArrowComponent,
    SvgBackground
  }
}
</script>

<style scoped>

.grid {
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
    "arrow . ."
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
  grid-template-rows: 0.1fr 1fr;
  gap: 20px 20px;
  grid-template-areas:
    "h2"
    "p";
}

h2 {
  grid-area: h2;
  font-size: 3em;
}

p {
  grid-area: p;
  font-size: 1.5em;
}

</style>