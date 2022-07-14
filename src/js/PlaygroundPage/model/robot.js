import {pi_half} from "../../../utils/constants";
import {sleep} from "../../../utils/threads";

/**
 * Represents the robot in the scene,
 * supporting basic animations, movements,
 * emotes.
 */
export class Robot {
    walkingDuration = 0.9583333134651184;
    state = "Idle";
    model;
    mixer = undefined;
    anims = {};
    actions = {};
    api = {};
    mapPos = {};
    moveQuantityDelta = 0.0;
    emitter;
    count = 0;

    previousAction = undefined;
    activeAction = undefined;

    constructor(model, mapPos, emitter) {
        this.model = model;
        this.mapPos.x = mapPos.x;
        this.mapPos.y = mapPos.y;
        this.model.position.x = mapPos.x * 5;
        this.model.position.z = mapPos.y * 5;
        this.model.position.y = 0;
        this.emitter = emitter;
    }

    updatePos() {
        this.mapPos.x = Math.round(this.model.position.x / 5);
        this.mapPos.y = Math.round(this.model.position.z / 5);
    }

    resetPosition(robotDescription) {
        this.model.position.x = robotDescription.posX * 5;
        this.model.position.z = robotDescription.posY * 5;
        if (this.model.rotation.y !== 0) {
            switch (robotDescription.orientation) {
                case "UP":
                    this.model.rotation.y = Math.PI;
                    break;
                case "DOWN":
                    this.model.rotation.y = 0;
                    break;
                case "LEFT":
                    this.model.rotation.y = -pi_half;
                    break;
                case "RIGHT":
                    this.model.rotation.y = pi_half;
                    break;
                default:
                    this.model.rotation.y = 0;
                    break;
            }
        }
    }


    move(state, quantity) {
        this.previousAction = this.activeAction;
        this.activeAction = this.actions["Walking"];

        this.moveQuantityDelta += quantity;

        this.state = state;

        this.mixer.addEventListener("finished", this.updatePos.bind(this));

        this.activeAction
            .reset()
            .setEffectiveTimeScale(1)
            .setEffectiveWeight(1)
            .play();
    }

    doJump = function () {
        this.emitter.emit("playground-robot-jump", this.mapPos);
    };

    animate(dt) {
        if (this.mixer) this.mixer.update(dt);

        let dz = 0;
        if (this.moveQuantityDelta > 0) {
            switch (this.state) {
                case "MoveForward":
                    dz = Math.min((dt * 5) / this.walkingDuration, this.moveQuantityDelta);
                    this.moveQuantityDelta -= dz;
                    this.model.translateZ(dz);
                    break;
                case "RotateLeft":
                    dz = Math.min(
                        (dt * pi_half) / this.walkingDuration,
                        this.moveQuantityDelta
                    );
                    this.moveQuantityDelta -= dz;
                    this.model.rotateY(dz);
                    break;
                case "RotateRight":
                    dz = Math.min(
                        (dt * pi_half) / this.walkingDuration,
                        this.moveQuantityDelta
                    );
                    this.moveQuantityDelta -= dz;
                    this.model.rotateY(-dz);
                    break;
                case this.state.startsWith("TeleportTo") ? this.state : '': {
                    let coordinates = this.state.match(/\d{1,3}/g);
                    dz = Math.min(
                        (dt * pi_half) / this.walkingDuration,
                        this.moveQuantityDelta
                    );
                    this.moveQuantityDelta -= dz;
                    this.model.rotateY(-dz * 300);

                    let scale = 1;
                    if (this.count > this.walkingDuration / 2) {
                        this.model.position.x = coordinates[0] * 5;
                        this.model.position.z = coordinates[1] * 5;

                        scale = this.count * 5 - (this.walkingDuration * 5 - 1);
                        scale = scale < 0 ? 0 : scale;
                        this.model.scale.set(scale, scale, scale)
                    } else {
                        scale = 1 - this.count * 5;
                        scale = scale < 0 ? 0 : scale;
                        this.model.scale.set(scale, scale, scale)
                    }
                    this.count += dt;

                    if (this.count >= this.walkingDuration) {
                        this.count = 0;
                        this.model.scale.set(1, 1, 1)
                    }
                    break;
                }
            }

            this.possiblySignalEventFinished();
        } else {
            this.state = "Idle";
        }
    }

    async possiblySignalEventFinished() {
        if (this.moveQuantityDelta <= 0) {
            sleep(300).then(() =>
                this.emitter.emit("playground-robot-event-finished"));
            this.moveQuantityDelta = 0;
        }
    }
}