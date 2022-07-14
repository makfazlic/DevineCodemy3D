import {sleep} from "../../../utils/threads";


/**
 * This is a simple class that enables
 * tile animations.
 */
export class AnimatedTile {
    type = "";
    model = undefined;
    mixer = undefined;
    actions = {};
    anims = {};
    api = {};
    mapPos = {};
    emitter = undefined;
    activeAtStart = false;

    constructor(model, mapPos, emitter, type, activeAtStart = false) {
        this.model = model;
        this.mapPos.x = mapPos.x;
        this.mapPos.y = mapPos.y;
        this.emitter = emitter;
        this.type = type;
        this.activeAtStart = activeAtStart;
    }


    animate(dt) {
        if (this.mixer) {
            this.mixer.update(dt);
        }
    }

    async possiblySignalEventFinished() {
        sleep(300).then(() =>
            this.emitter.emit("playground-robot-event-finished"));
    }
}