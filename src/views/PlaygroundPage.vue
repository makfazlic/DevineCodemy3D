<template>
  <div
    v-if="!levelDescription.board && levelDescription.name"
    id="unplayablebanner"
  >
    <h1>This level is not available to you.</h1>
    <p id="subtitle">
      Complete the previous levels to unlock this one.
    </p>
  </div>

  <TextPopup
    v-if="worldCompleted"
    :title="'Congratulations'"
    :auto-open="true"
    :description="worldDescription.congratulationsMessage"
    :show-button="false"
    :callback="closeCongratsMessage"
  />

  <vue-basic-alert
    ref="alert"
    :duration="300"
    :close-in="5000"
  />
  <ArrowComponent :link="'/listlevels'" />

  <div id="content">
    <div id="editor_left_pane">
      <div id="navbar">
        <font-awesome-icon
          class="nav-svg"
          icon="arrow-left"
          @click="previousLevel"
        />
        <h1> {{ levelDescription.name }}</h1>
        <font-awesome-icon
          class="nav-svg"
          icon="arrow-right"
          @click="nextLevel"
        />
        <TextPopup
          v-if="Object.keys(worldDescription).length > 0"
          id="worldDescComponent"
          ref="worldDesc"
          :auto-open="shouldDisplayWorldDescription()"
          :title="worldDescription.name"
          :description="worldDescription.descriptionMessage"
          :callback="setCookie"
        />
      </div>
      <!-- eslint-disable vue/no-v-html -->
      <div
        id="level_description"
        v-html="levelDescription.description"
      />
      <!--eslint-enable-->
      <div id="blocklyDiv" />
    </div>

    <div id="playground_right_pane">
      <div
        v-if="levelDescription.board"
        id="render"
      >
        <PlaygroundP
          ref="playground"
          :level-description="levelDescription"
        />
      </div>
      <div
        v-else
        class="loading"
      >
        <h2>Loading level...</h2>
      </div>

      <div
        v-if="levelDescription.board"
        class="button_runcode_box"
      >
        <button
          v-if="!running || stopped"
          id="run"
          class="button_code"
          @click="runCode()"
        >
          Run Code
        </button>
        <button
          v-else
          id="stop"
          class="button_code"
          @click="stopCodeExecution()"
        >
          Stop Code
        </button>
      </div>
    </div>
  </div>
  <SvgBackground />
</template>

<script>
import Playground from "../js/PlaygroundPage/scene/playground.js";

export default Playground;
</script>

<style scoped>

/* COLORS */

.button_code#run {
  background-color: #0fcb00;
}

.button_code#stop {
  background-color: #c70000;
}

/* ###################### */

/* LAYOUT */

#unplayablebanner {
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  text-align: center;
    color: red;
    backdrop-filter: blur(5px);
}

#unplayablebanner * {
  font-size: 2vw;
}

#unplayablebanner h1 {
  font-size: 4vw;
}


#content {
  display: grid;
  grid-template-columns: .4fr .6fr;
  grid-template-areas:
      "left_pane right_pane";
  overflow: hidden;
    height: 100%;
    width: 100%;
}

#editor_left_pane {
  grid-area: left_pane;

  display: grid;
  grid-template-rows: 1fr 3fr 4fr;
  grid-template-areas:
    "navbar"
    "text_box"
    "textarea";
}

#playground_right_pane {
  grid-area: right_pane;
  overflow: hidden;
}

/* Navbar */
#navbar {
  grid-area: navbar;
  display: flex;
  flex-direction: row;
  justify-content: center;
  align-items: center;
  gap: 2vh;
}

.nav-svg {
  transition: all .1s ease-in-out;
  height: 4vh;
}

.nav-svg:hover {
  transform: scale(var(--scale-hover-big));
}

#level_description {
  margin-bottom: 2vh;

  margin-left: 1vh;
  margin-right: 1vh;
  padding: 1vh;

  border-bottom-style: solid;
  border-bottom-width: 0.3vh;
  border-bottom-left-radius: 0.7vh;
  border-bottom-right-radius: 0.7vh;
    border-color: var(--text-contrast);
    color: var(--text-contrast);
}

div#render :first-child {
  height: 100vh;
}


/*///////////*/


.loading {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
    background-color: var(--bg-contrast1);
}

.button_runcode_box {
  position: absolute;
  min-height: 10%;
  min-width: 20%;
  right: 17%;
  bottom: 2vh;
  display: flex;
  align-items: center;
  justify-content: center;
}

.button_code {
  height: 70%;
  width: 80%;
  min-height: 50px;
  min-width: 70px;
  border-radius: 10px;
  cursor: pointer;
  border: none;
  transition: all var(--transition-quick);
  font-family: "Nove", Helvetica, Arial, sans-serif;
  font-size: 2em;
  color: black;
}

.button_code:hover {
  transform: scale(var(--scale-hover-small));
}

.button_code:active {
  filter: brightness(0.9);
  transform: scale(1);
}

</style>
