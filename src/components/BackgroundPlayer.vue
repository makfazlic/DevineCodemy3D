<template>
  <button
    id="audiotoggle"
    @click="toggleAudio"
  >
    <font-awesome-icon
      v-if="isPlaying"
      id="togglesvg"
      icon="volume-high"
    />
    <font-awesome-icon
      v-else
      id="togglesvg"
      icon="volume-mute"
    />
  </button>
</template>

<script>
import useSound from 'vue-use-sound'

const infernoSfxSrc = require("../assets/sfx/Inferno.mp3");
const purgatorySfxSrc = require("../assets/sfx/Purgatory.mp3")
const paradiseSfxSrc = require("../assets/sfx/Paradise.mp3")

export default {
  name: 'BackgroundPlayer',
  props: {
    // Whether to show the button to pause the background music or not
    showMuteButton: {
      type: Boolean,
      required: false,
      default: true
    }
  },
  setup() {
    const delegated = {
      interrupt: false,
      loop: true,
      autostart: false
    }

    const [playInferno, infernoAudio] = useSound(infernoSfxSrc, delegated)

    const [playPurgatory, purgatoryAudio] = useSound(purgatorySfxSrc, delegated)

    const [playParadise, paradiseAudio] = useSound(paradiseSfxSrc, delegated)

    console.log("[+] Imported SFX")

    return {
      currentTrack: {name: "Inferno", play: playInferno, audio: infernoAudio},
      tracks: [{name: "Inferno", play: playInferno, audio: infernoAudio},
        {name: "Purgatory", play: playPurgatory, audio: purgatoryAudio},
        {name: "Paradise", play: playParadise, audio: paradiseAudio}],
    }
  },
  data() {
    return {
      observer: null,
      isPlaying: false
    }
  },
  beforeMount() {
    let hasInteracted = false
    // Waits to autoplay the track until the user has interacted with the DOM once
    document.body.addEventListener('click', () => {
      if (!hasInteracted) {
        this.playCurrentTrack()
        hasInteracted = true;
      }
    })

    this.playAudioForWorld()
    // Listens on the body for world changes
    this.observer = new MutationObserver((mutationList) => {
      if (mutationList.some((mutation) => mutation.attributeName === "world")) {
        this.playAudioForWorld()
      }
    })
    this.observer.observe(document.body, {attributes: true})
  },

  beforeUnmount() {
    this.observer.disconnect()
  },
  methods: {
    playCurrentTrack() {
      this.currentTrack.play()
      this.isPlaying = true
    },
    pauseCurrentTrack() {
      this.currentTrack.audio.pause()
      this.isPlaying = false
    },
    playAudioForWorld() {
      const world = document.body.getAttribute('world') || "Inferno"
      console.log(`[+] Loading audio for world '${world}'`)
      // Returns if we already loaded the audio for this world
      if (this.currentTrack.name == world) {
        return
      }

      this.tracks.forEach((track) => {
        // Plays the track if it matches the world
        if (track.name == world) {
          this.currentTrack = track
          console.log("[+] New audio: " + track.name)
          if (this.isPlaying)
            track.play()
          // Stops track if it's the track for another world
        } else if (track.name != world) {
          track.audio.stop()
        }
      })
    },
    toggleAudio() {
      if (this.isPlaying) {
        this.pauseCurrentTrack()
      } else {
        this.playCurrentTrack()
      }
    }
  }
}
</script>

<style scoped>
/* COLORS */

#audiotoggle {
  background: var(--accent1-contrast);
  border-radius: 10px;
  border: none;
  cursor: pointer;
  transition: var(--transition-quick);
    position: absolute;
    padding: 0.7vh;
    right: 2vh;
    top: 2vh;
}

#audiotoggle:hover {
  transform: scale(var(--scale-hover-small));
}

/* ###### */

#togglesvg {
  width: 5vh;
  height: 5vh;
  z-index: 0;
}
</style>