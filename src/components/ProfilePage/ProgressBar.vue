<!--

FOR THE MOMENT USER HAS TO BE PUBLIC TO BE SHOWN,
TODO: add fields in the backend to support the page
    - make the UserDTO and User model to contain also the levelCompleated int
-->


<template>
  <div class="bar">
    <div
      class="progress shadow"
      :style="{width: `${perc}%`}"
    />
  </div>
</template>

<script>
export default {
    name: "ProgressBar",
    props: {
        perc: {
            type: Number,
            required: true
        }
    }

    // mounted() {
		//         // if/when uncommenting this, please don't use localhost:8080 but the backend env variable :)
    //         fetch('http://localhost:8080/users/' + "6251b4ba74938f3b58cbd212")
    //             .then(res => res.json())
    //             .then(user => {
    //                 // console.log("//// progress bar", user)
    //                 // let progress = document.getElementsByClassName("progress");
    //                 // console.log(progress)
    //                 let progress = (user.completedLevels * 100)/30;
    //                 this.width = {width: `${progress}%`}
    //             })
    //
    // }
}
</script>

<style scoped>

.bar {
    height: 30%;
    width: 90%;
    display: flex;
    align-items: center;
    border-radius: 75px;
    border: #2c3e50 solid 2px;
}

.progress {
    height: 100%;
    background-color: #2c3e50;
    border-radius: 75px;

}

.shadow {
    /* 25 50 */
    box-shadow: 0px 45px 50px rgba(0, 0, 0, 0.25);
}

</style>