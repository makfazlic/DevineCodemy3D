<template>
  <div class="lvlInfoBox">
    <div
      v-if="err"
      class="centered"
    >
      no level completed
    </div>
    <div
      v-else
      class="lvlInfoLowerBox"
    >
      level {{ levelNum }}<br>
      <span v-if="levelInfo.completed">&check;</span>
      <span v-else>&#215;</span>
      {{ (levelInfo.allAttempts).length }} attempts
    </div>
  </div>
</template>

<script>
export default {
    name: "LevelStats",
    props: {
        levelNum: {
            type: Number,
            required: true
        },
        levelInfo: {
          type: Function,
          default: () => {
            return {}
          }
        },
        err: {
            type: Boolean,
            required: true
        }
    }
}
</script>

<style scoped>

span {
    font-size: 50px;
}

.lvlInfoBox {
    border-radius: 15px;
    padding: 10px;
    margin-top: 25px;
    font-size: 25px;
    width: 80%;
    min-height: 100px;
    background-color: rgba(255,255,255,.03);
    box-shadow: 0 45px 50px rgba(0, 0, 0, 0.25);
}

.lvlInfoLowerBox {
    display: flex;
    justify-content: space-around;
    align-items: baseline;
}

.centered {
    height: 100%;
    width: 100%;
    display: flex;
    align-items: center;
    justify-content: center;
}


</style>