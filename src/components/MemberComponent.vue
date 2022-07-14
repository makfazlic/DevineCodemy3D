<template>
  <button
    class="card"
    @click="docFunction(name)"
  >
    <div class="image">
      <img 
        :src="image" 
        alt="member_image"
      >
    </div>
    <h1>{{ name }}</h1>
    <h2>{{ role }}</h2>
  </button>
</template>

<script>

export default {
  name: "MemberComponent",
  props: {
    name: {
      type: String,
      default: "Team Member",
    },
    role: {
      type: String,
      default: "Team Role",
    },
  },
  data() {
    switch (this.name) {
      case 'Alan Copa':
        return {
          image: "https://gitlab.com/uploads/-/system/user/avatar/11025344/avatar.png?width=400",
        }
      case 'Diell Kryeziu':
        return {
          image: "https://secure.gravatar.com/avatar/bb1051e54777429d278a55aa8aed22db?s=800&d=identicon",
        }
      case 'Albert Cefreda':
        return {
          image: "https://secure.gravatar.com/avatar/616aeede1697fff3fd538fd7ff45993e?s=800&d=identicon",
        }
      case 'Alen Sugimoto':
        return {
          image: "https://secure.gravatar.com/avatar/4a6ba75c35d813a6389bd37427eb9646?s=800&d=identicon",
        }
      case 'Alfio Vavassori':
        return {
          image: "https://secure.gravatar.com/avatar/9b4dd9a75dd265b9a1e9df88f0c36cf6?s=800&d=identicon",
        }
      case 'Alessandro Cagnani':
        return {
          image: "https://secure.gravatar.com/avatar/49afaa542581db52a53157b1acd64959?s=800&d=identicon",
        }
      case 'Roberto Palmieri':
        return {
          image: "https://gitlab.com/uploads/-/system/user/avatar/11006483/avatar.png?width=400",
        }
      case 'Bojan Lazarevski':
        return {
          image: "https://secure.gravatar.com/avatar/f6b96f31513412e03b6fcb92a552953f?s=800&d=identicon",
        }
      case 'Daniela Gjorgjieva':
        return {
          image: "https://secure.gravatar.com/avatar/e2593af8bd17989052e08abbd3d268ef?s=800&d=identicon",
        }
      case 'Enrico Benedettini':
        return {
          image: "https://secure.gravatar.com/avatar/971ca4235f451f82defcbedad5f4e921?s=800&d=identicon",
        }
      case 'Harkeerat Singh Sawhney':
        return {
          image: "https://gitlab.com/uploads/-/system/user/avatar/11006493/avatar.png?width=400",
        }
      case 'Mak Fazlic':
        return {
          image: "https://secure.gravatar.com/avatar/65a19ac5c4eec4d900a5de10eb8706c2?s=800&d=identicon",
        }
      case 'Cindy Guerrero Toro':
        return {
          image: "https://gitlab.com/uploads/-/system/user/avatar/11041742/avatar.png?width=400",
        }
      case 'Alessandro Gobbetti':
        return {
          image: "https://gitlab.com/uploads/-/system/user/avatar/11006698/avatar.png?width=400",
        }
    }
  },
  methods: {
    docFunction: function () {
      switch (this.name) {
        case 'Alan Copa':
          window.open("https://gitlab.com/AlanCopa");
          break;
        case 'Diell Kryeziu':
          window.open("https://gitlab.com/Diellk");
            break;
        case 'Albert Cefreda':
          window.open("https://gitlab.com/AlbertCerfeda");
            break;
        case 'Alen Sugimoto':
          window.open("https://gitlab.com/alensugimoto");
            break;
        case 'Alfio Vavassori':
          window.open("https://gitlab.com/alfiovavassori");
            break;
        case 'Alessandro Cagnani':
          window.open("https://gitlab.com/Squeakink");
            break;
        case 'Roberto Palmieri':
          window.open("https://gitlab.com/robypalmy");
            break;
        case 'Bojan Lazarevski':
          window.open("https://gitlab.com/BojanUSI");
            break;
        case 'Daniela Gjorgjieva':
          window.open("https://gitlab.com/DanielaGj");
            break;
        case 'Enrico Benedettini':
          window.open("https://gitlab.com/Enrico-Benedettini");
            break;
        case 'Harkeerat Singh Sawhney':
          window.open("https://gitlab.com/HarkeeratS");
            break;
        case 'Mak Fazlic':
          window.open("https://gitlab.com/makfazlic");
            break;
        case 'Cindy Guerrero Toro':
          window.open("https://gitlab.com/cguerreroto");
            break;
        case 'Alessandro Gobbetti':
          window.open("https://gitlab.com/alessandro.gobbetti");
            break;
      }
    }
  }
}
</script>

<style scoped>

.card {
  transition: all .1s ease-in-out;
  height: 100%;
  width: 100%;
  min-width: 1vw;
  min-height: 1vh;
  /*max-height: 100vh;*/
  /*max-width: 100vw;*/
  display: grid;
  grid-template-columns: 1fr;
  grid-template-rows: 47% 32% 30%;
  grid-template-areas:
    "img"
    "name"
    "role"
}

button {
  transition: all .1s ease-in-out;
  background-color: Transparent;
  border-color: Transparent;
}

.card:hover {
  transform: scale(1.2);
}

.image {
  justify-content: center;
  align-items: center;
  display: flex;
}

img {
  grid-area: img;
  max-width: 10vw;
  min-height: 1vh;
}

h1 {
  grid-area: name;
  text-align: center;
  font-size: 2vw;
}

h2 {
  grid-area: role;
  text-align: center;
  font-size: 1.9vw;
}

h1, h2 {
  color: orange;
}

</style>