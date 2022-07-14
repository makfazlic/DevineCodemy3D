<template>
  <router-link
    :to="redirectPath"
    class="button_container"
    :style="`--hubButtonGradientColor: ${gradientColor}`"
  >
    <h1>{{ label }}</h1>
  </router-link>
</template>

<script>
export default {
  name: "HubButton",
  props: {
    label: {
      type: String,
      default: "/",
    },
    redirectPath: {
      type: String,
      default: "/",
    },
    bgImagePath: {
      type: String,
      default: "/",
    },
    gradientColor: {
      type: String,
      default: "/",
    },
  },
}
</script>

/* COLORS */
<style scoped>
.button_container {
  transition: all var(--transition-quick) ease-in-out;
  border-color: var(--hubButtonGradientColor);
  background: linear-gradient(0deg, var(--hubButtonGradientColor) 0%, #131513 40%);
    display: flex;
    align-items: center;
    justify-content: center;
    overflow: hidden;

    border-radius: 25px;
    border-style: solid;

}

.button_container:hover {
  background: linear-gradient(0deg, var(--hubButtonGradientColor) 0%, #131513 80%);
  font-size: 2.5vh;
    cursor: pointer;
    transform: scale(var(--scale-hover-small));
}

h1 {
  color: white;
    font-size: 4em;
}

</style>