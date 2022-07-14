/**
 * updateArrows() - updates the arrows to show the correct level
 */
export function updateArrows() {
    this.isPrevActive = this.$refs.splide.splide.index !== 0;
    this.isNextActive =
        this.$refs.splide.splide.index !==
        this.$refs.splide.splide.length -
        this.$refs.splide.splide.options.perPage;
}

/**
 * next_page() - Go to the next page in the worlds
 */
export function next_page() {
    this.$refs.splide.splide.go(">");
    this.updateArrows();
}

/**
 * prev_page() - Go to the previous page in the worlds
 */
export function prev_page() {
    this.$refs.splide.splide.go("<");
    this.updateArrows();
}

/**
 * setSplideStart() - Set the splide to the start of the worlds
 */
export function setSplideStart() {
    this.$refs.splide.splide.go(this.playable_levels.length - 3);
    this.updateArrows();
}


/**
 * setCookie() - Set a cookies with the worlds seen
 */
export function setCookie() {
    this.$cookies.set('devine-codemy_worlds-played', Math.max(parseInt(this.$cookies.get('devine-codemy_worlds-played')), this.worlds[this.currentWorld].worldNumber), -1);
}

/**
 * display_worlds() - Display the worlds
 * @param {} type
 * @param {string} title
 * @param {string} message
 */
