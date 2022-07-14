/**
 * This represents a collectable actor
 * in the 3d scene.
 */
export class Collectable {
    model = undefined;
    mixer = undefined;
    anims = {};
    actions = {};
    api = {};
    mapPos = {};
    collectableType = undefined;
    emitter = undefined;

    previousAction = undefined;
    activeAction = undefined;

    collectionDuration = 0.125;
    collectionSpeed = 12;
    collectingAnimation = false;
    collectingAnimationState = 0;

    constructor(model, mapPos, emitter, collectableType) {
        this.model = model;
        this.mapPos.x = mapPos.x;
        this.mapPos.y = mapPos.y;
        this.model.scale.set(1.8, 1.8, 1.8);
        this.model.position.x = mapPos.x * 5;
        this.model.position.z = mapPos.y * 5;
        this.model.position.y = 5.75;
        this.emitter = emitter;
        this.collectableType = collectableType;
    }

    animate(dt) {
        this.mixer.update(dt);
        this.model.rotateZ(0.02);
        if (this.collectingAnimation) {
            this.collectingAnimationState += dt;
            this.model.translateZ((this.collectingAnimationState < this.collectionDuration / 2 ? 1 : -1) * this.collectionSpeed * dt);
        }
        if (this.collectingAnimationState > this.collectionDuration) {
            this.collectingAnimation = false;
            this.collectingAnimationState = 0;
            // At the end of animation, emit the collected event so
            // that the state of the playground can be changed accordingly.
            this.emitter.emit("playground-collected-item", this);
        }
    }

    collectAnimation() {
        this.collectingAnimation = true;
    }
}