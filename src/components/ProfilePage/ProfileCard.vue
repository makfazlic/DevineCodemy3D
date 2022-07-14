<template>
  <vue-basic-alert
    ref="alert"
    :duration="300"
    :close-in="5000"
  />
  <transition
    name="slide"
    appear
  >
    <div
      v-if="showPopUp"
      class="popUp"
    >
      <div class="update-box">
        Privacy:
        <select
          name="privacy"
          required
          @change="setPublic($event)"
        >
          <option
            disabled
            selected
            value=""
          >
            Please select
          </option>
          <option value="true">
            Public
          </option>
          <option value="false">
            Private
          </option>
        </select>
      </div>
      <div class="err-box">
        <span id="privacy-err">Please choose your privacy preference</span>
      </div>
      <div class="confirm-box">
        <button @click="validate">
          <em class="fa-solid fa-circle-check confirm-svg" />
        </button>
        <button @click="togglePopUp">
          <em class="fa-solid fa-circle-xmark confirm-svg" />
        </button>
      </div>
    </div>
  </transition>
  <transition
    name="fade"
    appear
  >
    <div
      v-if="showPopUp"
      class="overlay"
      @click="togglePopUp"
    />
  </transition>

  <div class="card-container">
    <div class="under-img">
      <span id="privacy">
        <span v-if="!user.publicProfile">
          <em class="fa-solid fa-user-secret" />
          PRIVATE
        </span>

        <span v-if="user.publicProfile">
          <em class="fa-solid fa-user-large" />
          PUBLIC
        </span>
      </span>
      <button
        v-if="
          $store.state.userId &&
            ($route.query.id === $store.state.userId ||
              !$route.query.id)
        "
        class="button-33"
        role="button"
        @click="togglePopUp"
      >
        EDIT PROFILE
      </button>
    </div>
    <div class="ProPic">
      <img
        alt="Profile pic"
        :src="user.avatarUrl"
      >
    </div>
    <div class="ProfileInfo">
      <span id="name">{{ user.name }}</span>
      <span id="username">Username: <strong>{{ user.username }}</strong></span>
      <span id="mail">
        <span v-if="visible">Mail: {{ user.email }}</span>
        <span v-else>top secret</span>
      </span>
      <span id="desc">
        <span
          v-if="!user.bio"
          style="font-size: 1em"
        > The Bio is empty </span>
        <span
          v-else
          style="font-size: 1em"
        >
          {{ user.bio }}
        </span>
      </span>
    </div>
    <div
      v-if="!visible"
      class="social"
    >
      Social not available
    </div>
    <div
      v-else-if="user.socialMedia"
      class="social"
    >
      <a
        v-if="user.socialMedia.twitter"
        @click="socialRedirect('twitter')"
      >
        <font-awesome-icon 
          class="social-svg" 
          :icon="['fab', 'twitter']" 
        />
      </a>
      <a
        v-if="user.socialMedia.linkedin"
        @click="socialRedirect('linkedin')"
      >
        <font-awesome-icon 
          class="social-svg" 
          :icon="['fab', 'linkedin']" 
        />
      </a>
      <a
        v-if="user.socialMedia.skype"
        @click="socialRedirect('skype')"
      >
        <font-awesome-icon 
          class="social-svg" 
          :icon="['fab', 'skype']" 
        />
      </a>
    </div>
  </div>
</template>

<script>
import VueBasicAlert from "vue-basic-alert";

export default {
  name: "ProfileCard",
  components: {
    VueBasicAlert,
  },
  data() {
    return {
      id: "",
      user: {},
      showPopUp: false,
      publicProfile: Boolean,
      publicProfileInitialized: false,
      visible: true,
    };
  },
  beforeCreate() {
    let id = this.$route.query.id;
    if (id) {
      fetch(`${process.env.VUE_APP_CODELAND_BACKEND_HOST}/users/${id}`, {
        method: "GET",
        credentials: "include",
      })
          .then((res) => res.json())
          .then((user) => {
            this.user = user;
            console.log("user to be seen\n" + JSON.stringify(user));
            if (this.$store.state.userId != id) {
              this.visible = false;
            }
            if (!this.visible) {
              this.$refs.alert.showAlert(
                  "error", // There are 4 types of alert: success, info, warning, error
                  "Private", // Message of the alert
                  "the user is not visible" // Header of the alert
              );
            }
          });
    } else {
      fetch(`${process.env.VUE_APP_CODELAND_BACKEND_HOST}/users/user`, {
        method: "GET",
        credentials: "include",
      })
          .then((res) => res.json())
          .then((user) => {
            this.user = user;
          });
    }
  },
  methods: {
    updateUser() {
      this.$store.dispatch('updateUser', {
        publicProfileInitialized: true,
        publicProfile: this.publicProfile,

      }).then(location.reload());
    },
    validate() {
      if (this.publicProfileInitialized) {
        console.log("////// field public " + this.publicProfile)
        this.updateUser();

      } else {
        document.getElementById("privacy-err").style.display = "block";
      }
    },
    setPublic(event) {
      this.publicProfileInitialized = true;
      this.publicProfile = (event.target.value === "true")
      document.getElementById("privacy-err").style.display = "none";
    },
    togglePopUp() {
      this.publicProfileInitialized = false
      this.showPopUp = !this.showPopUp;
    },
    socialRedirect(social) {
      switch (social) {
        case "twitter":
          window.open("https://www.twitter.com/" + this.user.socialMedia.twitter)
          break
        case "linkedin":
          window.open("https://www.linkedin.com/in/" + this.user.socialMedia.linkedin)
          break
        case "skype":
            navigator.clipboard.writeText(this.user.socialMedia.skype)
            alert('Contact copied to clipboard')
            break;

      }
    }
  },
};

</script>

<style scoped>

/* COLORS */
* {
  color: var(--text);
}

.popUp {
  background: var(--bg-contrast2);
    position: fixed;
    top: 30%;
    left: 40%;
    display: flex;
    flex-direction: column;
    justify-content: space-around;
    height: 30vh;
    width: 20vw;
    border-radius: 25px;
    z-index: 99;
}
.popUp select {
  background: var(--bg-contrast1);
}


.ProPic img {
  border: var(--accent2) solid 2px;
}


/* ###### */

.err-box {
  height: 10%;
  color: #aa0000;
}

.err-box span {
  display: none;
}

.overlay {
  position: absolute;
  top: 0;
  left: 0;
  bottom: 0;
  right: 0;
  z-index: 98;
  background-color: rgba(0, 0, 0, 0.3);
}

.fade-enter-active,
.fade-leave-active {
  transition: all 0.6s;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}

.slide-enter-active,
.slide-leave-active {
  transition: all 0.2s ease;
}

.slide-enter-from,
.slide-leave-to {
  /*transform: translateX(100vw);*/
  opacity: 0;
}

.update-box {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 10px;
  flex-direction: column;
  width: 100%;
  height: 50%;
  font-size: 1.3vw;
}

.update-box > select {
  width: 50%;
  height: 20%;
}

.confirm-box {
  display: flex;
  justify-content: center;
  gap: 10px;
  align-items: center;
  flex-direction: row;
  max-height: 25%;
}

.confirm-svg {
  max-height: 60%;
  height: 4vh;
}

.confirm-box button {
  all: initial;
}

.card-container {
  height: 100%;
  width: 100%;
  padding: 1vh;
  display: grid;
  grid-template-columns: 0.6fr 1.4fr;
  grid-template-rows: 1.5fr 0.7fr;
  gap: 1vh 1vw;
  grid-auto-flow: row;
}

.under-img {
  grid-area: 2 / 1 / 3 / 2;
  display: flex;
  flex-direction: column;
  justify-content: space-evenly;
  align-items: center;
}

.ProPic {
  grid-area: 1 / 1 / 2 / 2;
  display: flex;
  justify-content: center;
  align-items: center;
}

.ProfileInfo {
  max-width: 100%;
  overflow: hidden;
  grid-area: 1 / 2 / 2 / 3;
  position: relative;
  display: flex;
  flex-direction: column;
  align-items: flex-start;
}

.social {
  grid-area: 2 / 2 / 3 / 2;
  display: flex;
  justify-content: space-around;
  align-items: center;
  font-size: 2.5vw;
}

.social-svg {
  height: 9vh;
  transition: all .1s ease-in-out;
}

.social-svg:hover {
  transform: scale(var(--scale-hover-small));
}

#name {
  text-align: left;
  margin-top: 3vh;
  font-size: 3vw;
  font-weight: bold;
}

#username {
  font-size: 1.4vw;
  font-weight: bold;
}

#mail {
  font-size: 1.4vw;
  font-weight: bold;
}

#desc {
  position: relative;
  height: auto;
  bottom: 0;
  padding: 1%;
  font-size: 1.7vh;
  margin: 2% 2% 2% 0;
  border-style: solid;
  border-width: 2px;
  border-radius: 13px;
  text-align: left;
  min-height: 40%;
  min-width: 95%;
    border-color: var(--accent1-contrast);
}

.ProPic > img {
  height: 250px;
  width: 250px;
  border-radius: 50%;
}

#privacy {
  font-size: 2em;
}

#privacy img {
  height: 20px;
}

.button-33 {
  border-radius: 100px;
  cursor: pointer;
  display: inline-block;
  font-family: CerebriSans-Regular, -apple-system, system-ui, Roboto, sans-serif;
  padding: 7px 20px;
  text-align: center;
  text-decoration: none;
  transition: all 250ms;
  border: 0;
  font-size: 2em;
  user-select: none;
  -webkit-user-select: none;
  touch-action: manipulation;
    background: linear-gradient(180deg, var(--accent2-contrast) var(--accent2));
    box-shadow: 0px 3px 9px rgba(96, 95, 95, 0.18);
}

.button-33:hover {
    box-shadow: 0px 3px 9px rgba(96, 95, 95, 0.7);
    transform: scale(1.025);
}
</style>
