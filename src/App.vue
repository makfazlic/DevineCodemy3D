<template>
  <router-view />
  <BackgroundPlayer />
</template>

<script>
import BackgroundPlayer from '@/components/BackgroundPlayer'
// It checks if the user is logged in or not by calling the backend. If the user is logged in, it will redirect to the HubPage page.
export default {
  components: {
    BackgroundPlayer
  },
  watch: {
    $route: {
      immediate: true,
      handler(to) {
        // Sets the page title based on the  meta attribute 'title' inside the router.
        document.title = to.meta.title || 'Devine Codemy';
        this.$store.dispatch('restoreWorldTheme')
        this.$store.dispatch('checkAuthenticated')
      }
    },
  },
  beforeMount() {
    this.$store.dispatch('checkAuthenticated')
    this.$store.dispatch('restoreWorldTheme')
  }
};
</script>

<!-- eslint-disable vue-scoped-css/enforce-style-type -->
<style>

/* Define global css variables */
:root {
  --transition-quick: 0.1s;
  --transition-slow: 0.3s;
  --scale-hover-small: 1.1;
  --scale-hover-big: 1.2;
  --scale-shrink: 0.92;
}

/*
  COLOR THEMES
*/
body {
  --accent1: #ffb700;
  --accent1-contrast: #e1a300;
  --accent2: #e1f300;
  --accent2-contrast: #b8c900;
}

/* Dark theme */
body, body[world='Inferno'] {
  --bg: #a21e00;
  --bg-contrast: #c42500;
  --bg-contrast1: #e12a00;
  --bg-contrast2: #ff3000;
  --bg-contrast3: #660000;

  --text: #ececec;
  --text-contrast: #f1f1f1;
  --text-contrast1: #ffffff;
  --text-contrast2: #000000;

  --accent1: #ffb700;
  --accent1-contrast: #e1a300;
  --accent2: #e1f300;
  --accent2-contrast: #b8c900;
}

body[world='Purgatory'] {
  --bg: #386200;
  --bg-contrast: #548f00;
  --bg-contrast1: #66ad00;
  --bg-contrast2: #76c900;
  --bg-contrast3: #003300;

  --text: #ececec;
  --text-contrast: #ffea00;
  --text-contrast1: #ffffff;
  --text-contrast2: #f1f1f1;

  --accent1: #02fc34;
  --accent1-contrast: #00ab23;
  --accent2: #5100ff;
  --accent2-contrast: #3600ad;
}

body[world='Paradise'] {
  --bg: #FEFCFA;
  --bg-contrast: #ffeed7;
  --bg-contrast1: #ffeed7;
  --bg-contrast2: #ffeed7;
  --bg-contrast3: #64C8FF;

  --text: #e3bc43;
  --text-contrast: #b08c1d;
  --text-contrast1: #987814;

  --accent1: #5674C4;
  --accent1-contrast: #385fc5;
  --accent2: #E3BECC;
  --accent2-contrast: #dc97b1;
}

html, body, #app {
  margin: 0;
  height: 100%;
  width: 100%;
}

/*
    FONTS
 */
@font-face {
  font-family: "Nove";
  src: local("Nove"), url(assets/fonts/Nove.ttf) format("truetype");
}

@font-face {
  font-family: "Tommy";
  src: local("Tommy"), url(assets/fonts/Tommy.otf) format("opentype");
}

* {
  font-family: "Tommy", sans-serif;
  color: var(--text);
  margin: 0;
  padding: 0;
}

h1, h2, h3, h4, h5, h6, tspan {
  font-family: "Nove", sans-serif;
}

em, strong {
  color: var(--text-contrast1);
}

a {
  text-decoration: none;
}

</style>
<!--eslint-enable-->