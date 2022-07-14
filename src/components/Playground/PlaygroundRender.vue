<template>
  <Renderer
    v-if="levelDescription"
    id="renderer"
    ref="renderer"
    antialias
    :orbit-ctrl="{ enableDamping: true }"
    width="1200"
    height="1000"
  >
    <Camera
      ref="camera"
      :position="{ x: computeCameraX(), y: computeCameraY(), z: computeCameraZ() }"
    />
    <Scene
      ref="scene"
      background="DeepSkyBlue"
    />
  </Renderer>
  <div class="collectables_container">
    <div class="collectable">
      <img
        class="gemma_img"
        src="../../assets/PlaygroundPage/coin.gif"
        alt="gem img"
      >
      <span>
        {{ levelDescription.board.items.length - coinsToCollect }}
        /
        {{ levelDescription.board.items.length }}
      </span>
    </div>
  </div>
</template>

<script>
import Playground from '../../js/PlaygroundPage/scene/playgroundP.js';

export default Playground;
</script>

<style scoped>

/* COLORS */
.collectables_container {
  background: var(--bg);
  border-bottom: #f9fff9 solid 2px;
  height: 6vh;
    position: absolute;
    top: 0;
    margin: 1vh;

    display: flex;
    align-items: center;
    justify-content: left;
    border-radius: 1vh;
}

.collectables_container .collectable {
  display: flex;
  flex-direction: row;
  align-items: center;
  gap: 1vh;
  margin: 1vh;
}
.collectables_container .collectable img {
  height: 6vh !important;
}

</style>
