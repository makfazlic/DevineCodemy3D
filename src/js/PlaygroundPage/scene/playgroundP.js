import {LevelMap} from "@/js/PlaygroundPage/model/levelMap";
import {LevelScene} from "@/js/PlaygroundPage/scene/levelScene";
import events from "events";

import {
    // AmbientLight,
    Camera,
    // GltfModel,
    Renderer,
    Scene,
} from "troisjs";

export default {
    name: "PlaygroundP",
    components: {
        Camera,
        Renderer,
        Scene,
    },
    props: {
        levelDescription: {
            type: Object,
            required: true,
        },
    },

    data() {
        return {
            collected: {
                coin: 0,
                gem_green: 0,
                gem_pink: 0,
            },
            mute: true,
            coinsToCollect: 0,
        };
    },
    methods: {
        computeCameraX() {
            return this.levelDescription.robot.posX * 5 + 30;
        },
        computeCameraY() {
            return 10;
        },
        computeCameraZ() {
            return this.levelDescription.robot.posY * 5 + 10;
        },


        collectablesState() {
            return this.levelMap.getCollectablesState();
        },

        collectableCount() {
            return this.levelMap.getCollectablesState().length;
        },

        updateCollectableCount() {
            this.coinsToCollect = this.collectableCount();
        },

        reset() {
            this.levelScene.reset();
            this.levelMap.reset();
            this.collected = {
                coin: 0,
                gem_green: 0,
                gem_pink: 0,
            };
        },

        async performAction(robotAction, stopped) {
            if (stopped) {
                throw new Error("Stopped");
            }
            this.eventEmitter.emit("playground-robot-event", robotAction);
            return events.once(this.eventEmitter, "playground-robot-event-finished");
        },
    },

    async mounted() {
        this.eventEmitter = new events.EventEmitter();

        this.levelMap = new LevelMap(this.levelDescription);
        this.updateCollectableCount();


        this.levelScene = new LevelScene(
            this.$refs.scene,
            this.$refs.renderer,
            this.$refs.camera,
            this.eventEmitter,
            this.levelDescription
        );

        this.eventEmitter.on("playground-robot-jump", (robotPos) => {
            const hasCollectable = this.levelMap.hasAnyCollectableAt(robotPos);

            if (hasCollectable) {
                const collectable = this.levelScene.findActor(
                    (actor) =>
                        actor.mapPos?.x === robotPos.x &&
                        actor.mapPos?.y === robotPos.y &&
                        actor.collectableType !== undefined
                );
                this.collected[collectable.collectableType] += 1;
                collectable.collectAnimation();
            }
        });

        this.eventEmitter.on("playground-collected-item", (collectable) => {
            this.levelScene.removeActor(collectable);
            this.levelMap.collectAt(collectable.mapPos);
            this.updateCollectableCount();
        });

        await this.levelScene.composeScene();
        /**
         * After the scene is complete,
         */
        this.$emit("playground-ready");
        this.levelScene.animate();
    },
}