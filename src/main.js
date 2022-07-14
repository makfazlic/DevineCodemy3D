import {createApp} from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import VueCookies from 'vue-cookies'
import VueSplide from '@splidejs/vue-splide';

// FontAwesome setup
import {library} from "@fortawesome/fontawesome-svg-core";
import {faTwitter, faLinkedin, faSkype} from "@fortawesome/free-brands-svg-icons";
import {faVolumeHigh, faVolumeMute} from "@fortawesome/free-solid-svg-icons"

library.add(faLinkedin, faTwitter, faSkype, faVolumeHigh, faVolumeMute);
import {FontAwesomeIcon} from '@fortawesome/vue-fontawesome'


createApp(App).use(store).use(router).use(VueCookies).use(VueSplide).component('font-awesome-icon', FontAwesomeIcon).mount('#app')
