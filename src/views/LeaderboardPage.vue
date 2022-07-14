<template>
  <ArrowComponent :link="'/hub'" />
  <h1 id="leaderboard_heading">
    Leaderboard
  </h1>
  <h3 id="my_rank_heading">
    My rank: {{ my_rank }}
  </h3>
  <!--  Under is real leaderboard -->
  <table class="leaderboard_table">
    <caption />
    <thead>
      <tr>
        <th 
          v-for="field in lb_fields_text"
          :key="field"
          scope="col"
        >
          {{ field }}
        </th>
      </tr>
    </thead>
    <tbody>
      <tr
        id="my_row"
        @click="redirect"
      >
        <td
          v-for="field in lb_fields_fetch"
          :key="field"
        >
          <img
            v-if="field === 'avatarUrl'"
            alt="avatar"
            class="leaderboard-img"
            :src="this_player[field]"
          >

          <div
            v-else
            class="user-field"
          >
            {{ this_player[field] }}
          </div>
          <div
            v-if="field === 'rank'"
            class="user-field"
          >
            {{ getMyRank() }}
          </div>
        </td>
      </tr>
    </tbody>
    <tbody>
      <tr
        v-for="(user,i) in ordered_players"
        :key="user"
        @click="select($event)"
      >
        <td
          v-for="field in lb_fields_fetch"
          :id="user.id"
          :key="field"
        >
          <img
            v-if="field === 'avatarUrl'"
            alt="avatar"
            class="leaderboard-img"
            :src="user[field]"
          >

          <div
            v-else
            class="user-field"
          >
            {{ user[field] }}
          </div>
          <div
            v-if="field === 'rank'"
            class="user-field"
          >
            {{ i += 1 }}
          </div>
        </td>
      </tr>
    </tbody>
  </table>
  <SvgBackground :show-s-v-g-landscape="true" />
</template>

<script>
import ArrowComponent from "@/components/BackArrow";
import SvgBackground from "@/components/SvgBackground";

export default {
  name: "LeaderboardPage",
  components: {
    ArrowComponent,
      SvgBackground
  },
  data() {
    return {
      id: this.$store.state.userId,
      index: 1,
      my_rank: 0,
      this_player: {},
      ordered_players: [],
      lb_fields_fetch: ['avatarUrl', 'rank', 'username', 'completedLevels'],
      lb_fields_text: ['', 'Rank', 'Username', "Completed Levels"]
    }
  },
  async mounted() {
    await fetch(`${process.env.VUE_APP_CODELAND_BACKEND_HOST}/stats/leaderboard`, {
      credentials: "include"
    })
        .then((response) => {
          return response.json();
        })
        .then((users) => {
          this.ordered_players = users;
          this.my_rank = this.getMyRank();
        })
        //Debug then - remove later
        .then(() => {
          console.log(this.ordered_players)
        });
    await fetch(`${process.env.VUE_APP_CODELAND_BACKEND_HOST}/users/${this.id}`, {
      credentials: "include"
    })
        .then((response) => {
          return response.json();
        })
        .then((user) => {
          this.this_player = user;
          this.getMyCompletedLevels();
        })
        .then(() => {
          console.log("Current player: ");
          console.log(this.this_player)
        });
  },
  methods: {
    getMyRank() {
      for (let i = 0; i < this.ordered_players.length; i++) {
        if (this.ordered_players[i].id === this.id)
          return i + 1;
      }
    },
    getMyCompletedLevels() {
      this.ordered_players.forEach((player) => {
        if (player.id === this.this_player.id) {
          this.this_player.completedLevels = player.completedLevels;
        }
      })
    },
    select: function (event) {
      if (event.target.id === "") {
        window.location = "/profile?id=" + event.target.parentNode.id
      } else {
        window.location = "/profile?id=" + event.target.id
      }
    },
    redirect: function () {
      window.location = "/profile"
    }
  }
}
</script>

<style scoped>
h1, h3 {
  text-align: center;
}

.leaderboard_table {
  border-collapse: collapse;

  margin-left: auto;
  margin-right: auto;

  border: 1px solid white;


}

.leaderboard_table th, td {
  padding: 10px;

}

.leaderboard_table th {
  border-bottom: 1px solid white;
  color: white;
  cursor: default;

}


.leaderboard_table tr:nth-child(even) {
  background-color: white;
  cursor: pointer;
}

.leaderboard_table tr:nth-child(odd) {
  background-color: black;
  color: white;
  cursor: pointer;
}

.leaderboard_table tr:hover {
  color: red;

}

.leaderboard-img {
  width: 50%;
  border-radius: 100%;
}

#leaderboard_heading {
  font-size: 50px;
  color: white;
}

#my_rank_heading {
  font-size: 20px;
  color: white;
}

#my_row {
  margin-bottom: 15px;
  width: 30%;
  background-color: #ec6262;
  border-bottom: 3px solid white;
  border-top: 3px solid white;
}

#my_row:hover {
  color: black;
}

</style>