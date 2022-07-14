<template>
  <ArrowComponent :link="'/'" />
  <div class="flexbox">
    <SvgBackground
      :left-g="-bg-contrast3"
      :show-solid-color-b-g="true"
      :show-s-v-g-landscape="true"
    />
    <div id="welcomeText">
      Login with <strong>GitLab</strong>
    </div>
    <button
      id="loginButton"
      type="button"
      @click="backendLoginPage"
    >
      <LoginButton />
    </button>
  </div>
  <SvgBackground
    :show-solid-color-b-g="false"
    :show-s-v-g-landscape="true"
  />
</template>

<script>
// @ is an alias to /src
import LoginButton from "@/assets/LoginPage/Login.svg";
import ArrowComponent from "@/components/BackArrow";
import {backendLoginPage} from "@/js/LoginPage/login.js";
import SvgBackground from "@/components/SvgBackground";


export default {
  name: "LoginPage",

  components: {
    ArrowComponent,
    LoginButton,
    SvgBackground
  },

  methods: {
    backendLoginPage
  },
};
</script>

<style scoped>
.flexbox {
  display: flex;
  flex-direction: column;
  margin-top: 30vh;
  height: 50vw;
  align-items: center;
  align-content: center;
  justify-content: flex-start;

}

#welcomeText {
  font-size: 5vw;
  color: var(--text-contrast)
}

#loginButton {
  background: none;
  border: none;
  position: relative;
  width: 15vw;
  top: 0;
  right: 0;
  margin: 2vw;
  transition: all var(--transition-quick);
}

#loginButton:hover {
  cursor: pointer;
  transform: scale(var(--scale-hover-small));
}

#loginButton:active {
  filter: brightness(0.8);
}

</style>