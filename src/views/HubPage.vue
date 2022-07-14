<template>
  <ArrowComponent :link="'/'" />
  <div class="content">
    <router-link
      :to="'./profile'"
      class="hiuser_router"
    >
      <div class="hiuser">
        <p class="name">
          Hi, {{ user.name }}
        </p><img
          alt="Profile pic"
          :src="user.avatarUrl"
          class="avatar"
        >
      </div>
    </router-link>

    <div class="button_space">
      <router-link
        :to="'./listlevels'"
        class="block1"
      >
        <div class="overlay">
          <h1 class="init_block_title1">
            Levels
          </h1>
          <h1 class="block_title1">
            See all
          </h1>
        </div>
      </router-link>
      <div class="block2">
        <router-link
          :to="'./leaderboard'"
          class="sub_block1"
        >
          <div class="overlay">
            <h1 class="init_block_title2">
              Leaderboard
            </h1>
            <h1 class="block_title2">
              Who's doing good?
            </h1>
          </div>
        </router-link>
        <router-link
          :to="'./about'"
          class="sub_block2"
        >
          <div class="overlay">
            <h1 class="init_block_title3">
              About
            </h1>
            <h1 class="block_title3">
              Check info
            </h1>
          </div>
        </router-link>
      </div>
    </div>
    <router-link
      :to="``"
      class="logout_router"
    >
      <div
        class="logout"
        @click="backendLogOut"
      >
        <p class="logout_message">
          Log out of {{ user.username }}
        </p>
        <svg
          xmlns="http://www.w3.org/2000/svg"
          class="logout_icon"
          viewBox="0 0 512 512"
        >
          <!--! Font Awesome Pro 6.1.1 by @fontawesome - https://fontawesome.com License - https://fontawesome.com/license (Commercial License) Copyright 2022 Fonticons, Inc. -->
          <path
            d="M96 480h64C177.7 480 192 465.7 192 448S177.7 416 160 416H96c-17.67 0-32-14.33-32-32V128c0-17.67 14.33-32 32-32h64C177.7 96 192 81.67 192 64S177.7 32 160 32H96C42.98 32 0 74.98 0 128v256C0 437 42.98 480 96 480zM504.8 238.5l-144.1-136c-6.975-6.578-17.2-8.375-26-4.594c-8.803 3.797-14.51 12.47-14.51 22.05l-.0918 72l-128-.001c-17.69 0-32.02 14.33-32.02 32v64c0 17.67 14.34 32 32.02 32l128 .001l.0918 71.1c0 9.578 5.707 18.25 14.51 22.05c8.803 3.781 19.03 1.984 26-4.594l144.1-136C514.4 264.4 514.4 247.6 504.8 238.5z"
          />
        </svg>
      </div>
    </router-link>
  </div>

  <SvgBackground />
</template>

<script>
import ArrowComponent from "@/components/BackArrow";
import SvgBackground from "@/components/SvgBackground";

export default {
  name: "HubPage",
  components: {
    ArrowComponent,
    SvgBackground
  },
  data() {
    return {
      user: {},
    };
  },
  beforeCreate() {
    fetch(`${process.env.VUE_APP_CODELAND_BACKEND_HOST}/users/user`, {
      method: "GET",
      credentials: "include",
    })
        .then((res) => res.json())
        .then((user) => {
          this.user = user;
          console.log("user to be seen\n" + JSON.stringify(user));
        });
  },
  methods: {
    backendLogOut() {
      window.location = `${process.env.VUE_APP_CODELAND_BACKEND_HOST}/logout`;
    }

  },
}

</script>

<style scoped>
/* COLORS */


/* ###### */

.content {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.hiuser_router {
  position: absolute;
  top: 4%;
}

.hiuser_router:hover .name {
  color: var(--accent2);
}

.hiuser_router:hover .avatar {
  border: 0.3em solid var(--accent2);
}

.hiuser {
  display: flex;
  justify-content: center;
  align-items: center;
}

.name {
  margin-right: 1vw;
  font-size: 2.5em;
  color: var(--accent1);
}

.button_space {
  display: flex;
  justify-content: space-around;
  align-items: center;
  width: 100vw;
  height: 100vh;
  margin: 0 5em;
}

.block1 {
  width: 70%;
  height: 55vh;
  background: url("@/assets/HubPage/gameplay.jpg") no-repeat center center;
  background-size: cover;
  margin: 5em;
  margin-right: 6em;
  border-radius: 3em;
  border: 0.3em solid black;
  transition: 0.5s;
}

.block1:hover {
  transform: scale(var(--scale-shrink));
}

.sub_block1:hover {
  transform: scale(var(--scale-shrink));
}

.sub_block2:hover {
  transform: scale(var(--scale-shrink));
}

.overlay {
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.4);
  border-radius: 1em;
  font-size: 2em;
  font-weight: bold;
  color: white;
  text-align: center;
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: column;
  transition: 0.5s;
  overflow: hidden;
  position: relative;
}

.overlay:hover {
  background: rgba(0, 0, 0, 0.8);
}

.overlay:hover .block_title1 {
  transform: translateY(0);
}

.overlay:hover .block_title2 {
  transform: translateY(0);
}

.overlay:hover .block_title3 {
  transform: translateY(0);
}

.overlay:hover .init_block_title1 {
  transform: translateY(-20em);
}

.overlay:hover .init_block_title2 {
  transform: translateY(-10em);
}

.overlay:hover .init_block_title3 {
  transform: translateY(-10em);
}


/* Structural */
.block2 {
  width: 100%;
  height: 55vh;
  margin: 5em;
  margin-left: 0;
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-direction: column;

}

.sub_block1 {
  height: 24vh;
  background: url("@/assets/HubPage/scores.jpg") no-repeat center center;
  background-size: cover;
  width: 100%;
  border: 0.3em solid black;
  transition: 0.5s;
  border-radius: 3em;
}

.sub_block2 {
  height: 24vh;
  background: url("@/assets/HubPage/about.jpg") no-repeat center center;
  background-size: cover;
  width: 100%;
  border: 0.3em solid black;
  transition: 0.5s;
  border-radius: 3em;
}

.block_title1 {
  position: absolute;
  transition: 0.5s;
  transform: translateY(20em);
}

.block_title2 {
  position: absolute;
  transition: 0.5s;
  transform: translateY(10em);
}

.block_title3 {
  position: absolute;
  transition: 0.5s;
  transform: translateY(10em);
}

.init_block_title1 {
  position: absolute;
  transition: 0.5s;
}

.init_block_title2 {
  position: absolute;
  transition: 0.5s;
}

.init_block_title3 {
  position: absolute;
  transition: 0.5s;
}

.avatar {
  width: 4em;
  height: 4em;
  border-radius: 50%;
  border: 0.3em solid var(--accent1);
}

.logout_router {
  position: absolute;
  bottom: 6%;
  margin: 0 auto;
}

.logout {
  display: flex;
  justify-content: center;
  align-items: center;
  transition: 0.5s;
}

.logout_message {
  font-size: 3em;
  color: black;
  transition: 0.5s;
  width: 0em;
  white-space: nowrap;
  overflow: hidden;
  margin-right: 0.5em;
}

.logout:hover .logout_message {
  width: 100%;
}

.logout_icon {
  width: 3em;
  height: 3em;
  display: block;
}

</style>
