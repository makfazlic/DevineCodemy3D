<template>
  <div class="grid_team">
    <ArrowComponent :link="'/about'" />
    <h1>Our Team</h1>
    <div class="centered">
      <div class="content">
        <Member
          id="a"
          :name="'Albert Cefreda'"
          :role="'team leader'"
        />
        <Member
          id="b"
          :name="'Alessandro Gobbetti'"
          :role="'team leader'"
        />
        <Member
          id="c"
          :name="'Bojan Lazarevski'"
          :role="'developer'"
        />
        <Member
          id="d"
          :name="'Cindy Guerrero Toro'"
          :role="'developer'"
        />
        <Member
          id="e"
          :name="'Enrico Benedettini'"
          :role="'developer'"
        />
        <Member
          id="f"
          :name="'Alen Sugimoto'"
          :role="'developer'"
        />
        <Member
          id="g"
          :name="'Diell Kryeziu'"
          :role="'developer'"
        />
        <Member
          id="h"
          :name="'Alessandro Cagnani'"
          :role="'developer'"
        />
        <Member
          id="i"
          :name="'Alfio Vavassori'"
          :role="'developer'"
        />
        <Member
          id="l"
          :name="'Harkeerat Singh Sawhney'"
          :role="'developer'"
        />
        <Member
          id="m"
          :name="'Alan Copa'"
          :role="'developer'"
        />
        <Member
          id="n"
          :name="'Roberto Palmieri'"
          :role="'developer'"
        />
        <Member
          id="o"
          :name="'Daniela Gjorgjieva'"
          :role="'developer'"
        />
        <Member
          id="p"
          :name="'Mak Fazlic'"
          :role="'developer'"
        />
      </div>
    </div>
  </div>
  <SvgBackground />
</template>

<script>
import ArrowComponent from "@/components/BackArrow";
import Member from "@/components/MemberComponent";
import SvgBackground from "@/components/SvgBackground";

export default {
  name: "TeamPage",
  components: {
    ArrowComponent,
    Member,
    SvgBackground
  }
}
</script>

<style scoped>

.grid_team {
  height: 100vh;
  width: 100vw;
  max-height: 100vh;
  max-width: 100vw;
  display: grid;
  /*grid-auto-columns: 1fr;*/
  grid-template-columns: 5% 1.9fr 7%;
  grid-template-rows: 10% 1.8fr 5%;
  /*gap: 0 0;*/
  grid-template-areas:
    ". h1 ."
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
  width: 95%;
  max-width: 95%;
  display: grid;
  /*grid-auto-columns: 1fr;*/
  grid-template-columns: 14% 14% 14% 14% 14% 14% 14%;
  grid-template-rows: 1fr 1fr;
  /*gap: 20px 20px;*/
  grid-template-areas:
    "a b c d e f g"
    "h i l m n o p";
}

#a {
  grid-area: a;

}

h1 {
  grid-area: h1;
  font-size: 3em;
  display: flex;
  justify-content: center;
  align-items: center;
  margin-right: 2%;
}

</style>