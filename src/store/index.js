import { createStore } from "vuex";
import Cookies from 'js-cookie'

export default createStore({
  state: {
    userId: "",
  },
  getters: {
    getUserId(state) {
      return state.userId;
    },
  },
  mutations: {
    setId(state, id) {
      state.userId = id;
    },
  },
  actions: {
    setUserId(context, id) {
      return context.commit("setId", id);
    },
    /*
     * fetch PUT to the server to update the user
     * */
    updateUser(context, updateInfo) {
      fetch(`${process.env.VUE_APP_CODELAND_BACKEND_HOST}/users/${context.getters.getUserId}`,
        {
          method: "PUT",
          credentials: "include",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify(updateInfo),
        }
      ).then((res) => console.log(res.json()));
    },
    setWorldTheme(context, world_name) {
      document.body.setAttribute("world", world_name)
      // Sets the cookie of the last seen world so that the theme gets restored when navigating through pages
      Cookies.set('last-world-seen', world_name)
    },
    restoreWorldTheme() {
      document.body.setAttribute("world", Cookies.get('last-world-seen') || "Inferno")
    },
    async checkAuthenticated() {
      if (
          window.location.pathname !== "/" &&
          window.location.pathname !== "/login"
      ) {
        // Calling the backend to check if the user is logged in or not.
        await fetch(`${process.env.VUE_APP_CODELAND_BACKEND_HOST}/auth/check`,
            {
              method: "GET",
              credentials: "include"
            })
            .then((response) => {
              if (response.status >= 300 || response.status < 200) {
                window.location = "/login";
              }
              return response.json();
            })
            .then((res) => {
              if (!this.state.userId) {
                this.dispatch("setUserId", res.id);
              }
            })
            .catch(                                 // If there is an error and the error is a 401 it will redirect to the login page.
                (error) => {
                  if (error.response.status === 401) {
                    window.location = "/login";
                  }
                }
            )

      }
    },

  },
  modules: {},
});
