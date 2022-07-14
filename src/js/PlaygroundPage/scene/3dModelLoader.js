import * as THREE from "three";
import {GLTFLoader} from "three/examples/jsm/loaders/GLTFLoader.js";
import {pi_half} from "@/utils/constants";

const dir = new THREE.Vector3(-1, 0, 0).normalize();

function setProperties(model, scale = 2.4, rotate = dir) {
    model.scale.set(scale, scale, scale);
    if (rotate) {
        model.rotateOnWorldAxis(rotate, pi_half);
    }
}

export async function loadPlatformModels() {
    const loader = new GLTFLoader();
    const blocks = {};
    const collectables = {};
    const decorations = {};
    const grass_decorations = [];

    const gltf = await loader.loadAsync("assets/models/world/scene2.gltf");
    blocks["GRASS"] = gltf.scene.getObjectByName("Cube_Grass_Single000");
    blocks["WATER"] = gltf.scene.getObjectByName("water_low");
    blocks["CONCRETE"] = gltf.scene.getObjectByName("concrete_cube");
    blocks["SAND"] = gltf.scene.getObjectByName("dirt_cube");
    blocks["BRIDGE"] = gltf.scene.getObjectByName("bridge");
    blocks["CLOUDG"] = gltf.scene.getObjectByName("cloud_light");
    blocks["CLOUDB"] = gltf.scene.getObjectByName("cloud_dark");
    blocks["LAVA"] = gltf.scene.getObjectByName("lava_low");
    blocks["OBSIDIAN"] = gltf.scene.getObjectByName("obsidian");
    blocks["TELEPORT"] = gltf.scene.getObjectByName("teleport_cube");
    blocks["LEVER"] = gltf.scene.getObjectByName("lever");

    const animations = gltf.animations

    for (const obj of Object.values(blocks)) {
        setProperties(obj);
    }

    collectables["COIN"] = gltf.scene.getObjectByName("Coin003");
    collectables["gem_pink"] = gltf.scene.getObjectByName("Gem_Pink");
    collectables["gem_green"] = gltf.scene.getObjectByName("Gem_Green");
    for (const obj of Object.values(collectables)) {
        setProperties(obj, 0.2);
    }

    decorations["tree"] = gltf.scene.getObjectByName("Tree_Fruit");
    decorations["bush"] = gltf.scene.getObjectByName("Bush_Fruit001");
    decorations["cloud1"] = gltf.scene.getObjectByName("Cloud_1");
    decorations["cloud2"] = gltf.scene.getObjectByName("Cloud_2");
    decorations["cloud3"] = gltf.scene.getObjectByName("Cloud_3");

    for (const obj of Object.values(decorations)) {
        setProperties(obj, 1.5);
    }

    grass_decorations.push(gltf.scene.getObjectByName(`Grass_3008`));
    grass_decorations.push(gltf.scene.getObjectByName(`Grass_3011`));
    grass_decorations.push(gltf.scene.getObjectByName(`Grass_3013`));
    grass_decorations.push(gltf.scene.getObjectByName(`Plant_Small`));
    grass_decorations.push(gltf.scene.getObjectByName(`Plant_Large`));
    for (const obj of Object.values(grass_decorations)) {
        let point1 = new THREE.Vector3();
        if (obj) {
            obj.getWorldPosition(point1);
            setProperties(obj, 1);
        }
    }

    return {
        blocks,
        collectables,
        decorations,
        grass_decorations,
        animations
    };
}

export async function loadRobotAssets(robotOrientation) {
    const loader = new GLTFLoader();
    let direction;
    switch (robotOrientation) {
        case "DOWN":
            direction = new THREE.Vector3(0, 0, 1).normalize();
            break;
        case "UP":
            direction = new THREE.Vector3(0, 0, -1).normalize();
            break;
        case "LEFT":
            direction = new THREE.Vector3(-1, 0, 0).normalize();
            break;
        case "RIGHT":
            direction = new THREE.Vector3(1, 0, 0).normalize();
            break;
        default:
            // default to down
            direction = new THREE.Vector3(0, 0, 1).normalize();
    }
    const speed = 0.05;
    const vector = direction.multiplyScalar(speed, speed, speed);


    const gltf = await loader.loadAsync("assets/models/robot/RobotExpressive.gltf");
    const model = gltf.scene;

    // Without this, there's an issue that hides certain
    // parts of the model when animating:
    // https://discourse.threejs.org/t/parts-of-glb-object-disappear-in-certain-angles-and-zoom/21295/4
    model.traverse(function (node) {
        node.frustumCulled = false;
    });

    model.lookAt(vector);
    const animations = gltf.animations;

    return {
        model,
        animations,
    };
}
