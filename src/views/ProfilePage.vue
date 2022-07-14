<template>
  <div class="container-out">
    <ArrowComponent :link="'/hub'" />
    <div class="centered">
      <div class="content">
        <div class="ProfileCard">
          <ProfileCard />
        </div>
        <div class="ProfileStats">
          <p>Level Completion</p>
          <div class="scrollable">
            <levelStats
              v-for="(info, levelNum) in levelData"
              :key="levelNum"
              :level-info="info"
              :level-num="levelNum"
              :err="levelData.error"
            />
          </div>
        </div>
        <div class="ProgressBar">
          <h2>THE JOURNEY</h2>
          <ProgressBar :perc="completedLevelPerc" />
        </div>
      </div>
    </div>
  </div>
  <SvgBackground />
</template>

<script>
import ProfileCard from '@/components/ProfilePage/ProfileCard.vue';
import ArrowComponent from '@/components/BackArrow.vue';
import ProgressBar from "@/components/ProfilePage/ProgressBar";
import levelStats from "@/components/ProfilePage/UserStats";
import SvgBackground from "@/components/SvgBackground";

export default {
  name: 'ProfilePage',
  components: {
    ProgressBar,
    ProfileCard,
    ArrowComponent,
    levelStats,
    SvgBackground
  },
  data() {
    return {
      levelData: {},
      completedLevelPerc: 0
    }
  },
  beforeCreate() {
    let id

    if (!this.$route.query.id) {
      id = this.$store.state.userId
    } else {
      id = this.$route.query.id
    }


      fetch(`${process.env.VUE_APP_CODELAND_BACKEND_HOST}/stats/${id}`,
          {
            method: "GET",
            credentials: "include"
          })
          .then(res => res.json())
          .then(levelData => {

            let completedLevels
            if (levelData && levelData.data) {
              this.levelData = levelData.data
              completedLevels = Object.values(levelData.data).filter((level) => level.completed === true).length;
            } else {
              this.levelData.error = "Please try the game before"
              completedLevels = 0
            }

            this.completedLevelPerc = (completedLevels * 100) / 15;
          })

  }

}
</script>

<style scoped>
/* COLORS */

.ProgressBar h2 {
  color: #2c3e50;
}

/* ####### */



.container-out {
  height: 100vh;
  width: 100vw;
  max-height: 100vh;
  max-width: 100vw;

  display: grid;
  grid-auto-columns: 1fr;
  grid-template-columns: 5% 1.9fr 7%;
  grid-template-rows: 7% 1.8fr 7%;
  gap: 0 0;
  grid-template-areas:
  "arrow . ."
  ". main ."
  ". . .";
}

.centered {
  display: flex;
  justify-content: center;
  align-items: center;
  grid-area: main;
}

.content {
  height: 100%;
  width: 80%;
  display: grid;
  grid-auto-columns: 1fr;
  grid-template-columns: 1.4fr 0.6fr;
  grid-template-rows: 1.2fr 0.8fr;
  gap: 20px 20px;
  grid-template-areas:
    "ProfileCard ProfileStats"
    "ProgressBar ProgressBar";
}


.ProgressBar {
  grid-area: ProgressBar;
  border-radius: 25px;
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: column;
    background: linear-gradient(180deg, var(--accent1-contrast), var(--accent1));
}

.ProgressBar > h2 {
  font-size: 2em;
}

.ProfileCard {
  padding: 10px;
  grid-area: ProfileCard;
  border-radius: 25px;
  max-height: 50vh;
    background: linear-gradient(90deg, var(--accent1-contrast), var(--accent1));

}

.ProfileStats {
  grid-area: ProfileStats;
  border-radius: 25px;
  font-size: 2vw;
  padding: 1vh;
  text-align: center;
  /*padding: 10px;*/
  overflow: hidden;
  max-height: 50vh;
    background: linear-gradient(90deg, var(--accent1-contrast), var(--accent1));
    color: var(--text-contrast1);

}

.scrollable::-webkit-scrollbar {
  display: none;
}

.scrollable {
  overflow-y: auto;
  height: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
}


</style>