import * as THREE from "three";
import {sleep} from "@/utils/threads";
import {Collectable} from "../model/collectable";
import {loadPlatformModels, loadRobotAssets} from "./3dModelLoader";
import {Robot} from "../model/robot";
import {AnimatedTile} from "@/js/PlaygroundPage/model/animatedTile";
import {pi_half} from "@/utils/constants";
import {randomBoolean, randomInteger} from "@/utils/random";
import {AnimatedObject} from "@/js/PlaygroundPage/model/animatedObject";
import {randFloat} from "three/src/math/MathUtils";

export class LevelScene {
    scene = undefined;
    renderer = undefined;
    camera = undefined;
    robot = undefined;
    actors = [];
    clock = undefined;
    eventEmitter = undefined;
    levelDescription = undefined;

    constructor(scene, renderer, camera, eventEmitter, levelDescription) {
        this.scene = scene;
        this.renderer = renderer;
        this.camera = camera;
        this.clock = new THREE.Clock();
        this.eventEmitter = eventEmitter;
        this._setupEventDispatch();
        this.levelDescription = levelDescription;
    }

    render() {
        this.renderer.render(this.scene, this.camera);
    }

    animate() {
        const dt = this.clock.getDelta();
        this.actors.forEach((actor) => {
            actor.animate?.(dt);
        });
        requestAnimationFrame(this.animate.bind(this));
        this.render();
    }

    findActor(predicate) {
        return this.actors.find(predicate);
    }

    removeActor(collectable) {
        this.actors = this.actors.filter((actor) => actor !== collectable);
        collectable.model.removeFromParent();
    }

    _setupEventDispatch() {

        this.eventEmitter.on("playground-robot-event", (event) => {
            switch (event.type) {
                case "MoveForward":
                    this.robot.move("MoveForward", 5);
                    break;
                case "TurnLeft":
                    this.robot.move("RotateLeft", pi_half);
                    break;
                case "TurnRight":
                    this.robot.move("RotateRight", pi_half);
                    break;
                case "Jump":
                    this.robot.api["Jump"]();
                    break;
                case "EmoteNo":
                    this.robot.api["No"]();
                    break;
                case "EmoteThumbsUp":
                    this.robot.api["ThumbsUp"]();
                    break;
                case "EmoteWave":
                    this.robot.api["Wave"]();
                    break;
                case "EmoteDance":
                    this.robot.api["Dance"]();
                    break;
                case "EmoteDeath":
                    this.robot.api["Death"]();
                    break;
                case "Idle":
                    this.robot.api["Idle"]();
                    break;
                case event.type.startsWith("ToggleTeleport") ? event.type : '': {
                    let teleportCoordinates = event.type.match(/\d{1,3}/g);
                    let teleportToToggle = this.actors.find(actor => actor.mapPos.x == teleportCoordinates[0] &&
                        actor.mapPos.y == teleportCoordinates[1]);
                    teleportToToggle?.api["teleporting"]()
                }
                    break;
                case event.type.startsWith("TeleportTo") ? event.type : '':
                    this.robot.move(event.type, pi_half);
                    break;
                case event.type.startsWith("AnimateAt") ? event.type : '': {
                    let deactiveTeleportCoordinates = event.type.match(/\d{1,3}/g);

                    // activate the teleport at the given coordinate
                    let teleportToActivate = this.actors.find(actor => actor.mapPos.x == deactiveTeleportCoordinates[0] &&
                        actor.mapPos.y == deactiveTeleportCoordinates[1]);

                    let materialUnactive = teleportToActivate.model.children[0].material.clone()
                    materialUnactive.emissive = new THREE.Color(0x00ffff);
                    teleportToActivate.model.children[0].material = materialUnactive;
                    teleportToActivate.possiblySignalEventFinished();
                    break;
                }
                case event.type.startsWith("DeanimateAt") ? event.type : '': {
                    let activeTeleportCoordinates = event.type.match(/\d{1,3}/g);

                    // deactivate teleport at the given coordinate
                    let teleportToDeactivate = this.actors.find(actor => actor.mapPos.x == activeTeleportCoordinates[0] &&
                        actor.mapPos.y == activeTeleportCoordinates[1]);

                    let materialActive = teleportToDeactivate.model.children[0].material.clone()
                    materialActive.emissive = new THREE.Color(0x303030);
                    teleportToDeactivate.model.children[0].material = materialActive;
                    teleportToDeactivate.possiblySignalEventFinished();
                    break;
                }
                case event.type.startsWith("ActivateLever") ? event.type : '': {
                    let leverCoordinates = event.type.match(/\d{1,3}/g);
                    let lever = this.actors.find(actor => actor.mapPos.x == leverCoordinates[0] &&
                        actor.mapPos.y == leverCoordinates[1]);
                    lever?.api["leverActive"]();
                    break;
                }
                case event.type.startsWith("DeactivateLever") ? event.type : '': {
                    let leverDeactiveCoordinates = event.type.match(/\d{1,3}/g);
                    let leverDeactive = this.actors.find(actor => actor.mapPos.x == leverDeactiveCoordinates[0] &&
                        actor.mapPos.y == leverDeactiveCoordinates[1]);
                    leverDeactive?.api["leverDeactive"]();
                    break;
                }
                default:
                    break;
            }
        });
    }

    _initAnimatedModel(actor, model, animations, emotes, states, clampWhenFinished = false) {
        this.scene.add(model);

        actor.mixer = new THREE.AnimationMixer(model);
        actor.model = model;
        for (let animation of animations) {
            const clip = animation;
            actor.anims[clip.name] = clip;
            const action = actor.mixer.clipAction(clip);
            if (clampWhenFinished) {
                action.clampWhenFinished = true;
            }
            actor.actions[clip.name] = action;
            action.loop = THREE.LoopOnce;
        }

        const restoreState = async () => {
            await sleep(500);
            this.eventEmitter.emit("playground-robot-event-finished");
            actor.mixer.removeEventListener("finished", restoreState);
        };

        const createEmoteCallback = (actorObj, name) => {
            actorObj.api[name] = async () => {
                fadeToAction(name, 0.2);
                actorObj[`do${name}`]?.();
                actorObj.mixer.addEventListener("finished", restoreState);
            };
        };

        for (const emote of emotes) {
            createEmoteCallback(actor, emote);
        }

        for (const emote of states) {
            createEmoteCallback(actor, emote);
        }

        function fadeToAction(name, duration) {
            actor.previousAction = actor.activeAction;
            actor.activeAction = actor.actions[name];

            if (actor.previousAction !== actor.activeAction) {
                actor.previousAction?.fadeOut(duration);
            }

            actor?.activeAction?.reset()
                .setEffectiveTimeScale(1)
                .setEffectiveWeight(1)
                .fadeIn(duration)
                .play();
        }
    }

    _addBlocks() {

        const teleportColors = [
            new THREE.Color(0x00ffff),
            new THREE.Color(0x00ff00),
            new THREE.Color(0xffff00),
            new THREE.Color(0xff0000),
            new THREE.Color(0x0000ff),
            new THREE.Color(0xffffff),
            new THREE.Color(0x000000),
        ];

        // an array with all the teleports and their colors
        let teleportMap = [];

        for (const tile of this.levelDescription.board.grid) {
            const type = tile.type;
            const obj = this.models.blocks[type].clone();
            obj.position.x = tile.posX * 5;
            obj.position.z = tile.posY * 5;
            obj.position.y = tile.posZ * 5 - 2.5;

            if (tile.type === "TELEPORT") {
                // set active color
                if (tile.active === true) {
                    let material = obj.children[0].material.clone()
                    material.emissive = new THREE.Color(0x00ffff);
                    obj.children[0].material = material
                } else {
                    let material = obj.children[0].material.clone()
                    material.emissive = new THREE.Color(0x303030);
                    obj.children[0].material = material;
                }

                // set parent color
                const found = teleportMap.find(t => t.x === tile.targetX && t.y === tile.targetY);
                if (found) { // assign the same color of the parent
                    let material = obj.children[4].material.clone()
                    material.color = found.color;
                    obj.children[4].material = material;
                } else { // assign a new color and add to map
                    let material = obj.children[4].material.clone()
                    material.color = teleportColors[teleportMap.length];
                    obj.children[4].material = material;
                    teleportMap.push({x: tile.posX, y: tile.posY, color: material.color});
                }
            }

            this.scene.add(obj);

            if (tile.type === "GRASS") {
                this._addGrassDecorations(tile);
            }
            if (tile.type === "LEVER" || tile.type === "TELEPORT") {
                const animatedTile = new AnimatedTile(
                    obj,
                    {x: tile.posX, y: tile.posY},
                    this.eventEmitter,
                    tile.type,
                    tile.active
                );

                let emotes = tile.type === "LEVER" ? ["leverActive", "leverDeactive"] : ["teleporting"];
                this._initAnimatedModel(animatedTile, obj, this.models.animations, emotes, [], true);

                this.actors.push(animatedTile);
            }
        }
    }

    /**
     * This method adds grass decorations to a
     * grass tile.
     * @param  tile
     */
    _addGrassDecorations(tile) {
        let i = 0;
        let decorationNumber = 0;

        while (i < 4 && decorationNumber < 2) {
            i += 1;
            let shouldDecorate = randomBoolean();
            if (shouldDecorate) {
                decorationNumber += 1;
                let random_index = randomInteger(this.models.grass_decorations.length);
                let decoration = this.models.grass_decorations[random_index];
                decoration = decoration.clone();
                let bbox = new THREE.Box3().setFromObject(decoration);

                decoration.position.x =
                    tile.posX * 5 + (i % 2 === 0 ? -1 : 1) * Math.random() * 1.25;
                decoration.position.z =
                    tile.posY * 5 + (i % 2 === 1 ? -1 : 1) * Math.random() * 1.25;
                decoration.position.y = decoration.position.y - bbox.min.y - 0.25;

                this.scene.add(decoration);
            }
        }
    }

    _addCollectables() {
        for (const collectable of this.levelDescription.board.items) {
            const elem = this.models.collectables[collectable.type].clone();
            const coin = new Collectable(
                elem,
                {x: collectable.posX, y: collectable.posY},
                this.eventEmitter,
                collectable.type
            );
            this._initAnimatedModel(coin, elem, [], [], []);
            this.actors.push(coin);
        }
    }

    _addDecorations() {
        let allCloudDecorations = ["cloud1", "cloud2", "cloud3"]
        let decorations = [];

        for (let i = 0; i < (randomInteger(this.levelDescription.board.dimX) + 3); i++) {
            decorations.push({
                x: randomInteger(this.levelDescription.board.dimX + 4) - 2,
                y: randomInteger(this.levelDescription.board.dimY + 4) - 2,
                z: randomInteger(10) + 5,
                type: allCloudDecorations[randomInteger(allCloudDecorations.length)]
            });
        }

        for (const decoration of decorations) {
            let type = decoration.type;
            let decorationModel = this.models.decorations[type].clone();
            decorationModel.position.x = decoration.x * 5;
            decorationModel.position.z = decoration.y * 5;
            decorationModel.position.y = decoration.z * 3;
            decorationModel.rotation.z = randFloat(0, 2 * Math.PI);

            if (type.includes("cloud")) {
                this.actors.push(new AnimatedObject(decorationModel));
            }

            this.scene.add(decorationModel);
        }
    }

    _addLights() {
        const hemiLight = new THREE.HemisphereLight(0xffffff, 0x444444);
        hemiLight.position.set(0, 20, 0);
        this.scene.add(hemiLight);

        const dirLight = new THREE.DirectionalLight(0xffffff);
        dirLight.position.set(0, 20, 10);
        this.scene.add(dirLight);
    }

    async _addRobot() {
        const {model, animations} = await loadRobotAssets(this.levelDescription.robot.orientation);
        const robotDescription = this.levelDescription.robot;
        this.robot = new Robot(model, {x: robotDescription.posX, y: robotDescription.posY}, this.eventEmitter);

        this.actors.push(this.robot);

        const states = [
            "Idle",
            "Walking",
            "Running",
            "Dance",
            "Death",
            "Sitting",
            "Standing",
        ];

        const emotes = ["Jump", "Yes", "No", "Wave", "Punch", "ThumbsUp"];

        this._initAnimatedModel(this.robot, model, animations, emotes, states);

        this.robot.activeAction = this.robot.actions["Idle"];
        this.robot.activeAction.play();
    }

    async composeScene() {
        this.models = await loadPlatformModels();
        this._addBlocks();
        this._addCollectables();
        this._addDecorations();
        this._addLights();
        await this._addRobot();
    }

    reset() {
        this.robot.resetPosition(this.levelDescription.robot);
        this.actors.filter(actor => actor.type === "LEVER").forEach(lever => lever.mixer.stopAllAction());
        // reset all teleport active colors
        this.actors.filter(actor => actor.type === "TELEPORT").forEach(teleport => {
            if (teleport.activeAtStart === true) {
                let material = teleport.model.children[0].material.clone()
                material.emissive = new THREE.Color(0x00ffff);
                teleport.model.children[0].material = material
            } else {
                let material = teleport.model.children[0].material.clone()
                material.emissive = new THREE.Color(0x303030);
                teleport.model.children[0].material = material;
            }
        });
        const collectableActors = this.actors.filter(actor => actor.collectableType !== undefined);
        collectableActors.forEach((actor) => this.removeActor(actor));
        this._addCollectables();
    }
}
